/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Inmueble;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sofia
 */
public interface InmuebleInterface {
    public Inmueble guardar(Inmueble inmueble);
    
    public void eliminar(Integer id);
    
    public Optional<Inmueble> obtener(Integer id);
    
    public List<Inmueble> listar();
}
