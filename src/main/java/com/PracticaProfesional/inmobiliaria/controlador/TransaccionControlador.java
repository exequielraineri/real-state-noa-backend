/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Transaccion;
import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import com.PracticaProfesional.inmobiliaria.servicios.TransaccionServicios;
import com.PracticaProfesional.inmobiliaria.servicios.UsuarioServicios;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
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
 *
 * @author Sofia
 */
@CrossOrigin("*")
@RestController
@RequestMapping("transacciones")
public class TransaccionControlador {

    Map<String, Object> response;
    @Autowired
    private TransaccionServicios tranService;

    @Autowired
    private UsuarioServicios usuarioService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar(
            @RequestParam(required = false, name = "estado", defaultValue = "true") boolean estado,
            @RequestParam(required = false, name = "fechaDesde") LocalDateTime fechaDesde,
            @RequestParam(required = false, name = "fechaHasta") LocalDateTime fechaHasta,
            @RequestParam(required = false, name = "tipoTransaccion") String TipoTransaccion,
            @RequestParam(required = false, name = "tipoOperacion") String TipoOperacion
    ) {
        try {
            response = new HashMap<>();
            response.put("data", tranService.listarFiltrado(estado, fechaDesde, fechaHasta, TipoOperacion, TipoTransaccion));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> obtenerTransaccion(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Transaccion transaccionDB = tranService.obtener(id).orElse(null);
            if (transaccionDB == null) {
                response.put("data", "no se encontro transaccion");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("data", transaccionDB);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> nuevaTransaccion(@RequestBody Transaccion transaccion) {
        try {
            response = new HashMap<>();
            Usuario usuario = usuarioService.obtener(transaccion.getAgente().getId()).orElse(null);

            if (usuario == null) {
                response.put("data", "No se encontro el usuario");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            transaccion.setFechaTransaccion(LocalDateTime.now());
            transaccion.setEstado(true);
            transaccion.setAgente(usuario);

            response.put("data", tranService.guardar(transaccion));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Transaccion transaccion = tranService.obtener(id).orElse(null);
            if (transaccion == null) {
                response.put("data", "No se Encontro Transaccion");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            tranService.eliminar(transaccion.getId());
            response.put("data", "Se elimino transaccion id " + id);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> modificar(@RequestBody Transaccion transaccion, @PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Transaccion transaccionDB = tranService.obtener(id).orElse(null);
            if (transaccionDB == null) {
                response.put("data", "No se encontro transaccion");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            transaccionDB.setDescripcion(transaccion.getDescripcion());

            response.put("data", tranService.guardar(transaccion));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
