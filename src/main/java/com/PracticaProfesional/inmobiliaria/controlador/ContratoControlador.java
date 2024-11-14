/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoContrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoInmueble;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoCliente;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.ContratoServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import com.PracticaProfesional.inmobiliaria.servicios.UsuarioServicios;
import java.time.LocalDateTime;
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
@RequestMapping("contratos")
public class ContratoControlador {
    
    Map<String, Object> response;
    
    @Autowired
    private ContratoServicios contratoService;
    @Autowired
    private ClienteServicios clienteService;
    @Autowired
    private UsuarioServicios usuarioService;
    @Autowired
    private InmuebleServicios inmuebleService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> listar(
            @RequestParam(required = false, name = "estado") EnumEstadoContrato estado,
            @RequestParam(required = false, name = "fechaDesde") LocalDateTime fechaDesde,
            @RequestParam(required = false, name = "fechaHasta") LocalDateTime fechaHasta,
            @RequestParam(required = false, name = "cliente") Integer cliente,
            @RequestParam(required = false, name = "tipoContrato") EnumTipoContrato tipoContrato,
            @RequestParam(required = false, name = "activo", defaultValue = "true") boolean activo
    ) {
        try {
            System.out.println(fechaDesde);
            System.out.println(fechaHasta);
            response = new HashMap<>();
            response.put("data", contratoService.listarFiltrados(activo, estado, fechaDesde, fechaHasta, cliente, tipoContrato));
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
    public ResponseEntity<Map<String, Object>> nuevoContrato(@RequestBody Contrato contrato) {
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
            contrato.setFechaContrato(LocalDateTime.now());
            contrato.setInmueble(inmueble);
            contrato.setAgente(agente);
            contrato.setCliente(cliente);
            if (contrato.getTipoContrato().equals(EnumTipoContrato.VENTA)) {
                contrato.setTipoContrato(EnumTipoContrato.VENTA);
                inmueble.setEstado(EnumEstadoInmueble.VENDIDO);
                contrato.setTipoCliente(EnumTipoCliente.COMPRADOR);
                contrato.generarPagosVentas();
            } else {
                contrato.setTipoContrato(EnumTipoContrato.ALQUILER);
                inmueble.setEstado(EnumEstadoInmueble.ALQUILADO);
                contrato.setTipoCliente(EnumTipoCliente.INQUILINO);
                contrato.generarPagos();
            }
            
            contrato.setEstado(EnumEstadoContrato.PENDIENTE);
            response.put("data", contratoService.guardar(contrato));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
            
        } catch (NumberFormatException e) {
            response.put("error", "Error al procesar la transaccion");
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
    
}
