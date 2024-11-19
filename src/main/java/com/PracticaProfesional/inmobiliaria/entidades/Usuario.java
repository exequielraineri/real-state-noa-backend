/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Sofia
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "El rol no puede estar vacio")
    @Column(name = "rol", nullable = false)
    private String rol;

    @NotNull(message = "El nombre no puede estar vacio")
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull(message = "El apellido no puede estar vacio")
    @Column(name = "apellido", nullable = false)
    private String apellido;

    @NotNull(message = "El correo no puede estar vacio")
    @Column(name = "correo", unique = true, nullable = false)
    private String correo;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(unique = true)
    private String telefono;

    @NotNull(message = "Password no puede estar vacio")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull(message = "La fecha de registro no puede estar vacio")
    @Column(name = "fecha_registro", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaRegistro;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "comision_venta", nullable = false)
    private BigDecimal comisionVenta;

    @Column(name = "comision_alquiler", nullable = false)
    private BigDecimal comisionAlquiler;

    //@JsonManagedReference(value = "agente-transacciones")
    @JsonIgnoreProperties(value = {"agente"}, allowSetters = true)
    @OneToMany(mappedBy = "agente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Transaccion> transacciones = new ArrayList<>();

    //@JsonManagedReference(value = "agente-contratos")
    @JsonIgnoreProperties(value = {"agente"}, allowSetters = true)
    @OneToMany(mappedBy = "agente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contrato> contratos = new ArrayList<>();

    //@JsonManagedReference(value = "agente-consultas")
    @JsonIgnore
    @JsonIgnoreProperties(value = {"agente"}, allowSetters = true)
    @OneToMany(mappedBy = "agente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Consulta> consultas = new ArrayList<>();

    public void agregarTransaccion(Transaccion transaccion) {
        transacciones.add(transaccion);
        transaccion.setAgente(this);
    }

    public void agregarContrato(Contrato contrato) {
        contratos.add(contrato);
        contrato.setAgente(this);
    }

    public void agregarConsulta(Consulta consulta) {
        consultas.add(consulta);
        consulta.setAgente(this);
    }

    private boolean activo;

    private BigDecimal totalGanancias = BigDecimal.ZERO;
}
