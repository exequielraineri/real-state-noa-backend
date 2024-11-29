package com.PracticaProfesional.inmobiliaria.interfaz;


import com.PracticaProfesional.inmobiliaria.entidades.Pago;
import java.util.List;
import java.util.Optional;


public interface PagosInterface {
    public Pago guardar(Pago pagos);
    
    public void eliminar(Integer id);
    
    public Optional<Pago> obtener(Integer id);
    
    public List<Pago> listar();
}
