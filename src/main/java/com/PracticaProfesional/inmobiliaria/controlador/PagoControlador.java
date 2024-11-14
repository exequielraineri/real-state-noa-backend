/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.controlador;

import com.PracticaProfesional.inmobiliaria.entidades.Pago;
import com.PracticaProfesional.inmobiliaria.entidades.Transaccion;
import com.PracticaProfesional.inmobiliaria.servicios.PagosServicios;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${ruta.pdf}")
    private String RUTA_PDF;

    @GetMapping
    public ResponseEntity<Map<String, Object>> pagos(
            @RequestParam(name = "fechaDesde", required = false) Date fechaDesde,
            @RequestParam(name = "fechaHasta", required = false) Date fechaHasta,
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

            Date fechaActual = new Date();

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
            generarTicketPago(pagoBD);
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

    private void generarTicketPago(Pago pagoBD) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            File directory = new File(RUTA_PDF);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            int cuota = 0;
            for (Pago pago : pagoBD.getContrato().getPagos()) {
                cuota++;
                if (pago.getEstado().equals("PENDIENTE")) {
                    break;
                }
            }

            String pathArchivo = directory + File.separator + "ticket-pago-" + pagoBD.getId() + ".pdf";

            Document document = new Document(new Rectangle(204, 350));
            PdfWriter.getInstance(document, new FileOutputStream(pathArchivo));

            document.open();

            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fontCuerpo = new Font(Font.FontFamily.HELVETICA, 8);

            Paragraph encabezado = new Paragraph("REAL STATE | NOA", fontTitulo);
            encabezado.setAlignment(encabezado.ALIGN_CENTER);
            document.add(encabezado);

            Paragraph fechaPago = new Paragraph(sf.format(new Date()), fontCuerpo);
            fechaPago.setAlignment(fechaPago.ALIGN_CENTER);
            document.add(fechaPago);

            Paragraph nroPago = new Paragraph("ID: "+pagoBD.getId().toString(), fontCuerpo);
            nroPago.setAlignment(nroPago.ALIGN_LEFT);
            document.add(nroPago);

            Paragraph cliente = new Paragraph("Cliente: " + pagoBD.getContrato().getCliente().getNombre() + " " + pagoBD.getContrato().getCliente().getApellido(), fontCuerpo);
            cliente.setAlignment(cliente.ALIGN_LEFT);
            document.add(cliente);

            Paragraph agente = new Paragraph("Agente: " + pagoBD.getContrato().getAgente().getNombre() + " " + pagoBD.getContrato().getAgente().getApellido(), fontCuerpo);
            agente.setAlignment(agente.ALIGN_LEFT);
            document.add(agente);

            Paragraph contrato = new Paragraph("Contrato: " + pagoBD.getContrato().getId().toString(), fontCuerpo);
            contrato.setAlignment(contrato.ALIGN_LEFT);
            document.add(contrato);

            Paragraph numCuato = new Paragraph("Cuota: " + String.valueOf(cuota - 1) + " de " + pagoBD.getContrato().getPagos().size(), fontCuerpo);
            numCuato.setAlignment(numCuato.ALIGN_LEFT);
            document.add(numCuato);

            Paragraph monto = new Paragraph("Monto: $" + pagoBD.getMonto(), fontCuerpo);
            monto.setAlignment(monto.ALIGN_LEFT);
            document.add(monto);

            Paragraph metodoPago = new Paragraph("Metodo Pago: " + pagoBD.getMetodoPago(), fontCuerpo);
            metodoPago.setAlignment(metodoPago.ALIGN_LEFT);
            document.add(metodoPago);

            document.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
