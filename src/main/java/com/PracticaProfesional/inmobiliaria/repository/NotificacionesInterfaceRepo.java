package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Notificacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificacionesInterfaceRepo extends JpaRepository<Notificacion, Integer>{
    @Override
    @Query("SELECT n FROM Notificacion n WHERE n.activo=true")
    public List<Notificacion> findAll();
}
