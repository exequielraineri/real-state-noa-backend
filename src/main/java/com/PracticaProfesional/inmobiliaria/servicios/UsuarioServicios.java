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
        usuario.setComisionAlquiler(null);
        usuario.setComisionVenta(null);
        return repo.save(usuario);
    }

    @Override
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<Usuario> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Usuario> listar() {
        return repo.findAll();
    }

}
