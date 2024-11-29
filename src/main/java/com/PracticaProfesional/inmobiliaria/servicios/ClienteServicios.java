package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.repository.ClienteInterfaceRepo;
import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.interfaz.ClienteInterface;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClienteServicios implements ClienteInterface {

    @Autowired
    private ClienteInterfaceRepo repo;

    @Override
    public Cliente guardar(Cliente cliente) {
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setActivo(true);
        return repo.save(cliente);
    }

    @Override
    public void eliminar(Integer id) {
        Cliente cliente = obtener(id).get();
        cliente.setActivo(false);
        repo.save(cliente);
    }

    @Override
    public Optional<Cliente> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Cliente> listar() {
        return repo.findAll();
    }

    public List<Cliente> listarPorFiltros(String provincia, boolean estado,String nombre) {
        return repo.filtrarClientes(provincia, estado, nombre);
    }

}
