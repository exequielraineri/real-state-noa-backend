package com.PracticaProfesional.inmobiliaria.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "imagen")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Imagen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @JsonIgnoreProperties(value = {"imagenes"}, allowSetters = true)
    @ManyToOne
    @JoinColumn(name = "id_inmueble")
    private Inmueble inmueble;

    private boolean activo;
}
