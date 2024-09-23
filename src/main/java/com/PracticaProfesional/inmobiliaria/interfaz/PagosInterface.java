/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.interfaz;


import com.PracticaProfesional.inmobiliaria.entidades.Pagos;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sofia
 */
public interface PagosInterface {
    public Pagos guardar(Pagos pagos);
    
    public void eliminar(Integer id);
    
    public Optional<Pagos> obtener(Integer id);
    
    public List<Pagos> listar();
}
