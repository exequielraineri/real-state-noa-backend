/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Pago;
import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoContrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.ContratoServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import com.PracticaProfesional.inmobiliaria.servicios.PagosServicios;
import com.PracticaProfesional.inmobiliaria.servicios.UsuarioServicios;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.text.html.HTML;
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
@RequestMapping("ventas")
public class VentaControlador {

    Map<String, Object> response;

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
    public ResponseEntity<Map<String, Object>> venta() {
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
    public ResponseEntity<Map<String, Object>> nuevaVenta(@RequestBody Contrato contrato) {
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
            //Si la fecha de inicio es igual a la del contrato, el contrato se pone en estado activo
            if (sf.format(contrato.getFechaContrato()).equals(sf.format(contrato.getFechaInicio()))) {
                contrato.setEstado(EnumEstadoContrato.ACTIVO);
            } else {
                contrato.setEstado(EnumEstadoContrato.PENDIENTE);
            }
            contrato.setTipoContrato(EnumTipoContrato.VENTA);
            contrato.setInmueble(inmueble);
            contrato.setAgente(agente);
            contrato.setCliente(cliente);

            if (contrato.getInmueble() == null) {
                Contrato contratoDB = contratoService.guardar(contrato);
                response.put("data", contratoDB);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                response.put("data", "inmueble no disponible");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            response.put("error", "Error al procesar la venta");
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
            contratoDB.setInmueble(contrato.getInmueble());
            contratoDB.setCliente(contrato.getCliente());

            response.put("data", contratoService.guardar(contratoDB));
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*
    private void inicializarContrato(Contrato contrato, Cliente cliente, Usuario usuario, Inmueble inmueble) {
        contrato.setCliente(cliente);
        contrato.setAgente(usuario);
        contrato.setInmueble(inmueble);
        contrato.setImporte(inmueble.getPrecioVenta());
        contrato.setTipoContrato(EnumTipoContrato.VENTA);
        contrato.setFechaContrato(new Date());
    }

    private void procesarVenta(Contrato contrato, Pago pago, Inmueble inmueble) {
        contrato.setImporte(inmueble.getPrecioVenta());
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
        BigDecimal montoTotal = inmueble.getPrecioVenta();
        BigDecimal montoCuota = montoTotal.divide(BigDecimal.valueOf(numeroCuotas), RoundingMode.HALF_UP);

        for (int i = 1; i <= numeroCuotas; i++) {
            Pago pagoCuota = new Pago();
            pagoCuota.setMetodoPago(pago.getMetodoPago());
            pagoCuota.setFechaPago(new Date());
            pagoCuota.setNumCuota(i);
            pagoCuota.setMonto(montoCuota);
            pagoCuota.setEstado("Pendiente");
            pagoCuota.setContrato(contrato);
            pagoService.guardar(pagoCuota);
        }
    }

    private void actualizarDatos(Contrato viejo, Contrato nuevo) {
        viejo.setTipoContrato(nuevo.getTipoContrato());
        viejo.setFechaContrato(nuevo.getFechaContrato());
        viejo.setFechaFin(nuevo.getFechaFin());
        viejo.setFechaInicio(nuevo.getFechaInicio());
        viejo.setAgente(nuevo.getAgente());
        viejo.setCliente(nuevo.getCliente());
        viejo.setInmueble(nuevo.getInmueble());
        viejo.setImporte(nuevo.getImporte());
    }
     */

}
