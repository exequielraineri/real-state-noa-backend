/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Pago;
import com.PracticaProfesional.inmobiliaria.entidades.Transaccion;
import com.PracticaProfesional.inmobiliaria.servicios.PagosServicios;
import jakarta.validation.Valid;
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
@RequestMapping("pagos")
public class PagoControlador {

    private Map<String, Object> response;

    @Autowired
    private PagosServicios pagoService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> pagos(
            @RequestParam(name = "fechaDesde", required = false) LocalDateTime fechaDesde,
            @RequestParam(name = "fechaHasta", required = false) LocalDateTime fechaHasta,
            @RequestParam(name = "estado", required = false) String estado) {
        try {
            response = new HashMap<>();
            response.put("data", pagoService.listarFiltro(fechaDesde, fechaHasta, estado));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Map<String, Object>> obtenerPago(@PathVariable Integer id) {
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

    @PutMapping("/confirmar/{id}")
    public ResponseEntity<Map<String, Object>> confirmarPago(@PathVariable(required = true) Integer id,
            @RequestBody @Valid Pago pago) {
        try {
            response = new HashMap<>();
            Pago pagoBD = pagoService.obtener(id).orElse(null);
            if (pagoBD == null) {
                response.put("data", "No se encontro el contrato");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            if (pagoBD.getEstado().equals("PAGADO")) {
                response.put("data", "El pago ya se encuentra realizado");
                response.put("pago", pagoBD);
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            LocalDateTime fechaActual = LocalDateTime.now();

            //Genero una transaccion del pago confirmado
            Transaccion transaccion = new Transaccion();
            transaccion.setAgente(pagoBD.getContrato().getAgente());
            transaccion.setFechaTransaccion(fechaActual);
            transaccion.setEstado(true);
            transaccion.setImporte(pagoBD.getMonto());
            transaccion.setTipoOperacion(pagoBD.getContrato().getTipoContrato().name());
            transaccion.setTipoTransaccion("INGRESO");
            transaccion.setDescripcion(String.format("Registro de pago, contrato %d, inmueble %s", pagoBD.getContrato().getId(), pagoBD.getContrato().getInmueble().getTitulo()));

            pagoBD.setMetodoPago(pago.getMetodoPago());
            pagoBD.setFechaRegistro(fechaActual);
            pagoBD.confirmarPago();
            pagoBD.getContrato().getAgente().agregarTransaccion(transaccion);

            response.put("data", pagoService.guardar(pagoBD));
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

}
