/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoInmueble;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoInmuebles;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sofia
 */
@Repository
public interface InmuebleInterfaceRepo extends JpaRepository<Inmueble, Integer>, JpaSpecificationExecutor<Inmueble>  {

    @Query("SELECT i FROM Inmueble i WHERE (:tipoInmueble IS NULL OR i.tipoInmueble=:tipoInmueble) AND (:direccion IS NULL OR i.direccion LIKE %:direccion%) AND (:estado IS NULL OR i.estado = :estado) AND i.activo=true ")
    List<Inmueble> filtrarInmuebles(@Param("tipoInmueble") EnumTipoInmuebles tipoInmueble,
            @Param("direccion") String direccion,
            @Param("estado") EnumEstadoInmueble estado);

    @Override
    @Query("SELECT i FROM Inmueble i WHERE i.activo=true")
    public List<Inmueble> findAll();
}
