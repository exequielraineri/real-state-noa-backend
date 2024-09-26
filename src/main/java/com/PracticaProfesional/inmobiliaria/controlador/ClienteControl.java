/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

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
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("listado_cliente", obtenerCliente());
        model.addAttribute("contenido", "index");
        model.addAttribute("titulo", "Real State | Clientes");
        return "layout";
    }

    @PostMapping("/nuevo_cliente")
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

    @PostMapping("/guardar_modificacion")
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

    @GetMapping("/filtrar")
    public String filtrarClientes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String provincia,
            Model model) {

        // Filtrar la lista de clientes según los parámetros de búsqueda
        List<Cliente> clientesFiltrados = cliService.filtrarClientes(nombre, apellido, provincia);
        model.addAttribute("cliente", new Cliente());
        // Añadir los filtros actuales al modelo para mantener el valor en los inputs
        model.addAttribute("nombreFiltro", nombre);
        model.addAttribute("apellidoFiltro", apellido);
        model.addAttribute("provinciaFiltro", provincia);

        // Enviar la lista filtrada a la vista
        model.addAttribute("listado_cliente", clientesFiltrados);
        return "index";
    }

    @GetMapping("/listar_todos")
    public String listarTodos(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("listado_cliente", obtenerCliente()); // Obtiene todos los clientes
        return "index"; // Retorna a la plantilla index
    }

}
