/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.Pagos;
import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.ContratoServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import com.PracticaProfesional.inmobiliaria.servicios.PagosServicios;
import com.PracticaProfesional.inmobiliaria.servicios.UsuarioServicios;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

/**
 *
 * @author exera
 */
@CrossOrigin("*")
@Controller
@RequestMapping("alquiler")
public class AlquilerControlador {

    Map<String, Object> response;
    @Autowired
    private ContratoServicios conService;
    @Autowired
    private PagosServicios pagoService;
    @Autowired
    private ClienteServicios cliService;
    @Autowired
    private UsuarioServicios userService;
    @Autowired
    private InmuebleServicios inmuService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> inicioAlquiler() {
        try {
            response = new HashMap<>();
            response.put("data", listarAlquiler());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> nuevoAlquiler(
            @RequestParam Integer idCliente,
            @RequestParam Integer idUsuario,
            @RequestParam Integer idContrato,
            @RequestParam Integer idInmueble,
            @RequestBody Pagos pago) throws Exception {
        try {
            Cliente cliente = cliService.obtener(idCliente).orElseThrow(() -> new Exception("Cliente no encontrado"));
            Usuario usuario = userService.obtener(idUsuario).orElseThrow(() -> new Exception("Usuario no encontrado"));
            Inmueble inmueble = inmuService.obtener(idInmueble).orElseThrow(() -> new Exception("Inmueble no encontrado"));
            Contrato contrato = conService.obtener(idContrato).orElseThrow(() -> new Exception("Contrato no encontrado"));
            inicializarContrato(contrato, cliente, usuario, inmueble);

            if ("alquiler".equalsIgnoreCase(contrato.getTipoOperacion())) {
                procesarAlquiler(contrato, pago, inmueble);
            }

            Contrato contratoDB = conService.guardar(contrato);

            response = new HashMap<>();
            response.put("data", contratoDB);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            response.put("error", "Error al procesar el Alquiler");
            response.put("mensaje", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Contrato contrato = conService.obtener(id).orElse(null);
            if (contrato == null) {
                response.put("data", "No se Encontro Contrato");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            conService.eliminar(contrato.getId());
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
            Contrato contratoDB = conService.obtener(id).orElse(null);
            if (contratoDB == null) {
                response.put("data", "No se encontro contrato");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            actualizarDatos(contratoDB, contrato);
            response.put("data", conService.guardar(contrato));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Contrato> listarAlquiler() {
        return conService.listar().stream()
                .filter(transaccion -> "alquiler".equals(transaccion.getTipoOperacion()))
                .collect(Collectors.toList());
    }

    private void inicializarContrato(Contrato contrato, Cliente cliente, Usuario usuario, Inmueble inmueble) {
        contrato.setIdCliente(cliente);
        contrato.setIdAgente(usuario);
        contrato.setIdInmueble(inmueble);
        contrato.setImporte(inmueble.getPrecioAlquiler());
        contrato.setTipoOperacion("Alquiler");
        contrato.setTipoCliente("Inquilino");
        contrato.setFechaContrato(new Date());
    }

    private void procesarAlquiler(Contrato contrato, Pagos pago, Inmueble inmueble) {
        contrato.setImporte(inmueble.getPrecioAlquiler());
        String metodoPago = pago.getMetodoPago();

        if ("efectivo".equals(metodoPago)) {
            procesarPagoEfectivo(pago, contrato);
        } else if ("cuotas".equals(metodoPago)) {
            int numeroCuotas = pago.getNumCuota();
            procesarPagoCuotas(contrato, pago, inmueble, numeroCuotas);
        }
    }

    private void procesarPagoEfectivo(Pagos pago, Contrato contrato) {
        pago.setMetodoPago("efectivo");
        pago.setFechaPago(new Date());
        pago.setEstado("Pagado");
        pago.setIdContrato(contrato);
        pagoService.guardar(pago);
        List<Pagos> pagos = new ArrayList<>();
        pagos.add(pago);
        contrato.setPagosCollection(pagos);
    }

    private void procesarPagoCuotas(Contrato contrato, Pagos pago, Inmueble inmueble, int numeroCuotas) {
        BigDecimal montoTotal = inmueble.getPrecioAlquiler();
        BigDecimal montoCuota = montoTotal.divide(BigDecimal.valueOf(numeroCuotas), RoundingMode.HALF_UP);

        for (int i = 1; i <= numeroCuotas; i++) {
            Pagos pagoCuota = new Pagos();
            pagoCuota.setMetodoPago(pago.getMetodoPago());
            pagoCuota.setFechaPago(new Date());
            pagoCuota.setNumCuota(i);
            pagoCuota.setMonto(montoCuota);
            pagoCuota.setEstado("Pendiente");
            pagoCuota.setIdContrato(contrato);
            pagoService.guardar(pagoCuota);
        }
    }

    private void actualizarDatos(Contrato viejo, Contrato nuevo) {
        viejo.setTipoOperacion(nuevo.getTipoOperacion());
        viejo.setCantCuota(nuevo.getCantCuota());
        viejo.setFechaContrato(nuevo.getFechaContrato());
        viejo.setFechaFin(nuevo.getFechaFin());
        viejo.setFechaInicio(nuevo.getFechaInicio());
        viejo.setIdAgente(nuevo.getIdAgente());
        viejo.setIdCliente(nuevo.getIdCliente());
        viejo.setIdInmueble(nuevo.getIdInmueble());
        viejo.setImporte(nuevo.getImporte());
        viejo.setTipoCliente(nuevo.getTipoCliente());
    }

}
