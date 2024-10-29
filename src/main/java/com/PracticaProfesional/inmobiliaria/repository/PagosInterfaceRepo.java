/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Notificacion;
import com.PracticaProfesional.inmobiliaria.entidades.Pago;
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

    @Override
    @Query("SELECT p FROM Pago p WHERE p.activo=true")
    public List<Pago> findAll();
    
}
