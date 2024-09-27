/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Pagos;
import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import com.PracticaProfesional.inmobiliaria.servicios.ContratoServicios;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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
@RequestMapping("ventas")
public class VentaControl {

    @Autowired
    private ContratoServicios conService;

    @GetMapping("")
    public String venta(Model model, HttpServletRequest request) {
        model.addAttribute("request", request);
        model.addAttribute("contrato", new Contrato());
        model.addAttribute("pago", new Pagos());
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("listar", obtenerVenta());
        model.addAttribute("contenido", "fragmentos/ventas");
        model.addAttribute("titulo", "Real State | Ventas");
        return "layout";
    }

    private List<Contrato> obtenerVenta() {
        return conService.listar().stream()
                .filter(transaccion -> "venta".equals(transaccion.getTipoOperacion()))
                .collect(Collectors.toList());
    }

    @PostMapping("asd")
    public String nuevaVenta(@ModelAttribute("transaccion") Contrato contrato,
            @ModelAttribute("pagos") Pagos pago,
            @ModelAttribute("cliente") Cliente cliente,
            @ModelAttribute("usuario") Usuario usuario,
            @ModelAttribute("inmueble") Inmueble inmueble) {
        contrato.setIdCliente(cliente);
        contrato.setIdAgente(usuario);
        contrato.setIdInmueble(inmueble);
        Collection<Pagos> pagos = new ArrayList<>();
        pagos.add(pago);
        contrato.setPagosCollection(pagos);
        conService.guardar(contrato);
        return "";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        conService.eliminar(id);
        return "";
    }
}
