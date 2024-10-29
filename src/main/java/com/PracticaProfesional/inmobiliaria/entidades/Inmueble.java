package com.PracticaProfesional.inmobiliaria.entidades;

import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoInmueble;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoInmuebles;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 *
 */
@Getter
@Setter
@Data
@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inmueble")
public class Inmueble implements Serializable {

    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    private boolean activo;
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    //@JsonBackReference(value = "propietario-inmuebles")
    @JsonIgnoreProperties( value = {"inmuebles"}, allowSetters = true)
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_propietario")
    private Cliente propietario;

    @Column(name = "tipo_inmueble")
    @Enumerated(EnumType.STRING)
    private EnumTipoInmuebles tipoInmueble;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "tipo_operacion")
    private String tipoOperacion;

    @Column(name = "precio_venta")
    private BigDecimal precioVenta;

    @Column(name = "precio_alquiler")
    private BigDecimal precioAlquiler;

    @Column(name = "imp_municipales")
    private BigDecimal impMunicipales;

    @Column(name = "imp_inmobiliarios")
    private BigDecimal impInmobiliarios;

    @Column(name = "fecha_registro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EnumEstadoInmueble estado;

    @Column(name = "fecha_publicacion")
    @Temporal(TemporalType.DATE)
    private Date fechaPublicacion;

    @Column(name = "mts2")
    private BigDecimal mts2;

    @Column(name = "cant_ambientes")
    private Integer cantAmbientes;

    @Column(name = "tipo_ambiente")
    private String tipoAmbiente;

    @Column(name = "expensas")
    private BigDecimal expensas;

    @Column(name = "hectareas")
    private Float hectareas;

    private boolean riego;
    private boolean accesoRuta;
    private Integer banios;
    private boolean vidrieraCalle;
    private boolean venta;

    //@JsonManagedReference(value = "inmueble-contratos")
    @JsonIgnoreProperties(value = {"inmueble"}, allowSetters = true)
    @OneToMany(mappedBy = "inmueble", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contrato> contratos = new ArrayList<>();

    //@JsonManagedReference(value = "inmueble-imagenes")
    @JsonIgnoreProperties(value = {"inmueble"}, allowSetters = true)
    @OneToMany(mappedBy = "inmueble", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenes = new ArrayList<>();

    //@JsonManagedReference(value = "inmueble-consultas")
    @JsonIgnoreProperties(value = {"inmueble"}, allowSetters = true)
    @OneToMany(mappedBy = "inmueble", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consulta> consultas = new ArrayList<>();

    // Métodos para agregar/quitar imágenes
    public void addImagen(Imagen imagen) {
        imagenes.add(imagen);
        imagen.setInmueble(this);
    }

}
