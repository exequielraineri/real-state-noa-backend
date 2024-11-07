/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoCliente;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @param provincia
     * @param estado
     * @return
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> listar(
            @RequestParam(name = "tipoCliente", required = false) EnumTipoCliente tipoCliente,
            @RequestParam(name = "provincia", required = false) String provincia,
            @RequestParam(name = "estado", required = false,defaultValue = "true") boolean estado) {
        try {
            response = new HashMap<>();            
            response.put("data", cliService.listarPorFiltros(tipoCliente, provincia, estado));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("tipoCliente", tipoCliente);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> obtenerCliente(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Cliente cliente = cliService.obtener(id).orElse(null);
            if (cliente == null) {
                response.put("data", "No se encontro el cliente");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response.put("data", cliente);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
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
    public ResponseEntity<Map<String, Object>> nuevoCliente(@RequestBody Cliente cliente
    ) {
        try {
            response = new HashMap<>();
            Cliente clienteBD = cliService.guardar(cliente);
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
    public ResponseEntity<Map<String, Object>> modificar(@RequestBody Cliente cliente, @PathVariable Integer id
    ) {
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
        viejo.setTelefono(nuevo.getTelefono());
    }

}
