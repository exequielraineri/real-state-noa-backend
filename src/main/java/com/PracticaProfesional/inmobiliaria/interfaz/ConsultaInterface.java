/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Consulta;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sofia
 */
public interface ConsultaInterface {
    
    public Consulta guardar(Consulta consulta);
    
    public void eliminar(Integer id);
    
    public Optional<Consulta> obtener(Integer id);
    
    public List<Consulta> listar();
}
