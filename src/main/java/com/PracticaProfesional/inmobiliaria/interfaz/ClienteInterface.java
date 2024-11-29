package com.PracticaProfesional.inmobiliaria.interfaz;

import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteInterface {

    public Cliente guardar(Cliente cliente);

    public void eliminar(Integer id);

    public Optional<Cliente> obtener(Integer id);

    public List<Cliente> listar();

}
