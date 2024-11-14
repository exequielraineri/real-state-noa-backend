/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.entidades;

import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoContrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumFrecuenciaPago;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoCliente;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.*;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contrato")
public class Contrato implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tipo_contrato", nullable = false)
    private EnumTipoContrato tipoContrato;

    @Column(name = "fecha_contrato", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaContrato;

    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;

    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;

    @Column(name = "importe", nullable = false)
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    private EnumFrecuenciaPago frecuenciaPago;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumEstadoContrato estado;

    @JsonIgnoreProperties(value = {"contratos", "inmuebles"}, allowSetters = true)
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @JsonIgnoreProperties(value = {"contratos"}, allowSetters = true)
    @ManyToOne
    @JoinColumn(name = "id_inmueble")
    private Inmueble inmueble;

    @Column(name = "tipo_cliente")
    @Enumerated(EnumType.STRING)
    private EnumTipoCliente tipoCliente;

    //@JsonBackReference(value = "agente-contratos")
    @JsonIgnoreProperties(value = {"contratos"}, allowSetters = true)
    @ManyToOne
    @JoinColumn(name = "id_agente")
    private Usuario agente;

    //@JsonManagedReference(value = "contrato-pagos")
    @JsonIgnoreProperties(value = {"contrato"}, allowSetters = true)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contrato", fetch = FetchType.LAZY)
    private List<Pago> pagos = new ArrayList<>();

    //@JsonManagedReference(value = "contrato-notificaciones")
    @JsonIgnoreProperties(value = {"contrato"}, allowSetters = true)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contrato", orphanRemoval = true)
    private List<Notificacion> notificaciones = new ArrayList<>();

    //controlar las bajas logicas
    private boolean activo;
    private BigDecimal entrega;
    private int cuotas;

    public void agregarPago(Pago pago) {
        pagos.add(pago);
        pago.setContrato(this);
    }

    public void agregarNotificacion(Notificacion notificacion) {
        notificaciones.add(notificacion);
        notificacion.setContrato(this);
    }

    public int getCantidadPagos() {
        return pagos.size();
    }

    public void generarPagos() {
        pagos.clear();
        Calendar fechaPago = Calendar.getInstance();
        fechaPago.setTime(fechaInicio);
        setearImporte();
        if (getDias() <= 30) {
            this.frecuenciaPago = EnumFrecuenciaPago.UNICO_PAGO;
            //precio alquiler por dia
            generarPagoUnico(fechaPago);
        } else if (getDias() > 30 && frecuenciaPago == EnumFrecuenciaPago.UNICO_PAGO) {
            //precio alquiler por mes 
            this.frecuenciaPago = EnumFrecuenciaPago.UNICO_PAGO;
            generarPagoUnico(fechaPago);
        } else {
            //precio alquiler por mes
            this.frecuenciaPago = EnumFrecuenciaPago.MENSUAL;
            generarPagosMensuales(fechaPago);
        }

    }

    public int getDias() {
        if (fechaInicio != null && fechaFin != null) {
            long tiempoTranscurrido = fechaFin.getTime() - fechaInicio.getTime();
            return (int) TimeUnit.DAYS.convert(tiempoTranscurrido, TimeUnit.MILLISECONDS);
        }
        return 1;
    }

    public void actualizarSaldo() {
        Date fechaActual = new Date();
        if (getTipoContrato().equals(EnumTipoContrato.ALQUILER)) {
            if (getSaldoRestante().doubleValue() <= 0 && (fechaFin.after(fechaActual) || fechaFin.equals(fechaActual))) {
                this.estado = EnumEstadoContrato.FINALIZADO;
            } else if (getSaldoRestante().doubleValue() > 0 && (fechaInicio.after(fechaActual) || fechaInicio.equals(fechaActual))) {
                this.estado = EnumEstadoContrato.ACTIVO;
            }
        } else {
            if (getSaldoRestante().doubleValue() <= 0) {
                this.estado = EnumEstadoContrato.FINALIZADO;
            } else {
                this.estado = EnumEstadoContrato.ACTIVO;
            }
        }
    }

    public BigDecimal getSaldoRestante() {
        Optional<BigDecimal> totalPagado = pagos.stream()
                .filter((pago) -> pago.getEstado().equals("PAGADO"))
                .map((pago) -> pago.getMonto())
                .reduce(BigDecimal::add);

        return importe.subtract(totalPagado.orElse(BigDecimal.ZERO));
    }

    private void generarPagoUnico(Calendar fechaPago) {
        Pago pago = new Pago();
        pago.setEstado("PENDIENTE");
        pago.setFechaPago(fechaPago.getTime());
        pago.setMonto(getImporte());
        pago.setActivo(true);
        agregarPago(pago);
    }

    private void generarPagosMensuales(Calendar fechaPago) {
        int cantPagos = 0;
        Pago pago = new Pago();
        pago.setFechaPago(new Date());
        pago.setActivo(true);
        pago.setEstado("PENDIENTE");
        agregarPago(pago);
        cantPagos++;
        while (fechaPago.getTime().before(fechaFin) || fechaPago.getTime().equals(fechaFin)) {

            pago = new Pago();
            pago.setEstado("PENDIENTE");
            pago.setActivo(true);
            pago.setFechaPago(fechaPago.getTime());

            agregarPago(pago);

            fechaPago.add(Calendar.MONTH, 1);
            cantPagos++;
        }
        cantPagos = cantPagos == 0 ? 1 : cantPagos;

        for (Pago pagoMonto : pagos) {
            pagoMonto.setMonto(getImporte().divide(BigDecimal.valueOf(cantPagos), RoundingMode.HALF_UP));
        }
    }

    private void generarPagoVentasCoutas() {
        Calendar fechaPago = Calendar.getInstance();
        fechaPago.setTime(fechaContrato);
        Pago pagoInicial = new Pago();
        pagoInicial.setFechaPago(fechaPago.getTime());

        BigDecimal porcentaje = getEntrega().divide(BigDecimal.valueOf(100));
        BigDecimal montoInicial = getImporte().multiply(porcentaje).setScale(2, RoundingMode.HALF_UP);
        pagoInicial.setMonto(montoInicial);
        pagoInicial.setEstado("PENDIENTE");
        agregarPago(pagoInicial);
        fechaPago.add(Calendar.MONTH, 1);

        BigDecimal montoRestante = getImporte().subtract(montoInicial);
        BigDecimal montoPorCuota = montoRestante.divide(BigDecimal.valueOf(getCuotas()), RoundingMode.HALF_UP);

        for (int i = 0; i < getCuotas(); i++) {
            Pago cuota = new Pago();
            cuota.setEstado("PENDIENTE");
            cuota.setFechaPago(fechaPago.getTime());
            cuota.setMonto(montoPorCuota);
            agregarPago(cuota);
            fechaPago.add(Calendar.MONTH, 1);
        }
    }

    public void generarPagosVentas() {
        pagos.clear();
        Calendar fechaPago = Calendar.getInstance();
        fechaPago.setTime(fechaContrato);

        setearImporte();
        if (this.frecuenciaPago == EnumFrecuenciaPago.UNICO_PAGO) {
            generarPagoUnico(fechaPago);
        } else {
            generarPagoVentasCoutas();
        }
    }

    public void setearImporte() {
        if (getTipoContrato().equals(EnumTipoContrato.VENTA)) {
            this.importe = getInmueble().getPrecioVenta();
        } else {
            if (getDias() < 30) {
                this.importe = getInmueble().getPrecioAlquilerDia().multiply(BigDecimal.valueOf(getDias()));
            } else {
                double mes = getDias() / 30;
                this.importe = getInmueble().getPrecioAlquilerMes().multiply(BigDecimal.valueOf(mes));
            }
        }
    }
}
