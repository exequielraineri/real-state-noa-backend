/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.*;
/**
 *
 * @author Sofia
 */
@Entity
@Table(name = "transaccion")
@NamedQueries({
    @NamedQuery(name = "Transaccion.findAll", query = "SELECT t FROM Transaccion t"),
    @NamedQuery(name = "Transaccion.findById", query = "SELECT t FROM Transaccion t WHERE t.id = :id"),
    @NamedQuery(name = "Transaccion.findByTipoTransaccion", query = "SELECT t FROM Transaccion t WHERE t.tipoTransaccion = :tipoTransaccion"),
    @NamedQuery(name = "Transaccion.findByTipoOperacion", query = "SELECT t FROM Transaccion t WHERE t.tipoOperacion = :tipoOperacion"),
    @NamedQuery(name = "Transaccion.findByImporte", query = "SELECT t FROM Transaccion t WHERE t.importe = :importe"),
    @NamedQuery(name = "Transaccion.findByDescripcion", query = "SELECT t FROM Transaccion t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "Transaccion.findByFechaTransaccion", query = "SELECT t FROM Transaccion t WHERE t.fechaTransaccion = :fechaTransaccion"),
    @NamedQuery(name = "Transaccion.findByEstado", query = "SELECT t FROM Transaccion t WHERE t.estado = :estado")})
public class Transaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "tipo_transaccion")
    private String tipoTransaccion;
    @Column(name = "tipo_operacion")
    private String tipoOperacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "importe")
    private BigDecimal importe;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha_transaccion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaTransaccion;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "id_agente", referencedColumnName = "id")
    @ManyToOne
    private Usuario idAgente;

    public Transaccion() {
    }

    public Transaccion(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Usuario getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(Usuario idAgente) {
        this.idAgente = idAgente;
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
        if (!(object instanceof Transaccion)) {
            return false;
        }
        Transaccion other = (Transaccion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.PracticaProfesional.inmobiliaria.entidades.Transaccion[ id=" + id + " ]";
    }
    
}
