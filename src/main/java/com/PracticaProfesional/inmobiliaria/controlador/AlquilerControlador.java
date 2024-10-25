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
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoInmuebles;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.ContratoServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import com.PracticaProfesional.inmobiliaria.servicios.PagosServicios;
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
import java.util.stream.Collectors;
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
    private PagosServicios pagoService;

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
            System.out.println(contrato.toString());
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

            //Si la fecha de inicio es igual a la del contrato, el contrato se pone en estado activo
            if (sf.format(contrato.getFechaContrato()).equals(sf.format(contrato.getFechaInicio()))) {
                contrato.setEstado(EnumEstadoContrato.ACTIVO);
            } else {
                contrato.setEstado(EnumEstadoContrato.PENDIENTE);
            }
            contrato.setTipoContrato(EnumTipoContrato.ALQUILER);

            contrato.setInmueble(inmueble);
            contrato.setAgente(agente);
            contrato.setCliente(cliente);

            Contrato contratoDB = contratoService.guardar(contrato);
            response.put("data", contratoDB);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("error", "Error al procesar el Alquiler");
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
    public ResponseEntity<Map<String, Object>> modificar(@RequestBody Contrato contrato, @PathVariable Integer id) {
        try {
            response = new HashMap<>();
            Contrato contratoDB = contratoService.obtener(id).orElse(null);
            if (contratoDB == null) {
                response.put("data", "No se encontro contrato");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            contratoDB.setFechaInicio(contrato.getFechaInicio());
            contratoDB.setFechaFin(contrato.getFechaFin());
            contratoDB.setCliente(contrato.getCliente());
            contratoDB.setInmueble(contrato.getInmueble());

            response.put("data", contratoService.guardar(contratoDB));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void procesarAlquiler(Contrato contrato, Pago pago, Inmueble inmueble) {
        contrato.setImporte(inmueble.getPrecioAlquiler());
        String metodoPago = pago.getMetodoPago();

        if ("efectivo".equals(metodoPago)) {
            procesarPagoEfectivo(pago, contrato);
        } else if ("cuotas".equals(metodoPago)) {
            int numeroCuotas = pago.getNumCuota();
            procesarPagoCuotas(contrato, pago, inmueble, numeroCuotas);
        }
    }

    private void procesarPagoEfectivo(Pago pago, Contrato contrato) {
        pago.setMetodoPago("efectivo");
        pago.setFechaPago(new Date());
        pago.setEstado("Pagado");
        pago.setContrato(contrato);
        pagoService.guardar(pago);
        List<Pago> pagos = new ArrayList<>();
        pagos.add(pago);
        contrato.setPagos(pagos);
    }

    private void procesarPagoCuotas(Contrato contrato, Pago pago, Inmueble inmueble, int numeroCuotas) {
        BigDecimal montoTotal = inmueble.getPrecioAlquiler();
        BigDecimal montoCuota = montoTotal.divide(BigDecimal.valueOf(numeroCuotas), RoundingMode.HALF_UP);
        LocalDate fechaInicio = contrato.getFechaInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaUltimoPago = fechaInicio;

        for (int i = 1; i <= numeroCuotas; i++) {
            Pago pagoCuota = new Pago();
            pagoCuota.setMetodoPago(pago.getMetodoPago());
            pagoCuota.setNumCuota(i);
            pagoCuota.setEstado("Pendiente");
            pagoCuota.setContrato(contrato);

            fechaUltimoPago = fechaInicio.plusMonths(i - 1);
            LocalDate fechaPagoCuota = fechaUltimoPago.withDayOfMonth(1);
            pagoCuota.setFechaPago(Date.from(fechaPagoCuota.atStartOfDay(ZoneId.systemDefault()).toInstant()));

            Date fechaPagoEfectiva = pago.getFechaPago();

            LocalDate fechaLimite = fechaUltimoPago.withDayOfMonth(10);
            LocalDate fechaEfectivaPago = fechaPagoEfectiva.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (fechaEfectivaPago.isAfter(fechaLimite)) {
                BigDecimal recargo = montoCuota.multiply(BigDecimal.valueOf(0.10));
                montoCuota = montoCuota.add(recargo);
            }

            pagoCuota.setMonto(montoCuota);

            pagoService.guardar(pagoCuota);
        }

        contrato.setFechaFin(Date.from(fechaUltimoPago.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

}
