/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.entidades;

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

/**
 *
 * 
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contrato")
public class Contrato implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "tipo_operacion")
    private String tipoOperacion;
    @Column(name = "tipo_cliente")
    private String tipoCliente;
    @Column(name = "fecha_contrato")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaContrato;
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    @Column(name = "cant_cuota")
    private Integer cantCuota;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "importe")
    private BigDecimal importe;
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    @ManyToOne
    private Cliente idCliente;
    @JoinColumn(name = "id_inmueble", referencedColumnName = "id")
    @ManyToOne
    private Inmueble idInmueble;
    @JoinColumn(name = "id_agente", referencedColumnName = "id")
    @ManyToOne
    private Usuario idAgente;
    @OneToMany(mappedBy = "idContrato")
    private List<Pagos> pagosCollection=new ArrayList<>();
    @OneToMany(mappedBy = "idContrato")
    private List<Notificacion> notificacionCollection=new ArrayList<>();

}
