/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import jakarta.faces.annotation.RequestMap;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Exequiel
 */
@Controller
public class ErrorControl implements ErrorController {

    @RequestMapping("/error")
    public String manejarError(Model model,HttpServletRequest request) {
        model.addAttribute("request", request);
        model.addAttribute("contenido", "fragmentos/404");
        model.addAttribute("titulo", "Not Fount");
        return "layout";
    }

}
