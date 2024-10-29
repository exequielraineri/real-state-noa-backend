/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sofia
 */
@Repository
public interface ContratoInterfaceRepo extends JpaRepository<Contrato, Integer> {

    @Override
    @Query("SELECT c FROM Contrato c WHERE c.activo=true")
    List<Contrato> findAll();
}
