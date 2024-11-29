package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Transaccion;
import java.util.List;
import java.util.Optional;


public interface TransaccionInterface {
    public Transaccion guardar(Transaccion transaccion);
    
    public void eliminar(Integer id);
    
    public Optional<Transaccion> obtener(Integer id);
    
    public List<Transaccion> listar();
}
