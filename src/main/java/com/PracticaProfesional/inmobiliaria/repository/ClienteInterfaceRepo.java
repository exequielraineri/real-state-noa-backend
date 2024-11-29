package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ClienteInterfaceRepo extends JpaRepository<Cliente, Integer> {

    @Query("SELECT c FROM Cliente c WHERE "
            + "(:provincia IS NULL OR c.provincia LIKE %:provincia%) AND "
            + "(:estado IS NULL OR c.activo = :estado) AND (:nombre IS NULL OR c.nombre LIKE %:nombre% OR c.apellido LIKE %:nombre%)")
    List<Cliente> filtrarClientes(
            String provincia, boolean estado, String nombre);

    @Override
    public Optional<Cliente> findById(Integer id);

}
