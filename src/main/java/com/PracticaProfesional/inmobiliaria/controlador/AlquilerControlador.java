/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author exera
 */
@Controller
@RequestMapping("alquiler")
public class AlquilerControlador {

    @GetMapping
    public String inicioAlquiler(Model model) {
        model.addAttribute("contenido", "alquiler");
        model.addAttribute("titulo", "Real State | Alquiler");
        return "layout";
    }

}
