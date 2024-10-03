package com.PracticaProfesional.inmobiliaria.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
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
 * @author Sofia
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "inmueble")
@NamedQueries({
    @NamedQuery(name = "Inmueble.findByTieneRiego", query = "SELECT i FROM Inmueble i WHERE i.tieneRiego = :tieneRiego"),
    @NamedQuery(name = "Inmueble.findByTieneAccesoRuta", query = "SELECT i FROM Inmueble i WHERE i.tieneAccesoRuta = :tieneAccesoRuta"),
    @NamedQuery(name = "Inmueble.findByTieneBanios", query = "SELECT i FROM Inmueble i WHERE i.tieneBanios = :tieneBanios"),
    @NamedQuery(name = "Inmueble.findByTieneVidrieraCalle", query = "SELECT i FROM Inmueble i WHERE i.tieneVidrieraCalle = :tieneVidrieraCalle")})
public class Inmueble implements Serializable {

    private static final long serialVersionUID = 1L;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "id_propietario")
    private Integer idPropietario;
    private String nomPro;
    private String apPro;
    @Column(name = "tipo_inmueble")
    private String tipoInmueble;
    @Column(name = "ubicacion")
    private String ubicacion;
    @Column(name = "tipo_operacion")
    private String tipoOperacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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
    @Column(name = "tiene_riego")
    private Boolean tieneRiego;
    @Column(name = "tiene_acceso_ruta")
    private Boolean tieneAccesoRuta;
    @Column(name = "tiene_banios")
    private Boolean tieneBanios;
    @Column(name = "tiene_vidriera_calle")
    private Boolean tieneVidrieraCalle;
    @OneToMany(mappedBy = "idInmueble")
    private List<Contrato> contratoCollection;

    @OneToMany(mappedBy = "idInmueble", cascade = CascadeType.ALL)
    private List<Imagen> imagenCollection;
    @OneToMany(mappedBy = "idInmueble")
    private List<Consulta> consultaCollection;

    // Métodos para agregar/quitar imágenes
    public void addImagen(Imagen imagen) {
        if (imagenCollection == null) {
            imagenCollection = new ArrayList<>();
        }
        imagenCollection.add(imagen);
        imagen.setIdInmueble(this);
    }

}
