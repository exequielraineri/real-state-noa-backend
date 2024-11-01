/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.entidades;

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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consulta")
public class Consulta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    @Column(name = "fecha_respuesta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRespuesta;

    @Column(name = "estado")
    private String estado;

    private boolean activo;
    //@JsonBackReference(value = "cliente-consultas")
    @JsonIgnoreProperties({"consultas"})
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    //@JsonBackReference(value = "inmueble-consultas")
    @JsonIgnoreProperties({"consultas"})
    @ManyToOne
    @JoinColumn(name = "id_inmueble")
    private Inmueble inmueble;

    //@JsonBackReference(value = "agente-consultas")
    @JsonIgnoreProperties({"consultas"})
    @ManyToOne
    @JoinColumn(name = "id_agente")
    private Usuario agente;

}
