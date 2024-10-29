/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Consulta;
import com.PracticaProfesional.inmobiliaria.interfaz.ConsultaInterface;
import com.PracticaProfesional.inmobiliaria.repository.ConsultaInterfaceRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Sofia
 */
@Service
public class ConsultaServicios implements ConsultaInterface {

    @Autowired
    private ConsultaInterfaceRepo repo;

    @Override
    public Consulta guardar(Consulta consulta) {
        consulta.setActivo(true);
        return repo.save(consulta);
    }

    @Override
    public void eliminar(Integer id) {
        Consulta consulta = obtener(id).get();
        consulta.setActivo(false);
        repo.save(consulta);
    }

    @Override
    public Optional<Consulta> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Consulta> listar() {
        return repo.findAll();
    }

}
