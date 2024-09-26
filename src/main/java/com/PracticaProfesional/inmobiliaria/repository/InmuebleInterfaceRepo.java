/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sofia
 */
@Repository
public interface InmuebleInterfaceRepo extends JpaRepository<Inmueble, Integer>{
     @Query("SELECT i FROM Inmueble i WHERE "
            + "(:tipoInmueble IS NULL OR i.tipoInmueble LIKE %:tipoInmueble%) AND "
            + "(:ubicacion IS NULL OR i.ubicacion LIKE %:ubicacion%) AND "
            + "(:estado IS NULL OR i.estado LIKE %:estado%)")
    List<Inmueble> filtrarInmuebles(@Param("tipoInmueble") String tipoInmueble,
            @Param("ubicacion") String ubicacion,
            @Param("estado") String estado);
}
