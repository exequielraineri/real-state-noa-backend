/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoContrato;
import com.PracticaProfesional.inmobiliaria.interfaz.ContratoInterface;
import com.PracticaProfesional.inmobiliaria.repository.ContratoInterfaceRepo;
import java.util.Date;
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
    public Contrato guardar(Contrato contrato) {
        contrato.setActivo(true);
        return repo.save(contrato);
    }

    @Override
    public void eliminar(Integer id) {
        Contrato contrato = obtener(id).get();
        contrato.setActivo(false);
        repo.save(contrato);
    }

    @Override
    public Optional<Contrato> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Contrato> listar() {
        return repo.findAll();
    }

    public List<Contrato> listarFiltrados(boolean activo, EnumEstadoContrato estado, Date fechaDesde, Date fechaHasta, Integer cliente) {
        return repo.filtrarContratos(activo,estado, fechaDesde, fechaHasta,cliente);
    }
}
