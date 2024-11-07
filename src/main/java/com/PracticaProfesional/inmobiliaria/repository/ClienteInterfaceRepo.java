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
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sofia
 */
@Repository
public interface ClienteInterfaceRepo extends JpaRepository<Cliente, Integer> {

    @Query("SELECT c FROM Cliente c WHERE "
            + "(:provincia IS NULL OR c.provincia LIKE %:provincia%) AND "
            + "(:estado IS NULL OR c.activo = :estado) AND "
            + "(:tipoCliente IS NULL OR c.tipoCliente = :tipoCliente)")
    List<Cliente> filtrarClientes(
            String provincia,
            boolean estado,
            EnumTipoCliente tipoCliente);

    public Optional<Cliente> findById(Integer id);

}
