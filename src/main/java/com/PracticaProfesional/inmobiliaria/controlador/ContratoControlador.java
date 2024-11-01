/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.Transaccion;
import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoContrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoInmueble;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.ContratoServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import com.PracticaProfesional.inmobiliaria.servicios.UsuarioServicios;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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

    //SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    @GetMapping
    public ResponseEntity<Map<String, Object>> contrato() {
        try {
            response = new HashMap<>();
            response.put("data", contratoService.listar());
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
    public ResponseEntity<Map<String, Object>> nuevoContrato(@RequestBody Contrato contrato,
            @RequestParam(required = false) String metodoPago) {
        try {
            response = new HashMap<>();

            Cliente cliente = clienteService.obtener(contrato.getCliente().getId()).orElse(null);
            Usuario agente = usuarioService.obtener(contrato.getAgente().getId()).orElse(null);
            Inmueble inmueble = inmuebleService.obtener(contrato.getInmueble().getId()).orElse(null);

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

            if (contrato.getTipoContrato() == EnumTipoContrato.VENTA) {
                contrato.setTipoContrato(EnumTipoContrato.VENTA);
                contrato.generarPagoVentas(metodoPago);
            } else {
                contrato.setTipoContrato(EnumTipoContrato.ALQUILER);
                inmueble.setEstado(EnumEstadoInmueble.ALQUILADO);
                contrato.setEstado(EnumEstadoContrato.PENDIENTE);
                contrato.generarPagos();
            }

            contrato.setInmueble(inmueble);
            contrato.setAgente(agente);
            contrato.setCliente(cliente);

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

    @PutMapping("{id}")
    public ResponseEntity<Map<String, Object>> modificar(@RequestBody Contrato contrato,
            @PathVariable Integer id,
            @RequestParam(required = false) String metodoPago) {
        try {
            response = new HashMap<>();
            Contrato contratoDB = contratoService.obtener(id).orElse(null);
            Cliente clienteBD = clienteService.obtener(contrato.getCliente().getId()).orElse(null);

            if (contratoDB == null) {
                response.put("data", "No se encontro contrato");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            if (contratoDB.getTipoContrato() == EnumTipoContrato.VENTA) {
                contratoDB.generarPagoVentas(metodoPago);
            } else {
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

                contratoDB.generarPagos();
                contratoDB.setFechaInicio(contrato.getFechaInicio());
                contratoDB.setFechaFin(contrato.getFechaFin());

            }
            contratoDB.setInmueble(contrato.getInmueble());
            contratoDB.setCliente(clienteBD);
            response.put("data", contratoService.guardar(contratoDB));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("contrato", contrato);
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}