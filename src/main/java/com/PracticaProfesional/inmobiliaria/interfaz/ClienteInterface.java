/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sofia
 */
public interface ClienteInterface {
    public Cliente guardar (Cliente cliente);
    
    public void eliminar (Integer id);
    
    public Optional<Cliente> obtener(Integer id);
    
    public List<Cliente> listar();
}
