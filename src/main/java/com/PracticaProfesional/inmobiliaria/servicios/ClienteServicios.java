package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.repository.ClienteInterfaceRepo;
import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.interfaz.ClienteInterface;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Sofia
 */
@Service
public class ClienteServicios implements ClienteInterface {

    @Autowired
    private ClienteInterfaceRepo repo;

    @Override
    public Cliente guardar(Cliente cliente) {
        cliente.setFechaRegistro(new Date());
        return repo.save(cliente);
    }

    @Override
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<Cliente> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Cliente> listar() {
        return repo.findAll();
    }

}
