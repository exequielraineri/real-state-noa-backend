/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Usuario;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sofia
 */
public interface UsuarioInterface {
    public Usuario guardar(Usuario usuario);
    
    public void eliminar(Integer id);
    
    public Optional<Usuario> obtener(Integer id);
    
    public List<Usuario> listar();
}
