/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

/**
 *
 *
 */
@Entity
@Table(name = "pagos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pagos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha_pago")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;
    @Column(name = "metodo_pago")
    private String metodoPago;
    @Column(name = "num_cuota")
    private Integer numCuota;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "monto")
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal monto;
    @Column(name = "interes")
    private BigDecimal interes;
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id")
    @ManyToOne
    private Contrato idContrato;

}
