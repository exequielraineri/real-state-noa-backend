/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Pago;
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
            + "(:provincia IS NULL OR u.provincia LIKE %:provincia%) AND u.activo=true ")
    List<Usuario> filtrarUsuario(@Param("rol") String rol,
            @Param("provincia") String provincia);

    @Query("SELECT u FROM Usuario u WHERE u.correo=:correo AND u.password=:password AND u.activo=true")
    Optional<Usuario> findByCorreoAndPassword(String correo, String password);

    @Override
    @Query("SELECT u FROM Usuario u WHERE u.activo=true")
    public List<Usuario> findAll();

    @Query("SELECT u FROM Usuario u WHERE (:provincia IS NULL OR u.provincia LIKE %:provincia%) AND u.activo=:activo")
    public List<Usuario> listarFiltrado(String provincia, boolean activo);

}
