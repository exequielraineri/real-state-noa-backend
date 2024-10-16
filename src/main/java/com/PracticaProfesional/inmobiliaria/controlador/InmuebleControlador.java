/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Imagen;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoInmuebles;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Sofia
 */
@CrossOrigin("*")
@Controller
@RequestMapping("inmuebles")
public class InmuebleControlador {

    Map<String, Object> response;

    @Autowired
    private InmuebleServicios inmuebleServicio;
    @Autowired
    private ClienteServicios clienteServicio;

    private final String RUTA_IMAGENES = "src/main/resources/static/img";

    @GetMapping
    public ResponseEntity<Map<String, Object>> inicio() {
        try {
            response = new HashMap<>();
            response.put("data", inmuebleServicio.listar());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> cargar(
            @RequestParam String idPropietario,
            @ModelAttribute Inmueble inmueble,
            @RequestParam(name = "archivos") List<MultipartFile> imagenes,
            HttpServletRequest request) {
        try {
            response = new HashMap<>();
            procesarInformacionInmueble(inmueble, request);
            guardarImagenes(inmueble, imagenes);
            Cliente propietario = obtenerPropietario(idPropietario);
            if (propietario != null) {
                inmueble.setPropietario(propietario);
                inmuebleServicio.guardar(inmueble);
            } else {
                response.put("data", "Propietario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response.put("data", "Inmueble registrado exitosamente");
            response.put("data", inmueble);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (IOException e) {
            response.put("data", "Error al registrar el inmueble");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private void procesarInformacionInmueble(Inmueble inmueble, HttpServletRequest request) {
        Date fechaRegistro = new Date();
        inmueble.setFechaRegistro(fechaRegistro);
        inmueble.setEstado("Mantenimiento");
        inmueble.setTipoOperacion(inmueble.getIsVenta() ? "Venta" : "Alquiler");
        inmueble.setTipoInmueble(EnumTipoInmuebles.valueOf(request.getParameter("tipo")));
    }

    private void guardarImagenes(Inmueble inmueble, List<MultipartFile> imagenes) throws IOException {
        Date fechaRegistro = inmueble.getFechaRegistro();
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        Path directorioPath = Paths.get(RUTA_IMAGENES);

        if (!Files.exists(directorioPath)) {
            Files.createDirectories(directorioPath);
        }

        for (MultipartFile image : imagenes) {
            Path rutaImagen = directorioPath.resolve(sf.format(fechaRegistro) + "_" + image.getOriginalFilename());
            Files.write(rutaImagen, image.getBytes());

            Imagen imagen = new Imagen();
            imagen.setNombre(sf.format(fechaRegistro) + "_" + image.getOriginalFilename());
            inmueble.addImagen(imagen);
        }
    }

    private Cliente obtenerPropietario(String idPropietario) {
        return clienteServicio.obtener(Integer.valueOf(idPropietario)).orElse(null);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Inmueble inmu = this.inmuebleServicio.obtener(id).orElse(null);
            if (inmu == null) {
                response.put("data", "No se encontro Inmueble");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            inmuebleServicio.eliminar(inmu.getId());
            response.put("data", "Se elimino el inmueble id " + id);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> modificar(@RequestBody Inmueble inmueble, @PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Inmueble inmu = inmuebleServicio.obtener(id).orElse(null);
            if (inmu == null) {
                response.put("data", "no se encontro Inmueble");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            actualizarDatos(inmu, inmueble);
            response.put("data", inmuebleServicio.guardar(inmueble));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void actualizarDatos(Inmueble viejo, Inmueble nuevo) {
        viejo.setTitulo(nuevo.getTitulo());
        viejo.setDescripcion(nuevo.getDescripcion());
        viejo.setDireccion(nuevo.getDireccion());
        viejo.setTipoOperacion(nuevo.getTipoOperacion());
        viejo.setBanios(nuevo.getBanios());
        viejo.setCantAmbientes(nuevo.getCantAmbientes());
        viejo.setEstado(nuevo.getEstado());
        viejo.setExpensas(nuevo.getHectareas());
        viejo.setFechaPublicacion(nuevo.getFechaPublicacion());
        viejo.setFechaRegistro(nuevo.getFechaRegistro());
        viejo.setHectareas(nuevo.getHectareas());
        viejo.setImpInmobiliarios(nuevo.getImpInmobiliarios());
        viejo.setImpMunicipales(nuevo.getImpMunicipales());
        viejo.setMts2(nuevo.getMts2());
        viejo.setIsAccesoRuta(nuevo.getIsAccesoRuta());
        viejo.setIsRiego(nuevo.getIsRiego());
        viejo.setIsVenta(nuevo.getIsVenta());
        viejo.setIsVidriera(nuevo.getIsVidriera());
        viejo.setPrecioAlquiler(nuevo.getPrecioAlquiler());
        viejo.setPrecioVenta(nuevo.getPrecioVenta());
        viejo.setPropietario(nuevo.getPropietario());
        viejo.setTipoAmbiente(nuevo.getTipoAmbiente());
        viejo.setTipoInmueble(nuevo.getTipoInmueble());
    }

//    @GetMapping("/filtrar")
//    public String filtrarUsuario(
//            @RequestParam(required = false) String tipoinmueble,
//            @RequestParam(required = false) String ubicacion,
//            @RequestParam(required = false) String estado,
//            Model model) {
//
//        // Filtrar la lista de clientes según los parámetros de búsqueda
//        List<Inmueble> inmuebleFiltrados = inmuebleServicio.listarInmuebles(tipoinmueble, ubicacion, estado);
//        model.addAttribute("inmueble", new Inmueble());
//        // Añadir los filtros actuales al modelo para mantener el valor en los inputs
//        model.addAttribute("tipoinmuebleFiltro", tipoinmueble);
//        model.addAttribute("ubicacionFiltro", ubicacion);
//        model.addAttribute("estadoFiltro", estado);
//
//        // Enviar la lista filtrada a la vista
//        model.addAttribute("listado_inmueble", inmuebleFiltrados);
//        return "Inmueble";
//    }
//
//    @GetMapping("/ver/{id}")
//    public String verInmueble(@PathVariable Integer id, Model model, HttpServletRequest request) {
//        model.addAttribute("request", request);
//        model.addAttribute("titulo", "Real State | Detalle");
//        Inmueble inmueble = inmuebleServicio.obtener(id).get();
//        for (Imagen imagene : inmueble.getImagenes()) {
//            System.out.println(imagene.getNombre());
//        }
//        model.addAttribute("inmueble", inmueble);
//
//        return "verInmueble";
//    }
}
