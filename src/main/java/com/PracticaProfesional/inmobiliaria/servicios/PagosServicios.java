/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Pago;
import com.PracticaProfesional.inmobiliaria.interfaz.PagosInterface;
import com.PracticaProfesional.inmobiliaria.repository.PagosInterfaceRepo;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Sofia
 */
@Service
public class PagosServicios implements PagosInterface {

    @Autowired
    private PagosInterfaceRepo repo;

    @Override
    @Transactional
    public Pago guardar(Pago pago) {
        pago.setActivo(true);
        return repo.save(pago);
    }

    @Override
    public void eliminar(Integer id) {
        Pago pago = obtener(id).get();
        pago.setActivo(false);
        repo.save(pago);
    }

    @Override
    public Optional<Pago> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Pago> listar() {
        Sort sort = Sort.by("fechaPago").ascending();
        return repo.findAll(sort);
    }

    public List<Pago> listarFiltro(Date fechaDesde, Date fechaHasta, String estado){
        return repo.listarFiltro(fechaDesde, fechaHasta, estado);
    }

}
