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
import com.PracticaProfesional.inmobiliaria.servicios.ImagenServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Sofia
 */
@Controller
@RequestMapping("inmuebles")
public class InmuebleControlador {

    @Autowired
    private InmuebleServicios inmuebleServicio;
    @Autowired
    private ClienteServicios clienteServicio;

    @Autowired
    private ImagenServicios imagenServicio;
    private Inmueble inmueble = new Inmueble();

    private String RUTA_IMAGENES = "src/main/resources/static/img";

    @GetMapping
    public String inicio(Model model, HttpServletRequest request) {
        this.inmueble = new Inmueble();
        List<Inmueble> inmuebles = inmuebleServicio.listar();
        String tipo = request.getParameter("tipo") == null ? "Todos" : request.getParameter("tipo");
        String estado = request.getParameter("estado") == null ? "" : request.getParameter("estado");
        String ubicacion = request.getParameter("ubicacion") == null ? "" : request.getParameter("ubicacion");
        if (!tipo.equals("Todos")) {
            inmuebles = inmuebles.stream().filter(inmueble -> inmueble.getTipoInmueble().name().toLowerCase().contains(tipo.toLowerCase())).collect(Collectors.toList());
        }
        if (!estado.equals("Todos")) {
            inmuebles = inmuebles.stream().filter(inmueble -> inmueble.getEstado().toLowerCase().contains(estado.toLowerCase())).collect(Collectors.toList());
        }
        if (!ubicacion.equals("")) {
            inmuebles = inmuebles.stream().filter(inmueble -> inmueble.getDireccion().toLowerCase().contains(ubicacion.toLowerCase())).collect(Collectors.toList());
        }

        model.addAttribute("titulo", "Real State | Inmuebles");
        model.addAttribute("request", request);
        model.addAttribute("inmueble", inmueble);
        model.addAttribute("clientes", clienteServicio.listar());
        model.addAttribute("inmuebles", inmuebles);
        return "inmuebles";

    }

    @GetMapping("/nuevo")
    public String nuevoInmueble(Model model, HttpServletRequest request) {
        model.addAttribute("titulo", "Real State | Nuevo");
        model.addAttribute("request", request);
        inmueble = new Inmueble();
        model.addAttribute("inmueble", inmueble);
        model.addAttribute("clientes", clienteServicio.listar());
        return "nuevoInmueble";
    }

    @PostMapping(value = "/nuevo", consumes = {"multipart/form-data"})
    public String cargar(
            @ModelAttribute("propietario") String idPropietario,
            @ModelAttribute("inmueble") Inmueble inmueble,
            @RequestParam(name = "archivos") List<MultipartFile> imagenes,
            HttpServletRequest request) {
        Date fechaRegistro = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        inmueble.setFechaRegistro(fechaRegistro);
        inmueble.setEstado("Mantenimiento");
        inmueble.setTipoOperacion(inmueble.getIsVenta() ? "Venta" : "Alquiler");
        inmueble.setTipoInmueble(EnumTipoInmuebles.valueOf(request.getParameter("tipo")));
        Path directorioPath = Paths.get(RUTA_IMAGENES);
        if (!Files.exists(directorioPath)) {
            try {
                Files.createDirectories(directorioPath); // Crear directorio si no existe
            } catch (IOException ex) {
                System.err.println("Error: " + ex.getMessage());
                Logger.getLogger(InmuebleControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        for (MultipartFile image : imagenes) {
            try {
                Path rutaImagen = directorioPath.resolve(sf.format(fechaRegistro) + "_" + image.getOriginalFilename());
                Files.write(rutaImagen, image.getBytes());
                Imagen imagen = new Imagen();
                imagen.setNombre(sf.format(fechaRegistro) + "_" + image.getOriginalFilename());
                inmueble.addImagen(imagen);
            } catch (IOException ex) {
                System.err.println("Error: " + ex.getMessage());
                Logger.getLogger(InmuebleControlador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Cliente propietario = clienteServicio.obtener(inmueble.getPropietario().getId()).orElse(null);
        if (propietario != null) {
            inmueble.setPropietario(propietario);
            inmuebleServicio.guardar(inmueble);

        }

        return "redirect:/inmuebles";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        inmuebleServicio.eliminar(id);
        return "redirect:/inmuebles";
    }

    @PostMapping("/guardar_modificacion")
    public String modificar(@ModelAttribute("inmueble") Inmueble inmuebleNuevo) {
        inmueble = inmuebleServicio.obtener(inmuebleNuevo.getId()).get();

        inmueble.setTitulo(inmuebleNuevo.getTitulo());
        inmueble.setDescripcion(inmuebleNuevo.getDescripcion());
        inmuebleServicio.guardar(inmueble);

        return "redirect:/inmuebles";

    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, HttpServletRequest request) {
        this.inmueble = inmuebleServicio.obtener(id).get();
        model.addAttribute("inmueble", inmueble);
        request.setAttribute("tipo", inmueble.getTipoInmueble().name());
        model.addAttribute("titulo", "Real State | Editando");
        model.addAttribute("request", request);
        model.addAttribute("clientes", clienteServicio.listar());
        return "nuevoInmueble";
    }

    @GetMapping("/filtrar")
    public String filtrarUsuario(
            @RequestParam(required = false) String tipoinmueble,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) String estado,
            Model model) {

        // Filtrar la lista de clientes según los parámetros de búsqueda
        List<Inmueble> inmuebleFiltrados = inmuebleServicio.listarInmuebles(tipoinmueble, ubicacion, estado);
        model.addAttribute("inmueble", new Inmueble());
        // Añadir los filtros actuales al modelo para mantener el valor en los inputs
        model.addAttribute("tipoinmuebleFiltro", tipoinmueble);
        model.addAttribute("ubicacionFiltro", ubicacion);
        model.addAttribute("estadoFiltro", estado);

        // Enviar la lista filtrada a la vista
        model.addAttribute("listado_inmueble", inmuebleFiltrados);
        return "Inmueble";
    }

    @GetMapping("/listar_todos")
    public String listarTodos(Model model) {
        model.addAttribute("inmueble", new Inmueble());
        model.addAttribute("listado_inmueble", inmuebleServicio.listar()); // Obtiene todos los clientes
        return "Inmueble"; // Retorna a la plantilla index
    }

    @GetMapping("/ver/{id}")
    public String verInmueble(@PathVariable Integer id, Model model, HttpServletRequest request) {
        model.addAttribute("request", request);
        model.addAttribute("titulo", "Real State | Detalle");
        Inmueble inmueble = inmuebleServicio.obtener(id).get();
        for (Imagen imagene : inmueble.getImagenes()) {
            System.out.println(imagene.getNombre());
        }
        model.addAttribute("inmueble", inmueble);

        return "verInmueble";
    }

}
