/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Notificacion;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Sofia
 */
public interface NotificacionesInterface {
    public Notificacion guardar(Notificacion notificacion);
    
    public void eliminar(Integer id);
    
    public Optional<Notificacion> obtener(Integer id);
    
    public List<Notificacion> listar();
}
