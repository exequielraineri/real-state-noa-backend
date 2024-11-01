/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Transaccion;
import com.PracticaProfesional.inmobiliaria.interfaz.TransaccionInterface;
import com.PracticaProfesional.inmobiliaria.repository.TransaccionInterfaceRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author Sofia
 */
@Service
public class TransaccionServicios implements TransaccionInterface {

    @Autowired
    private TransaccionInterfaceRepo repo;

    @Override
    public Transaccion guardar(Transaccion transaccion) {
        transaccion.setActivo(true);
        return repo.save(transaccion);
    }

    @Override
    public void eliminar(Integer id) {
        Transaccion transaccion = obtener(id).get();
        transaccion.setActivo(false);
        repo.save(transaccion);
    }

    @Override
    public Optional<Transaccion> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Transaccion> listar() {
        Sort sort = Sort.by("fechaTransaccion").descending();
        return repo.findAll(sort);
    }

}
