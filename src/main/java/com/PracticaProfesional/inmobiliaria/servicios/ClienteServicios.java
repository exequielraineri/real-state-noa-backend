package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.repository.ClienteInterfaceRepo;
import com.PracticaProfesional.inmobiliaria.entidades.Cliente;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoCliente;
import com.PracticaProfesional.inmobiliaria.interfaz.ClienteInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import jakarta.persistence.criteria.Predicate;
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

    public List<Cliente> listarPorTipoCliente(String tipo) {
        return repo.findByTipoCliente(EnumTipoCliente.valueOf(tipo.toUpperCase()));
    }

    public List<Cliente> listarPorFiltros(String provincia, String estado, String tipoCliente) {
        return repo.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (tipoCliente != null && !tipoCliente.isEmpty()) {
                predicates.add((Predicate) criteriaBuilder.equal(root.get("tipoCliente"), EnumTipoCliente.valueOf(tipoCliente.toUpperCase())));
            }

            if (estado != null && !estado.isEmpty()) {
                predicates.add((Predicate) criteriaBuilder.like(root.get("estado"), "%" + estado + "%"));
            }

            if (provincia != null && !provincia.isEmpty()) {
                predicates.add((Predicate) criteriaBuilder.like(root.get("provincia"), "%" + provincia + "%"));
            }
            return criteriaBuilder.and((jakarta.persistence.criteria.Predicate[]) predicates.toArray(new Predicate[0]));
        });
    }

}
