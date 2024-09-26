/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Sofia
 */
@Controller
@RequestMapping("inmueble")
public class InmuebleControl {

    @Autowired
    private InmuebleServicios inmuServicio;

    @GetMapping("")
    public String nuevoInmueble(Model model) {
        model.addAttribute("inmuelbe", new Inmueble());
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("listdo-inmueble", obtenerInmueble());
        return "Inmueble";
    }

    @PostMapping("/cargar")
    public String inscribir(@ModelAttribute("inmueble") Inmueble inmueble, @ModelAttribute("cliente") Cliente cliente) {
        inmueble.setIdPropietario(cliente.getId());
        inmuServicio.guardar(inmueble);
        return "Inmueble";
    }

    private List<Inmueble> obtenerInmueble() {
        return inmuServicio.listar();
    }
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id){
        inmuServicio.eliminar(id);
        return "redirect:/inmueble";
    }
     @PostMapping("/guardar-modificacion")
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
}
