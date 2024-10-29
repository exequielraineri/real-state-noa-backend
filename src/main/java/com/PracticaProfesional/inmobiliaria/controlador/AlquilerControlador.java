/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.Pago;
import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoContrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoInmueble;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.ContratoServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import com.PracticaProfesional.inmobiliaria.servicios.UsuarioServicios;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.transaction.annotation.Transactional;
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
 * @author exera
 */
@CrossOrigin("*")
@RestController
@RequestMapping("alquiler")
public class AlquilerControlador {
    
    private Map<String, Object> response;
    @Autowired
    private ContratoServicios contratoService;
    @Autowired
    private ClienteServicios clienteService;
    @Autowired
    private UsuarioServicios usuarioService;
    @Autowired
    private InmuebleServicios inmuebleService;
    
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> inicioAlquiler() {
        try {
            response = new HashMap<>();
            List<Contrato> contratos = contratoService.listar();
            response.put("data", contratos);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> obtenerInmueble(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Contrato contrato = contratoService.obtener(id).orElse(null);
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
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> nuevoAlquiler(@RequestBody Contrato contrato) {
        try {
            response = new HashMap<>();
            
            Inmueble inmueble = inmuebleService.obtener(contrato.getInmueble().getId()).orElse(null);
            Cliente cliente = clienteService.obtener(contrato.getCliente().getId()).orElse(null);
            Usuario agente = usuarioService.obtener(contrato.getAgente().getId()).orElse(null);
            
            if (inmueble == null) {
                response.put("data", "No se encontro el inmueble");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            if (cliente == null) {
                response.put("data", "No se encontro el cliente");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            if (agente == null) {
                response.put("data", "No se encontro el agente");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
            contrato.setFechaContrato(new Date());
            
            contrato.setTipoContrato(EnumTipoContrato.ALQUILER);

            //seteamos el estado del inmueble a alquilado
            inmueble.setEstado(EnumEstadoInmueble.ALQUILADO);
            
            contrato.setInmueble(inmueble);
            
            agente.agregarContrato(contrato);
            
            contrato.setCliente(cliente);
            
            contrato.setEstado(EnumEstadoContrato.PENDIENTE);
            contrato.generarPagos();
            response.put("data", contratoService.guardar(contrato));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (HibernateException e) {
            response.put("error", "Error al procesar el Alquiler");
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (HttpMessageNotWritableException e) {
            response.put("error", "Error");
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Contrato contrato = contratoService.obtener(id).orElse(null);
            if (contrato == null) {
                response.put("data", "No se Encontro Contrato");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            contratoService.eliminar(contrato.getId());
            response.put("data", "Se elimino contrato id " + id);
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> modificar(@RequestBody Contrato contrato, @PathVariable Integer id) {
        try {
            
            response = new HashMap<>();
            Contrato contratoDB = contratoService.obtener(id).orElse(null);
            Cliente clienteBD = clienteService.obtener(contrato.getCliente().getId()).orElse(null);
            if (contratoDB == null) {
                response.put("data", "No se encontro contrato");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
            if (contratoDB.getFechaInicio() == null) {
                response.put("data", "Error fecha inicio null");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
            if (contratoDB.getFechaFin() == null) {
                response.put("data", "Error fecha fin null");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
            if (contratoDB.getInmueble() == null) {
                response.put("data", "Error inmueble null");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            
            contratoDB.setCliente(clienteBD);
            contratoDB.setFechaInicio(contrato.getFechaInicio());
            contratoDB.setFechaFin(contrato.getFechaFin());
            contratoDB.setInmueble(contrato.getInmueble());
            contratoDB.generarPagos();
            response.put("data", contratoService.guardar(contratoDB));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("alquiler", contrato);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
