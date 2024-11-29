package com.PracticaProfesional.inmobiliaria.servicios;

import com.PracticaProfesional.inmobiliaria.entidades.Notificacion;
import com.PracticaProfesional.inmobiliaria.interfaz.NotificacionesInterface;
import com.PracticaProfesional.inmobiliaria.repository.NotificacionesInterfaceRepo;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificacionServicios implements NotificacionesInterface {

    @Autowired
    private NotificacionesInterfaceRepo repo;

    @Override
    public Notificacion guardar(Notificacion notificacion) {
        notificacion.setActivo(true);
        return repo.save(notificacion);
    }

    @Override
    public void eliminar(Integer id) {
        Notificacion notificacion = obtener(id).get();
        notificacion.setActivo(false);
        repo.save(notificacion);
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
