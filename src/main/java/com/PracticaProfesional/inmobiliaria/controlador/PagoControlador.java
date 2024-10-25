/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Pago;
import com.PracticaProfesional.inmobiliaria.servicios.PagosServicios;
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
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Sofia
 */
@CrossOrigin("*")
@RestController
@RequestMapping("pagos")
public class PagoControlador {

    private Map<String, Object> response;

    @Autowired
    private PagosServicios pagoService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> pagos() {
        try {
            response = new HashMap<>();
            response.put("data", pagoService.listar());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> obtenerPagos(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Pago pagoDB = pagoService.obtener(id).orElse(null);
            if (pagoDB == null) {
                response.put("data", "no se encontro pago");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("data", pagoDB);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping
    public ResponseEntity<Map<String, Object>> nuevoPago(@RequestBody Pago pago) {
        try {
            response = new HashMap<>();
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Pago pago = pagoService.obtener(id).orElse(null);
            if (pago == null) {
                response.put("data", "No se Encontro pago");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            pagoService.eliminar(pago.getId());
            response.put("data", "Se elimino pago id " + id);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> modificar(@RequestBody Pago pago, @PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Pago pagoDB = pagoService.obtener(id).orElse(null);
            if (pagoDB == null) {
                response.put("data", "No se encontro pago");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            pagoDB.setEstado(pago.getEstado());
            pagoDB.setMetodoPago(pago.getMetodoPago());

            response.put("data", pagoService.guardar(pagoDB));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
