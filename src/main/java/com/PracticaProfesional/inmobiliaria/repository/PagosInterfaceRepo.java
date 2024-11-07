/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Pago;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sofia
 */
@Repository
public interface PagosInterfaceRepo extends JpaRepository<Pago, Integer>{

    //@Override
    //@Query("SELECT p FROM Pago p")
    //public List<Pago> findAll();
    @Query("SELECT p FROM Pago p WHERE "
            + "(:estado IS NULL OR p.estado = :estado) AND "
            + "(:fechaDesde IS NULL OR :fechaHasta IS NULL) OR p.fechaRegistro BETWEEN :fechaDesde AND :fechaHasta")
    public List<Pago> listarFiltro(Date fechaDesde, Date fechaHasta, String estado);

}
