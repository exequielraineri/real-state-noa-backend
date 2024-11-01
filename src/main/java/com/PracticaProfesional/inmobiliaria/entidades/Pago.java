/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "pagos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "fecha_pago")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaPago;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @Column(name = "monto")
    private BigDecimal monto;

    @Column(name = "interes")
    private BigDecimal interes;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    private boolean activo;
    
    //@JsonBackReference(value = "contrato-pagos")
    @JsonIgnoreProperties(value = {"pagos"}, allowSetters = true)
    @ManyToOne
    @JoinColumn(name = "id_contrato")
    private Contrato contrato;

    public void confirmarPago() {
        this.estado = "PAGADO";
        this.contrato.actualizarSaldo();
    }

}
