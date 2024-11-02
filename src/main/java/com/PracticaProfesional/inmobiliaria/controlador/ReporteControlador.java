/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.Transaccion;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import com.PracticaProfesional.inmobiliaria.servicios.ClienteServicios;
import com.PracticaProfesional.inmobiliaria.servicios.ContratoServicios;
import com.PracticaProfesional.inmobiliaria.servicios.InmuebleServicios;
import com.PracticaProfesional.inmobiliaria.servicios.TransaccionServicios;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    Map<String, Object> response, inmueble, transaccion, cliente;

    @GetMapping
    public ResponseEntity<Map<String, Object>> contrato() {
        try {
            response = new HashMap<>();
            inmueble = new HashMap<>();
            transaccion = new HashMap<>();
            cliente = new HashMap<>();

            long cantidadInmuebles = inmuebleServicios.cantidad();

            BigDecimal entradaVenta = BigDecimal.ZERO;
            BigDecimal entradaAlquiler = BigDecimal.ZERO;
            BigDecimal impuestosInmobiliarios = BigDecimal.ZERO;
            BigDecimal impuestosMunicipales = BigDecimal.ZERO;
            BigDecimal impuestosTotales;
            BigDecimal ingresoTotales = BigDecimal.ZERO;

            int cantidadPropietario = 0;
            int cantidadInquilino = 0;
            int cantidadComprador = 0;
            int cantidadCasas = 0;
            int cantidadDeptos = 0;
            int cantidadOficinas = 0;
            int cantidadCampos = 0;
            int cantidadIngresos = 0;

            List<Contrato> contratos = contratoServicios.listar();
            List<Inmueble> inmuebles = inmuebleServicios.listar();
            List<Transaccion> transacciones = transaccionServicios.listar();
            List<Cliente> clientes = clienteServicios.listar();

            for (Contrato contrato : contratos) {
                if (contrato.getTipoContrato() == EnumTipoContrato.VENTA) {
                    entradaVenta = entradaVenta.add(contrato.getImporte());
                } else if (contrato.getTipoContrato() == EnumTipoContrato.ALQUILER) {
                    entradaAlquiler = entradaAlquiler.add(contrato.getImporte());
                }
            }
            for (Inmueble inmuebleFiltro : inmuebles) {
                impuestosInmobiliarios = impuestosInmobiliarios.add(inmuebleFiltro.getImpInmobiliarios());
                impuestosMunicipales = impuestosMunicipales.add(inmuebleFiltro.getImpMunicipales());
                switch (inmuebleFiltro.getTipoInmueble()) {
                    case CASA:
                        cantidadCasas++;
                    case DEPARTAMENTO:
                        cantidadDeptos++;
                    case OFICINA:
                        cantidadOficinas++;
                    case CAMPO:
                        cantidadCampos++;
                }
            }
            impuestosTotales = impuestosInmobiliarios.add(impuestosMunicipales);

            inmueble.put("Casas", cantidadCasas);
            inmueble.put("Departamentos", cantidadDeptos);
            inmueble.put("Oficinas", cantidadOficinas);
            inmueble.put("Campo", cantidadCampos);
            inmueble.put("impuestos inmobiliarios", impuestosInmobiliarios);
            inmueble.put("inmpuestos Municipales", impuestosMunicipales);
            inmueble.put("impuestos totales", impuestosTotales);
            inmueble.put("entrada de Ventas", entradaVenta);
            inmueble.put("entrada de Alquiler", entradaAlquiler);
            inmueble.put("cantidad de inmueble", cantidadInmuebles);
            response.put("Inmuebles", inmueble);

            for (Transaccion transaccionFiltro : transacciones) {
                if (transaccionFiltro.getTipoTransaccion().equals("INGRESO")) {
                    cantidadIngresos++;
                    ingresoTotales = ingresoTotales.add(transaccionFiltro.getImporte());
                }
            }
            transaccion.put("Cantidad de ingreso", cantidadIngresos);
            transaccion.put("ingresos Totales", ingresoTotales);
            response.put("transacciones", transaccion);

            for (Cliente clienteFiltro : clientes) {
                switch (clienteFiltro.getTipoCliente()) {
                    case COMPRADOR:
                        cantidadComprador++;
                    case INQUILINO:
                        cantidadInquilino++;
                    case PROPIETARIO:
                        cantidadPropietario++;

                }
            }
            cliente.put("cantidad de Propietarios", cantidadPropietario);
            cliente.put("cantidad de Inquilinos", cantidadInquilino);
            cliente.put("cantidad de Comprador", cantidadComprador);
            response.put("cliente", cliente);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
