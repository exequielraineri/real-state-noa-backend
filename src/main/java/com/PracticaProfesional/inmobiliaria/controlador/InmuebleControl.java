/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Imagen;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.ImagenServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
public class InmuebleControl {

    @Autowired
    private InmuebleServicios inmobiliariaServicio;
    @Autowired
    private ClienteServicios clienteServicio;

    @Autowired
    private ImagenServicios imagenServicios;
    private Inmueble inmueble;

    private String RUTA_IMAGENES = "src/main/resources/static/img";

    @GetMapping
    public String nuevoInmueble(Model model, HttpServletRequest request) {
        model.addAttribute("contenido", "fragmentos/Inmueble");
        model.addAttribute("titulo", "Real State | Inmuebles");
        model.addAttribute("request", request);

        inmueble = new Inmueble();
        model.addAttribute("inmueble", inmueble);
        model.addAttribute("cliente", clienteServicio.listar());
        model.addAttribute("listado_inmueble", inmobiliariaServicio.listar());

        return "layout";

    }

    @PostMapping(value = "/nuevo", consumes = {"multipart/form-data"})
    public String cargar(@ModelAttribute("inmueble") Inmueble inmueble,
            @ModelAttribute("idPropietario") Cliente cliente,
            @RequestParam(value = "imagenPrincipal") MultipartFile imagenPrincipal, HttpServletRequest request) throws IOException {
        inmueble.setIdPropietario(cliente.getId());
        inmueble.setApPro(cliente.getApellido());
        inmueble.setNomPro(cliente.getNombre());
        System.out.println(imagenPrincipal);
        System.out.println("Entra");
        System.out.println("tipo: " + request.getContentType());
        //inmueble = inmobiliariaServicio.guardar(inmueble);

        Path directorioPath = Paths.get(RUTA_IMAGENES);
        if (!Files.exists(directorioPath)) {
            Files.createDirectories(directorioPath); // Crear directorio si no existe
        }

        if (imagenPrincipal != null) {

            if (imagenPrincipal != null) {

                Path rutaAbsoluta = directorioPath.resolve(imagenPrincipal.getOriginalFilename());
                Files.write(rutaAbsoluta, imagenPrincipal.getBytes());

                Imagen imagen = new Imagen();
                imagen.setNombre(imagenPrincipal.getOriginalFilename());
                inmueble.addImagen(imagen);
                System.out.println("----->" + inmueble.getId());

            }

        }

        inmobiliariaServicio.guardar(inmueble);
//System.out.println("Imagen nombre: " + imagenPrincipal.getOriginalFilename());
        return "redirect:/";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        inmobiliariaServicio.eliminar(id);
        return "redirect:/inmuebles";
    }

    @PostMapping("/guardar_modificacion")
    public String modificar(@ModelAttribute("inmueble") Inmueble inmueble) {
        inmobiliariaServicio.guardar(inmueble);
        return "redirect:/inmuebles";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Inmueble inmueble = inmobiliariaServicio.obtener(id).get();
        model.addAttribute("inmueble", inmueble);

        return "redirect:/inmuebles";
    }

    @GetMapping("/filtrar")
    public String filtrarUsuario(
            @RequestParam(required = false) String tipoinmueble,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) String estado,
            Model model) {

        // Filtrar la lista de clientes según los parámetros de búsqueda
        List<Inmueble> inmuebleFiltrados = inmobiliariaServicio.listarInmuebles(tipoinmueble, ubicacion, estado);
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
        model.addAttribute("listado_inmueble", inmobiliariaServicio.listar()); // Obtiene todos los clientes
        return "Inmueble"; // Retorna a la plantilla index
    }
}
