/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import com.PracticaProfesional.inmobiliaria.servicios.UsuarioServicios;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
@RequestMapping("usuario")
public class UsuarioControl {

    @Autowired
    UsuarioServicios userSerive;

    @GetMapping("")
    public String cargaUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("listado_usuario", obtenerUsuario());
        return "users";
    }

    @PostMapping("/nuevo_usuario")
    public String crearUsuario(@ModelAttribute("usuario") Usuario usuario) {
        userSerive.guardar(usuario);
        return "redirect:/usuario";
    }

    private List<Usuario> obtenerUsuario() {
        return userSerive.listar();
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        userSerive.eliminar(id);
        return "redirect:/usuario";
    }

    @PostMapping("/guardar_modificacion")
    public String modificar(@ModelAttribute("usuario") Usuario usuario, @RequestParam("fechaNacimiento") String fecha) {
        userSerive.guardar(usuario);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fechaNacimiento = sdf.parse(fecha);
            usuario.setFechaNacimiento(fechaNacimiento);
        } catch (ParseException e) {
           
        }
        return "redirect:/usuario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Usuario usuario = userSerive.obtener(id).orElse(null);
        model.addAttribute("usuario", usuario);
        return "editarUser";
    }

    

}
