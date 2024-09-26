package com.PracticaProfesional.inmobiliaria.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.*;

/**
 *
 * @author Sofia
 */
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
    private Collection<Contrato> contratoCollection;
    @OneToMany(mappedBy = "idInmueble")
    private Collection<Imagen> imagenCollection;
    @OneToMany(mappedBy = "idInmueble")
    private Collection<Consulta> consultaCollection;

    public Inmueble() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(Integer idPropietario) {
        this.idPropietario = idPropietario;
    }

    public String getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(String tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public BigDecimal getPrecioAlquiler() {
        return precioAlquiler;
    }

    public void setPrecioAlquiler(BigDecimal precioAlquiler) {
        this.precioAlquiler = precioAlquiler;
    }

    public BigDecimal getImpMunicipales() {
        return impMunicipales;
    }

    public void setImpMunicipales(BigDecimal impMunicipales) {
        this.impMunicipales = impMunicipales;
    }

    public BigDecimal getImpInmobiliarios() {
        return impInmobiliarios;
    }

    public void setImpInmobiliarios(BigDecimal impInmobiliarios) {
        this.impInmobiliarios = impInmobiliarios;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public BigDecimal getMts2() {
        return mts2;
    }

    public void setMts2(BigDecimal mts2) {
        this.mts2 = mts2;
    }

    public Integer getCantAmbientes() {
        return cantAmbientes;
    }

    public void setCantAmbientes(Integer cantAmbientes) {
        this.cantAmbientes = cantAmbientes;
    }

    public String getTipoAmbiente() {
        return tipoAmbiente;
    }

    public void setTipoAmbiente(String tipoAmbiente) {
        this.tipoAmbiente = tipoAmbiente;
    }

    public BigDecimal getExpensas() {
        return expensas;
    }

    public void setExpensas(BigDecimal expensas) {
        this.expensas = expensas;
    }

    public BigDecimal getHectareas() {
        return hectareas;
    }

    public void setHectareas(BigDecimal hectareas) {
        this.hectareas = hectareas;
    }

    public Boolean getTieneRiego() {
        return tieneRiego;
    }

    public void setTieneRiego(Boolean tieneRiego) {
        this.tieneRiego = tieneRiego;
    }

    public Boolean getTieneAccesoRuta() {
        return tieneAccesoRuta;
    }

    public void setTieneAccesoRuta(Boolean tieneAccesoRuta) {
        this.tieneAccesoRuta = tieneAccesoRuta;
    }

    public Boolean getTieneBanios() {
        return tieneBanios;
    }

    public void setTieneBanios(Boolean tieneBanios) {
        this.tieneBanios = tieneBanios;
    }

    public Boolean getTieneVidrieraCalle() {
        return tieneVidrieraCalle;
    }

    public void setTieneVidrieraCalle(Boolean tieneVidrieraCalle) {
        this.tieneVidrieraCalle = tieneVidrieraCalle;
    }

    public Collection<Contrato> getContratoCollection() {
        return contratoCollection;
    }

    public void setContratoCollection(Collection<Contrato> contratoCollection) {
        this.contratoCollection = contratoCollection;
    }

    public Collection<Imagen> getImagenCollection() {
        return imagenCollection;
    }

    public void setImagenCollection(Collection<Imagen> imagenCollection) {
        this.imagenCollection = imagenCollection;
    }

    public Collection<Consulta> getConsultaCollection() {
        return consultaCollection;
    }

    public void setConsultaCollection(Collection<Consulta> consultaCollection) {
        this.consultaCollection = consultaCollection;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inmueble)) {
            return false;
        }
        Inmueble other = (Inmueble) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.PracticaProfesional.inmobiliaria.entidades.Inmueble[ id=" + id + " ]";
    }
    
}