/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.control;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import org.springframework.ui.Model;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping("cliente")
public class ClienteControl {

    @Autowired
    ClienteServicios cliService;

    @GetMapping("")
    public String inicio(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("listadoCliente", obtenerCliente());
        return "index";
    }

    @PostMapping("/nuevoCliente")
    public String nuevoCliente(@ModelAttribute("cliente") Cliente clienete) {
        cliService.guardar(clienete);
        return "redirect:/cliente";
    }

    private List<Cliente> obtenerCliente() {
        return cliService.listar();
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        cliService.eliminar(id);
        return "redirect:/cliente";
    }

    @PostMapping("/GuardarModificacion")
    public String modificar(@ModelAttribute("cliente") Cliente cliente) {
        cliService.guardar(cliente);
        return "redirect:/cliente";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Cliente cliente = cliService.obtener(id).get();
        model.addAttribute("cliente", cliente);
        return "editarClientes"; 
    }

}
