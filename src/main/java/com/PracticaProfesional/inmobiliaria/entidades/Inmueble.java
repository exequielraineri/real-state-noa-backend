package com.PracticaProfesional.inmobiliaria.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import jakarta.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "inmueble")
@NamedQueries({
    @NamedQuery(name = "Inmueble.findAll", query = "SELECT i FROM Inmueble i"),
    @NamedQuery(name = "Inmueble.findById", query = "SELECT i FROM Inmueble i WHERE i.id = :id"),
    @NamedQuery(name = "Inmueble.findByIdPropietario", query = "SELECT i FROM Inmueble i WHERE i.idPropietario = :idPropietario"),
    @NamedQuery(name = "Inmueble.findByTipoInmueble", query = "SELECT i FROM Inmueble i WHERE i.tipoInmueble = :tipoInmueble"),
    @NamedQuery(name = "Inmueble.findByUbicacion", query = "SELECT i FROM Inmueble i WHERE i.ubicacion = :ubicacion"),
    @NamedQuery(name = "Inmueble.findByTipoOperacion", query = "SELECT i FROM Inmueble i WHERE i.tipoOperacion = :tipoOperacion"),
    @NamedQuery(name = "Inmueble.findByPrecioVenta", query = "SELECT i FROM Inmueble i WHERE i.precioVenta = :precioVenta"),
    @NamedQuery(name = "Inmueble.findByPrecioAlquiler", query = "SELECT i FROM Inmueble i WHERE i.precioAlquiler = :precioAlquiler"),
    @NamedQuery(name = "Inmueble.findByImpMunicipales", query = "SELECT i FROM Inmueble i WHERE i.impMunicipales = :impMunicipales"),
    @NamedQuery(name = "Inmueble.findByImpInmobiliarios", query = "SELECT i FROM Inmueble i WHERE i.impInmobiliarios = :impInmobiliarios"),
    @NamedQuery(name = "Inmueble.findByFechaRegistro", query = "SELECT i FROM Inmueble i WHERE i.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "Inmueble.findByEstado", query = "SELECT i FROM Inmueble i WHERE i.estado = :estado"),
    @NamedQuery(name = "Inmueble.findByFechaPublicacion", query = "SELECT i FROM Inmueble i WHERE i.fechaPublicacion = :fechaPublicacion"),
    @NamedQuery(name = "Inmueble.findByMts2", query = "SELECT i FROM Inmueble i WHERE i.mts2 = :mts2"),
    @NamedQuery(name = "Inmueble.findByCantAmbientes", query = "SELECT i FROM Inmueble i WHERE i.cantAmbientes = :cantAmbientes"),
    @NamedQuery(name = "Inmueble.findByTipoAmbiente", query = "SELECT i FROM Inmueble i WHERE i.tipoAmbiente = :tipoAmbiente"),
    @NamedQuery(name = "Inmueble.findByExpensas", query = "SELECT i FROM Inmueble i WHERE i.expensas = :expensas"),
    @NamedQuery(name = "Inmueble.findByHectareas", query = "SELECT i FROM Inmueble i WHERE i.hectareas = :hectareas"),
    @NamedQuery(name = "Inmueble.findByTieneRiego", query = "SELECT i FROM Inmueble i WHERE i.tieneRiego = :tieneRiego"),
    @NamedQuery(name = "Inmueble.findByTieneAccesoRuta", query = "SELECT i FROM Inmueble i WHERE i.tieneAccesoRuta = :tieneAccesoRuta"),
    @NamedQuery(name = "Inmueble.findByTieneBanios", query = "SELECT i FROM Inmueble i WHERE i.tieneBanios = :tieneBanios"),
    @NamedQuery(name = "Inmueble.findByTieneVidrieraCalle", query = "SELECT i FROM Inmueble i WHERE i.tieneVidrieraCalle = :tieneVidrieraCalle")
})
public class Inmueble implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Column(name = "precio_venta")
    private BigDecimal precioVenta; // Cambiado a BigDecimal para mayor precisión

    @Column(name = "precio_alquiler")
    private BigDecimal precioAlquiler;

    @Column(name = "imp_municipales")
    private BigDecimal impMunicipales;

    @Column(name = "imp_inmobiliarios")
    private BigDecimal impInmobiliarios;


    @Column(name = "fecha_registro")
    private Date fechaRegistro; // Usando LocalDateTime

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_publicacion")
    private Date fechaPublicacion; // Usando LocalDateTime

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

    @OneToMany(mappedBy = "idInmueble", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Imagen> imagenCollection;

    @OneToMany(mappedBy = "idInmueble")
    private Collection<Consulta> consultaCollection;

    // Constructor vacío
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

    // Constructor con todos los campos (excepto las colecciones)
//    public Inmueble(Integer id, Integer idPropietario, String tipoInmueble, String ubicacion, String tipoOperacion,
//            BigDecimal precioVenta, BigDecimal precioAlquiler, BigDecimal impMunicipales,
//            BigDecimal impInmobiliarios, LocalDateTime fechaRegistro, String estado,
//            Date fechaPublicacion, BigDecimal mts2, Integer cantAmbientes,
//            String tipoAmbiente, BigDecimal expensas, BigDecimal hectareas,
//            Boolean tieneRiego, Boolean tieneAccesoRuta, Boolean tieneBanios,
//            Boolean tieneVidrieraCalle) {
//        this.id = id;
//        this.idPropietario = idPropietario;
//        this.tipoInmueble = tipoInmueble;
//        this.ubicacion = ubicacion;
//        this.tipoOperacion = tipoOperacion;
//        this.precioVenta = precioVenta;
//        this.precioAlquiler = precioAlquiler;
//        this.impMunicipales = impMunicipales;
//        this.impInmobiliarios = impInmobiliarios;
//        this.fechaRegistro = fechaRegistro;
//        this.estado = estado;
//        this.fechaPublicacion = fechaPublicacion;
//        this.mts2 = mts2;
//        this.cantAmbientes = cantAmbientes;
//        this.tipoAmbiente = tipoAmbiente;
//        this.expensas = expensas;
//        this.hectareas = hectareas;
//        this.tieneRiego = tieneRiego;
//        this.tieneAccesoRuta = tieneAccesoRuta;
//        this.tieneBanios = tieneBanios;
//        this.tieneVidrieraCalle = tieneVidrieraCalle;
//    }

    // Getters y Setters
    // ... (sin cambios, puedes mantener los anteriores)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Inmueble)) {
            return false;
        }
        Inmueble other = (Inmueble) object;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Inmueble{"
                + "id=" + id
                + ", idPropietario=" + idPropietario
                + ", tipoInmueble='" + tipoInmueble + '\''
                + ", ubicacion='" + ubicacion + '\''
                + ", tipoOperacion='" + tipoOperacion + '\''
                + ", precioVenta=" + precioVenta
                + ", precioAlquiler=" + precioAlquiler
                + ", impMunicipales=" + impMunicipales
                + ", impInmobiliarios=" + impInmobiliarios
                + ", fechaRegistro=" + fechaRegistro
                + ", estado='" + estado + '\''
                + ", fechaPublicacion=" + fechaPublicacion
                + ", mts2=" + mts2
                + ", cantAmbientes=" + cantAmbientes
                + ", tipoAmbiente='" + tipoAmbiente + '\''
                + ", expensas=" + expensas
                + ", hectareas=" + hectareas
                + ", tieneRiego=" + tieneRiego
                + ", tieneAccesoRuta=" + tieneAccesoRuta
                + ", tieneBanios=" + tieneBanios
                + ", tieneVidrieraCalle=" + tieneVidrieraCalle
                + '}';
    }
}
