/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoCliente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sofia
 */
@Repository
public interface ClienteInterfaceRepo extends JpaRepository<Cliente, Integer> {

    @Query("SELECT c FROM Cliente c WHERE "
            + "(:nombre IS NULL OR c.nombre LIKE %:nombre%) AND "
            + "(:apellido IS NULL OR c.apellido LIKE %:apellido%) AND "
            + "(:provincia IS NULL OR c.provincia LIKE %:provincia%)")
    List<Cliente> filtrarClientes(@Param("nombre") String nombre,
            @Param("apellido") String apellido,
            @Param("provincia") String provincia);

    public Optional<Cliente> findById(Integer id);

    @Query("SELECT c FROM Cliente c WHERE c.tipoCliente=:tipo")
    List<Cliente> findByTipoCliente(EnumTipoCliente tipo);

}
