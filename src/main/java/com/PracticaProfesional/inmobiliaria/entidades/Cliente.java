/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.entidades;

import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoCliente;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "dni", nullable = false, unique = true)
    private String dni;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    @Column(name = "provincia")
    private String provincia;

    private boolean activo;

    //@JsonManagedReference(value = "propietario-inmuebles")
    @JsonIgnoreProperties(value = {"propietario"}, allowSetters = true)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "propietario", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Inmueble> inmuebles = new ArrayList<>();

    @Column(name = "tipo_cliente")
    @Enumerated(EnumType.STRING)
    private EnumTipoCliente tipoCliente;

    //@JsonManagedReference(value = "cliente-contratos")
    @JsonIgnoreProperties(value = {"cliente"}, allowSetters = true)
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contrato> contratos = new ArrayList<>();

    //@JsonManagedReference(value = "cliente-consultas")
    @JsonIgnoreProperties(value = {"cliente"}, allowSetters = true)
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consulta> consultas = new ArrayList<>();

    public void agregarInmueble(Inmueble inmueble) {
        inmuebles.add(inmueble);
        inmueble.setPropietario(this);
    }

    public void agregarContrato(Contrato contrato) {
        contratos.add(contrato);
        contrato.setCliente(this);
    }

    public void agregarConsulta(Consulta consulta) {
        consultas.add(consulta);
        consulta.setCliente(this);
    }

}
