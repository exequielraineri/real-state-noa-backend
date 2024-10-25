/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
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
public interface UsuarioInterfaceRepo extends JpaRepository<Usuario, Integer> {

    @Query("SELECT u FROM Usuario u WHERE "
            + "(:rol IS NULL OR u.rol LIKE %:rol%) AND "
            + "(:provincia IS NULL OR u.provincia LIKE %:provincia%)")
    List<Usuario> filtrarUsuario(@Param("rol") String rol,
            @Param("provincia") String provincia);

    @Query("SELECT u FROM Usuario u WHERE u.correo=:correo AND u.password=:password")
    Optional<Usuario> findByCorreoAndPassword(String correo, String password);
}
