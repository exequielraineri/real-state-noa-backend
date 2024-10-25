/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.interfaz;


import com.PracticaProfesional.inmobiliaria.entidades.Pago;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sofia
 */
public interface PagosInterface {
    public Pago guardar(Pago pagos);
    
    public void eliminar(Integer id);
    
    public Optional<Pago> obtener(Integer id);
    
    public List<Pago> listar();
}
