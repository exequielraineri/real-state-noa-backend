/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Transaccion;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sofia
 */
public interface TransaccionInterface {
    public Transaccion guardar(Transaccion transaccion);
    
    public void eliminar(Integer id);
    
    public Optional<Transaccion> obtener(Integer id);
    
    public List<Transaccion> listar();
}
