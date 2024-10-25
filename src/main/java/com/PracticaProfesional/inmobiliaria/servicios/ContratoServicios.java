/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.interfaz.ContratoInterface;
import com.PracticaProfesional.inmobiliaria.repository.ContratoInterfaceRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Sofia
 */
@Service
public class ContratoServicios implements ContratoInterface {

    @Autowired
    private ContratoInterfaceRepo repo;

    @Override
    @Transactional
    public Contrato guardar(Contrato contrato) {
        return repo.save(contrato);
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Contrato> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contrato> listar() {
        return repo.findAll();
    }
}
