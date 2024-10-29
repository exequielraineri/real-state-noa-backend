/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.entidades;

import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoContrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumFrecuenciaPago;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
    @Temporal(TemporalType.DATE)
    private Date fechaContrato;

    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;

    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;

    @Column(name = "importe", nullable = false)
    private BigDecimal importe;

    @Enumerated(EnumType.STRING)
    private EnumFrecuenciaPago frecuenciaPago;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumEstadoContrato estado;

    @JsonIgnoreProperties(value = {"contratos"}, allowSetters = true)
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @JsonIgnoreProperties(value = {"contratos"}, allowSetters = true)
    @ManyToOne
    @JoinColumn(name = "id_inmueble")
    private Inmueble inmueble;

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

        calcularImporte();

        if (getDias() <= 30) {
            this.frecuenciaPago = EnumFrecuenciaPago.UNICO_PAGO;
            generarPagoUnico(fechaPago);
        } else if (getDias() > 30 && frecuenciaPago == EnumFrecuenciaPago.UNICO_PAGO) {
            this.frecuenciaPago = EnumFrecuenciaPago.UNICO_PAGO;
            generarPagoUnico(fechaPago);
        } else {
            this.frecuenciaPago = EnumFrecuenciaPago.MENSUAL;
            generarPagosMensuales(fechaPago);
        }

    }

    private void calcularImporte() {
        if (inmueble != null && inmueble.getPrecioAlquiler() != null) {
            this.importe = inmueble.getPrecioAlquiler().multiply(BigDecimal.valueOf(getDias()));
        } else {
            this.importe = BigDecimal.ZERO;
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
        if (getSaldoRestante().doubleValue() <= 0 && (fechaFin.after(fechaActual) || fechaFin.equals(fechaActual))) {
            this.estado = EnumEstadoContrato.FINALIZADO;
        } else if (getSaldoRestante().doubleValue() > 0 && (fechaInicio.after(fechaActual) || fechaInicio.equals(fechaActual))) {
            this.estado = EnumEstadoContrato.ACTIVO;
        }
    }

    public BigDecimal getSaldoRestante() {
        Optional<BigDecimal> totalPagado = pagos.stream()
                .filter((pago) -> pago.getEstado().equals("CONFIRMADO"))
                .map((pago) -> pago.getMonto())
                .reduce(BigDecimal::add);

        return importe.subtract(totalPagado.orElse(BigDecimal.ZERO));
    }

    private void generarPagoUnico(Calendar fechaPago) {
        Pago pago = new Pago();
        pago.setEstado("PENDIENTE");
        pago.setFechaPago(fechaPago.getTime());
        pago.setMonto(getImporte());
        agregarPago(pago);
    }

    private void generarPagosMensuales(Calendar fechaPago) {
        int cantPagos = 0;
        while (fechaPago.getTime().before(fechaFin) || fechaPago.getTime().equals(fechaFin)) {
            cantPagos++;
            Pago pago = new Pago();
            pago.setEstado("PENDIENTE");
            pago.setFechaPago(fechaPago.getTime());

            agregarPago(pago);

            fechaPago.add(Calendar.MONTH, 1);
        }
        cantPagos = cantPagos == 0 ? 1 : cantPagos;

        for (Pago pago : pagos) {
            pago.setMonto(getImporte().divide(BigDecimal.valueOf(cantPagos), RoundingMode.HALF_UP));
        }
    }

}
