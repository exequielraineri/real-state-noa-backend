/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de clientes
 *
 * @author Sofia
 */
@CrossOrigin("*")
@RestController
@RequestMapping("clientes")
public class ClienteControlador {

    Map<String, Object> response;

    @Autowired
    ClienteServicios cliService;

    /**
     *
     * @param tipoCliente
     * @return
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> listar(
            @RequestParam(name = "tipoCliente", defaultValue = "") String tipoCliente) {
        try {
            response = new HashMap<>();

            if (tipoCliente.isEmpty()) {
                response.put("data", cliService.listar());
            } else {
                response.put("data", cliService.listarPorTipoCliente(tipoCliente));
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("tipoCliente", tipoCliente);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *
     * @param cliente
     * @return
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> nuevoCliente(@RequestBody Cliente cliente) {
        try {
            Cliente clienteBD = cliService.guardar(cliente);
            response = new HashMap<>();
            response.put("data", clienteBD);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Cliente clienteBD = cliService.obtener(id).orElse(null);
            if (clienteBD == null) {
                response.put("data", "No se encontro cliente");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            cliService.eliminar(clienteBD.getId());
            response.put("data", "Se elimino el cliente id: " + id);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> modificar(@RequestBody Cliente cliente, @PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Cliente clienteBD = cliService.obtener(id).orElse(null);
            if (clienteBD == null) {
                response.put("data", "No se encontro Cliente");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            actualizarDatos(clienteBD, cliente);
            response.put("data", cliService.guardar(cliente));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void actualizarDatos(Cliente viejo, Cliente nuevo) {
        viejo.setNombre(nuevo.getNombre());
        viejo.setApellido(nuevo.getApellido());
        viejo.setProvincia(nuevo.getProvincia());
        viejo.setEstado(nuevo.getEstado());
        viejo.setTelefono(nuevo.getTelefono());
    }

}
