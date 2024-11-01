/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.Transaccion;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.ContratoServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import com.PracticaProfesional.inmobiliaria.servicios.TransaccionServicios;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Sofia
 */
@CrossOrigin("*")
@RestController
@RequestMapping("reportes")
public class ReporteControlador {

    @Autowired
    private InmuebleServicios inmuebleServicios;
    @Autowired
    private ClienteServicios clienteServicios;
    @Autowired
    private TransaccionServicios transaccionServicios;
    @Autowired
    private ContratoServicios contratoServicios;

    Map<String, Object> response;

    @GetMapping
    public ResponseEntity<Map<String, Object>> contrato() {
        try {
            response = new HashMap<>();
            response.put("data", contratoServicios.listar());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> obtenerContratos(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Contrato contrato = contratoServicios.obtener(id).orElse(null);
            if (contrato == null) {
                response.put("data", "No se encontro el contrato");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("data", contrato);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarClientes(
            @RequestParam(name = "tipoCliente", defaultValue = "") String tipoCliente) {
        try {
            response = new HashMap<>();

            if (tipoCliente.isEmpty()) {
                response.put("data", clienteServicios.listar());
            } else {
                response.put("data", clienteServicios.listarPorTipoCliente(tipoCliente));
            }
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
            Cliente cliente = clienteServicios.obtener(id).orElse(null);
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
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarInmuebles(
            @RequestParam(name = "tipoInmueble", required = false) String tipoInmueble,
            @RequestParam(name = "direccion", required = false) String direccion,
            @RequestParam(name = "estado", required = false) String estado
    ) {
        try {
            response = new HashMap<>();

            response.put("data", inmuebleServicios.listar(tipoInmueble, direccion, estado));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> obtenerInmuebles(@PathVariable Integer id) {
        try {
            response = new HashMap<>();

            Inmueble inmueble = inmuebleServicios.obtener(id).orElse(null);
            if (inmueble == null) {
                response.put("data", "No se encontro el inmueble");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            response.put("data", inmueble);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTransaccion() {
        try {
            response = new HashMap<>();
            response.put("data", transaccionServicios.listar());
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
            Transaccion transaccionDB = transaccionServicios.obtener(id).orElse(null);
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
}
