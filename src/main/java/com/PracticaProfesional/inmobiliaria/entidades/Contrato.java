/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.entidades;

import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoContrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 *
 */
@ToString
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

    @Column(name = "estado")
    private EnumEstadoContrato estado;

    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "contratos"})
    //@JsonBackReference(value = "cliente-contratos")
    @JsonIgnoreProperties({"contratos", "contratos"})
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    //@JsonBackReference(value = "inmueble-contratos")
    @JsonIgnoreProperties({"contratos"})
    @ManyToOne
    @JoinColumn(name = "id_inmueble")
    private Inmueble inmueble;

    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "contratos"})
    @JsonBackReference(value = "agente-contratos")
    @ManyToOne
    @JoinColumn(name = "id_agente")
    private Usuario agente;

    @JsonManagedReference(value = "contrato-pagos")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contrato", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Pago> pagos = new ArrayList<>();

    @JsonManagedReference(value = "contrato-notificaciones")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contrato", orphanRemoval = true)
    private List<Notificacion> notificaciones = new ArrayList<>();

    public void agregarPago(Pago pago) {
        pagos.add(pago);
        pago.setContrato(this);
    }

}
