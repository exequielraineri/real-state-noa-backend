/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Notificacion;
import com.PracticaProfesional.inmobiliaria.interfaz.NotificacionesInterface;
import com.PracticaProfesional.inmobiliaria.repository.NotificacionesInterfaceRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Sofia
 */
@Service
public class NotificacionServicios implements NotificacionesInterface {

    @Autowired
    private NotificacionesInterfaceRepo repo;

    @Override
    public Notificacion guardar(Notificacion notificacion) {
        return repo.save(notificacion);
    }

    @Override
    public void eliminar(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Optional<Notificacion> obtener(Integer id) {
        return repo.findById(id);
    }

    @Override
    public List<Notificacion> listar() {
        return repo.findAll();
    }

}
