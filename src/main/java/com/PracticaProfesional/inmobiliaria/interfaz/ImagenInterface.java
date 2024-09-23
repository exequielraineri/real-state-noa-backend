/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Imagen;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sofia
 */
public interface ImagenInterface {
    public Imagen guardar(Imagen imagen);
    
    public void eliminar(Integer id);
    
    public Optional<Imagen> obtener(Integer id);
    
    public List<Imagen> listar();
}
