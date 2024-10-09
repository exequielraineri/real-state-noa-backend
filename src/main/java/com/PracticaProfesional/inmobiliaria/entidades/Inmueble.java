package com.PracticaProfesional.inmobiliaria.entidades;

import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoInmuebles;
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inmueble")
@ToString
public class Inmueble implements Serializable {

    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    private String titulo;
    @ManyToOne
    @JoinColumn(name = "id_propietario")
    private Cliente propietario;
    @Column(name = "tipo_inmueble")
    private EnumTipoInmuebles tipoInmueble;
    @Column(name = "direccion")
    private String direccion;
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
    private String estado;
    @Column(name = "fecha_publicacion")
    @Temporal(TemporalType.TIMESTAMP)
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
    private BigDecimal hectareas;
    private Boolean isRiego;
    private Boolean isAccesoRuta;
    private Integer banios;
    private Boolean isVidriera;
    private Boolean isVenta;
    @OneToMany(mappedBy = "idInmueble")
    private List<Contrato> contratos = new ArrayList<>();

    @OneToMany(mappedBy = "idInmueble", cascade = CascadeType.ALL)
    private List<Imagen> imagenes = new ArrayList<>();
    @OneToMany(mappedBy = "idInmueble")
    private List<Consulta> consultas = new ArrayList<>();

    // Métodos para agregar/quitar imágenes
    public void addImagen(Imagen imagen) {
        imagenes.add(imagen);
        imagen.setIdInmueble(this);
    }

}
