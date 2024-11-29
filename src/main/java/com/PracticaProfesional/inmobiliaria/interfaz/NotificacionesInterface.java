package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Notificacion;
import java.util.List;
import java.util.Optional;


public interface NotificacionesInterface {
    public Notificacion guardar(Notificacion notificacion);
    
    public void eliminar(Integer id);
    
    public Optional<Notificacion> obtener(Integer id);
    
    public List<Notificacion> listar();
}
