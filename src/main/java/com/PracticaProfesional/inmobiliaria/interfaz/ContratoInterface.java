package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import java.util.List;
import java.util.Optional;


public interface ContratoInterface {
    public Contrato guardar(Contrato contrato);
    
    public void eliminar(Integer id);
    
    public Optional<Contrato> obtener(Integer id);
    
    public List<Contrato> listar();
}
