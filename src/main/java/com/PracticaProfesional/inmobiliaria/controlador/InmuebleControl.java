/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Imagen;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import jakarta.servlet.http.HttpServletRequest;
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

/**
 *
 * @author Sofia
 */
@Controller
@RequestMapping("inmuebles")
public class InmuebleControl {

    @Autowired
    private InmuebleServicios inmuServicio;
    @Autowired
    private ClienteServicios cliServicio;

    @GetMapping
    public String nuevoInmueble(Model model, HttpServletRequest request) {
        model.addAttribute("request", request);
        model.addAttribute("inmueble", new Inmueble());
        model.addAttribute("cliente", cliServicio.listar());
        model.addAttribute("listdo_inmueble", obtenerInmueble());
        model.addAttribute("contenido", "fragmentos/inmueble");
        model.addAttribute("titulo", "Real State | Inmuebles");
        return "layout";

    }

    @PostMapping("/cargar")
    public String cargar(@ModelAttribute("inmueble") Inmueble inmueble,
            @ModelAttribute("cliente") Cliente cliente,
            @ModelAttribute("imagen") Imagen imagen) {
        inmueble.setIdPropietario(cliente.getId());
        Collection<Imagen> imagenes = new ArrayList<>();
        imagenes.add(imagen);
        inmueble.setImagenCollection(imagenes);
        inmuServicio.guardar(inmueble);
        return "redirect:/inmueble";
    }

    private List<Inmueble> obtenerInmueble() {
        return inmuServicio.listar();
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        inmuServicio.eliminar(id);
        return "redirect:/inmueble";
    }

    @PostMapping("/guardar_modificacion")
    public String modificar(@ModelAttribute("inmueble") Inmueble inmueble) {
        inmuServicio.guardar(inmueble);
        return "redirect:/inmueble";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Inmueble inmueble = inmuServicio.obtener(id).get();
        model.addAttribute("inmueble", inmueble);
        return "editarInmueble";
    }

    @GetMapping("/filtrar")
    public String filtrarUsuario(
            @RequestParam(required = false) String tipoinmueble,
            @RequestParam(required = false) String ubicacion,
            @RequestParam(required = false) String estado,
            Model model) {

        // Filtrar la lista de clientes según los parámetros de búsqueda
        List<Inmueble> inmuebleFiltrados = inmuServicio.listarInmuebles(tipoinmueble, ubicacion, estado);
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
        model.addAttribute("listado_inmueble", obtenerInmueble()); // Obtiene todos los clientes
        return "Inmueble"; // Retorna a la plantilla index
    }
}
