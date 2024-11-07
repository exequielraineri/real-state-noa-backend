/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import com.PracticaProfesional.inmobiliaria.interfaz.UsuarioInterface;
import com.PracticaProfesional.inmobiliaria.repository.UsuarioInterfaceRepo;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Sofia
 */
@Service
public class UsuarioServicios implements UsuarioInterface {

    @Autowired
    private UsuarioInterfaceRepo repo;

    @Override
    public Usuario guardar(Usuario usuario) {
        usuario.setFechaRegistro(new Date());
        usuario.setActivo(true);
        return repo.save(usuario);
    }

    @Override
    public void eliminar(Integer id) {
        Usuario usuario = obtener(id).get();
        usuario.setActivo(false);
        repo.save(usuario);
    }

    @Override
    public Optional<Usuario> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Usuario> listar() {
        return repo.findAll();
    }

    public List<Usuario> filtrarUsuario(String rol, String provincia) {
        return repo.filtrarUsuario(rol, provincia);
    }

    public Optional<Usuario> findByCorreoAndPassword(String correo, String password) {
        return repo.findByCorreoAndPassword(correo, password);
    }

    public List<Usuario> listarFiltrado(String provincia, boolean activo) {
        return repo.listarFiltrado(provincia, activo);
    }

}
