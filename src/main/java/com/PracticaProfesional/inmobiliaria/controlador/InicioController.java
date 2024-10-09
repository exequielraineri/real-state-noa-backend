/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Exequiel
 */
@Controller
@RequestMapping
public class InicioController {

    @GetMapping({"", "/","/inicio"})
    public String inicio(Model model, HttpServletRequest request) {
        model.addAttribute("titulo", "Real State");
        model.addAttribute("request", request);
        return "inicio";
    }

}
