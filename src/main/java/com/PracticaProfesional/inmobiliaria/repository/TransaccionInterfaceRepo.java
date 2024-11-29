package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Transaccion;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface TransaccionInterfaceRepo extends JpaRepository<Transaccion, Integer> {

    @Override
    @Query("SELECT t FROM Transaccion t WHERE t.estado=true")
    public List<Transaccion> findAll();

    @Query("SELECT t FROM Transaccion t WHERE (:estado IS NULL OR t.estado=:estado) AND ((:fechaDesde IS NULL AND :fechaHasta IS NULL) OR t.fechaTransaccion BETWEEN :fechaDesde AND :fechaHasta) AND (:tipoTransaccion IS NULL OR t.tipoTransaccion=:tipoTransaccion) AND (:tipoOperacion IS NULL OR t.tipoOperacion=:tipoOperacion) ORDER BY t.fechaTransaccion DESC")
    public List<Transaccion> listarFiltrado(boolean estado, LocalDateTime fechaDesde, LocalDateTime fechaHasta, String tipoTransaccion, String tipoOperacion);
}
