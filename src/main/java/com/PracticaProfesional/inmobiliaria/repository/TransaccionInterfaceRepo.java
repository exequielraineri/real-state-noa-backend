/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Transaccion;
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
public interface TransaccionInterfaceRepo extends JpaRepository<Transaccion, Integer> {

    @Override
    @Query("SELECT t FROM Transaccion t WHERE t.estado=true")
    public List<Transaccion> findAll();

    @Query("SELECT t FROM Transaccion t WHERE (:estado IS NULL OR t.estado=:estado) AND ((:fechaDesde IS NULL AND :fechaHasta IS NULL) OR t.fechaTransaccion BETWEEN :fechaDesde AND :fechaHasta) AND (:tipoTransaccion IS NULL OR t.tipoTransaccion=:tipoTransaccion) AND (:tipoOperacion IS NULL OR t.tipoOperacion=:tipoOperacion)")
    public List<Transaccion> listarFiltrado(boolean estado, Date fechaDesde, Date fechaHasta, String tipoTransaccion, String tipoOperacion);
}
