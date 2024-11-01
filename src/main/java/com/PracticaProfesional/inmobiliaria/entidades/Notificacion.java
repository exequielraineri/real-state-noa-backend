/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.entidades;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
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
@Table(name = "notificacion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "mensaje", nullable = false)
    private String mensaje;

    @Column(name = "fecha_registro")
    @Temporal(TemporalType.DATE)
    private Date fechaRegistro;

    private boolean activo;
    
    //@JsonBackReference(value = "contrato-notificaciones")
    @JsonIgnoreProperties(value = {"notificaciones"}, allowSetters = true)
    @ManyToOne
    @JoinColumn(name = "id_contrato")
    private Contrato contrato;

}
