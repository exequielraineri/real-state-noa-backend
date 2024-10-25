/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Imagen;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.ContratoServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import com.PracticaProfesional.inmobiliaria.servicios.UsuarioServicios;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Sofia
 */
@CrossOrigin("*")
@RestController
@RequestMapping("inmuebles")
public class InmuebleControlador {

    Map<String, Object> response;

    @Autowired
    private InmuebleServicios inmuebleServicio;
    @Autowired
    private ClienteServicios clienteServicio;
    @Autowired
    private UsuarioServicios usuarioServicio;

    @Autowired
    private ContratoServicios contratoService;

    private final String RUTA_IMAGENES = System.getProperty("user.dir") + "/imagenes/";

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        try {
            response = new HashMap<>();
            response.put("data", inmuebleServicio.listar());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> guardar(
            @RequestBody(required = true) Inmueble inmueble) {
        try {
            response = new HashMap<>();
            Cliente propietario = obtenerPropietario(inmueble.getPropietario().getId().toString());
            if (propietario != null) {
                procesarInformacionInmueble(inmueble);
                inmueble.setPropietario(propietario);
                inmueble = inmuebleServicio.guardar(inmueble);
            } else {
                response.put("data", "Propietario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("data", inmueble);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            response.put("msj", "Error Otros");
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void procesarInformacionInmueble(Inmueble inmueble) {
        inmueble.setFechaRegistro(new Date());
        inmueble.setEstado("Mantenimiento");
        inmueble.setTipoOperacion(inmueble.getIsVenta() ? "Venta" : "Alquiler");
    }

    @PostMapping(value = "subir-imagen/{idInmueble}", consumes = {"multipart/form-data"})
    private ResponseEntity<Map<String, Object>> subirImagenes(
            @PathVariable(name = "idInmueble") Integer id,
            @RequestParam(name = "imagenes") MultipartFile[] imagenes) {
        Date fechaRegistro = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        Path directorioPath = Paths.get(RUTA_IMAGENES);
        try {
            response = new HashMap<>();
            Inmueble inmueble = inmuebleServicio.obtener(id).orElse(null);
            if (inmueble == null) {
                response.put("data", "No se encontro el inmueble");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

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
            inmueble = inmuebleServicio.guardar(inmueble);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (IOException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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

            if (true) {
                response.put("data", "hgola");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            /*
            Inmueble inmuebleBD = inmuebleServicio.obtener(id).orElse(null);

            if (inmuebleBD == null) {
                response.put("data", "no se encontro Inmueble");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            

            actualizarDatos(inmuebleBD, inmueble);
            //inmuebleBD = inmuebleServicio.guardar(inmuebleBD);
            
             */
            response.put("data", "Exitoso");
            //          response.put("ejemplo", inmuebleBD);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void actualizarDatos(Inmueble inmuebleBD, Inmueble nuevo) {
        inmuebleBD.setTitulo(nuevo.getTitulo());
        inmuebleBD.setDescripcion(nuevo.getDescripcion());
        inmuebleBD.setDireccion(nuevo.getDireccion());
        //viejo.setTipoOperacion(nuevo.getTipoOperacion());
        inmuebleBD.setBanios(nuevo.getBanios());
        //viejo.setContratos(nuevo.getContratos());
        inmuebleBD.setCantAmbientes(nuevo.getCantAmbientes());
        //viejo.setEstado(nuevo.getEstado());
        inmuebleBD.setExpensas(nuevo.getExpensas());
        //viejo.setFechaPublicacion(nuevo.getFechaPublicacion());
        //viejo.setFechaRegistro(nuevo.getFechaRegistro());
        inmuebleBD.setHectareas(nuevo.getHectareas());
        inmuebleBD.setImpInmobiliarios(nuevo.getImpInmobiliarios());
        inmuebleBD.setImpMunicipales(nuevo.getImpMunicipales());
        inmuebleBD.setMts2(nuevo.getMts2());
        inmuebleBD.setIsAccesoRuta(nuevo.getIsAccesoRuta());
        inmuebleBD.setIsRiego(nuevo.getIsRiego());
        inmuebleBD.setIsVenta(nuevo.getIsVenta());
        inmuebleBD.setIsVidriera(nuevo.getIsVidriera());
        inmuebleBD.setPrecioAlquiler(nuevo.getPrecioAlquiler());
        inmuebleBD.setPrecioVenta(nuevo.getPrecioVenta());
        //viejo.setPropietario(nuevo.getPropietario());
        inmuebleBD.setTipoAmbiente(nuevo.getTipoAmbiente());
        inmuebleBD.setTipoInmueble(nuevo.getTipoInmueble());
    }

    @GetMapping("imagen/{nombreImagen:.+}")
    @ResponseBody
    public ResponseEntity<Resource> mostrarImagen(@PathVariable String nombreImagen) {
        try {
            Path imagen = Paths.get(RUTA_IMAGENES).resolve(nombreImagen);
            Resource resource = new UrlResource(imagen.toUri());

            if (resource.exists() || resource.isReadable()) {
                String contentType;
                contentType = Files.probeContentType(imagen);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("No se puede leer el archivo: " + nombreImagen);
            }
        } catch (RuntimeException | MalformedURLException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> obtener(@PathVariable Integer id) {
        try {
            response = new HashMap<>();

            Inmueble inmueble = inmuebleServicio.obtener(id).orElse(null);
            if (inmueble == null) {
                response.put("data", "No se encontro el inmueble");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("data", inmueble);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
