/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.entidades;

import java.io.Serializable;
import java.util.Date;import jakarta.persistence.*;

/**
 *
 * @author Sofia
 */
@Entity
@Table(name = "consulta")
@NamedQueries({
    @NamedQuery(name = "Consulta.findAll", query = "SELECT c FROM Consulta c"),
    @NamedQuery(name = "Consulta.findById", query = "SELECT c FROM Consulta c WHERE c.id = :id"),
    @NamedQuery(name = "Consulta.findByMensaje", query = "SELECT c FROM Consulta c WHERE c.mensaje = :mensaje"),
    @NamedQuery(name = "Consulta.findByFechaRegistro", query = "SELECT c FROM Consulta c WHERE c.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "Consulta.findByFechaRespuesta", query = "SELECT c FROM Consulta c WHERE c.fechaRespuesta = :fechaRespuesta"),
    @NamedQuery(name = "Consulta.findByEstado", query = "SELECT c FROM Consulta c WHERE c.estado = :estado")})
public class Consulta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
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
    @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    @ManyToOne
    private Cliente idCliente;
    @JoinColumn(name = "id_inmueble", referencedColumnName = "id")
    @ManyToOne
    private Inmueble idInmueble;
    @JoinColumn(name = "id_agente", referencedColumnName = "id")
    @ManyToOne
    private Usuario idAgente;

    public Consulta() {
    }

    public Consulta(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(Date fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Inmueble getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(Inmueble idInmueble) {
        this.idInmueble = idInmueble;
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
        if (!(object instanceof Consulta)) {
            return false;
        }
        Consulta other = (Consulta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.PracticaProfesional.inmobiliaria.entidades.Consulta[ id=" + id + " ]";
    }
    
}
