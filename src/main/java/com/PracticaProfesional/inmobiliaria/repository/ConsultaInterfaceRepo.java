/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Consulta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sofia
 */
@Repository
public interface ConsultaInterfaceRepo extends JpaRepository<Consulta, Integer> {

    @Override
    @Query("SELECT c FROM Consulta c WHERE c.activo=true")
    List<Consulta> findAll();
}
