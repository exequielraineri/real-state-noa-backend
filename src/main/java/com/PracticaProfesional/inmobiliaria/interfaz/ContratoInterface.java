/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sofia
 */
public interface ContratoInterface {
    public Contrato guardar(Contrato contrato);
    
    public void eliminar(Integer id);
    
    public Optional<Contrato> obtener(Integer id);
    
    public List<Contrato> listar();
}
