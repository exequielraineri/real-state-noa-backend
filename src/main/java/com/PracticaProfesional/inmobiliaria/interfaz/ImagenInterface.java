package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Imagen;
import java.util.List;
import java.util.Optional;


public interface ImagenInterface {
    public Imagen guardar(Imagen imagen);
    
    public void eliminar(Integer id);
    
    public Optional<Imagen> obtener(Integer id);
    
    public List<Imagen> listar();
}
