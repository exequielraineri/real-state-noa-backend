package com.PracticaProfesional.inmobiliaria.entidades;

import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoContrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumFrecuenciaPago;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoCliente;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.*;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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
    @Enumerated(EnumType.STRING)
    private EnumTipoContrato tipoContrato;

    @Column(name = "fecha_contrato", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaContrato;

    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaFin;

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

    @JsonIgnoreProperties(value = {"contrato"}, allowSetters = true)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contrato", fetch = FetchType.LAZY)
    private List<Pago> pagos = new ArrayList<>();

  
    //controlar las bajas logicas
    private boolean activo;
    private BigDecimal entrega;
    private int cuotas;

    public void agregarPago(Pago pago) {
        pagos.add(pago);
        pago.setContrato(this);
    }

  
    public int getCantidadPagos() {
        return pagos.size();
    }

    public void generarPagos() {
        pagos.clear();
        LocalDateTime fechaPago = fechaInicio;
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
            return (int) Duration.between(fechaInicio, fechaFin).toDays();
        }
        return 1;
    }

    public void actualizarSaldo() {
        LocalDateTime fechaActual = LocalDateTime.now();
        if (getTipoContrato().equals(EnumTipoContrato.ALQUILER)) {
            if (getSaldoRestante().doubleValue() <= 0 && (fechaFin.isAfter(fechaActual) || fechaFin.equals(fechaActual))) {
                this.estado = EnumEstadoContrato.FINALIZADO;
            } else if (getSaldoRestante().doubleValue() > 0 && (fechaInicio.isAfter(fechaActual) || fechaInicio.equals(fechaActual))) {
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

    private void generarPagoUnico(LocalDateTime fechaPago) {
        Pago pago = new Pago();
        pago.setEstado("PENDIENTE");
        pago.setFechaPago(fechaPago);
        pago.setMonto(getImporte());
        pago.setActivo(true);
        agregarPago(pago);
    }

    private void generarPagosMensuales(LocalDateTime fechaPago) {
        int cantPagos = 0;
        Pago pago = new Pago();
        pago.setFechaPago(LocalDateTime.now());
        pago.setActivo(true);
        pago.setEstado("PENDIENTE");
        agregarPago(pago);
        cantPagos++;
        while (fechaPago.isBefore(fechaFin) || fechaPago.equals(fechaFin)) {
            pago = new Pago();
            pago.setEstado("PENDIENTE");
            pago.setActivo(true);
            pago.setFechaPago(fechaPago);
            agregarPago(pago);
            fechaPago = fechaPago.plusMonths(1);
            cantPagos++;
        }
        cantPagos = cantPagos == 0 ? 1 : cantPagos;
        for (Pago pagoMonto : pagos) {
            pagoMonto.setMonto(getImporte().divide(BigDecimal.valueOf(cantPagos), RoundingMode.HALF_UP));
        }
    }

    private void generarPagoVentasCoutas() {
        LocalDateTime fechaPago = fechaContrato;
        Pago pagoInicial = new Pago();
        pagoInicial.setFechaPago(fechaPago);
        BigDecimal porcentaje = getEntrega().divide(BigDecimal.valueOf(100));
        BigDecimal montoInicial = getImporte().multiply(porcentaje).setScale(2, RoundingMode.HALF_UP);
        pagoInicial.setMonto(montoInicial);
        pagoInicial.setEstado("PENDIENTE");
        agregarPago(pagoInicial);
        fechaPago = fechaPago.minusMonths(1);
        BigDecimal montoRestante = getImporte().subtract(montoInicial);
        BigDecimal montoPorCuota = montoRestante.divide(BigDecimal.valueOf(getCuotas()), RoundingMode.HALF_UP);
        for (int i = 0; i < getCuotas(); i++) {
            Pago cuota = new Pago();
            cuota.setEstado("PENDIENTE");
            cuota.setFechaPago(fechaPago);
            cuota.setMonto(montoPorCuota);
            agregarPago(cuota);
            fechaPago = fechaPago.minusMonths(1);
        }
    }

    public void generarPagosVentas() {
        pagos.clear();
        LocalDateTime fechaPago = fechaContrato;
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
