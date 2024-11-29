package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Pago;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Sofia
 */
@Repository
public interface PagosInterfaceRepo extends JpaRepository<Pago, Integer> {

  
    @Query("SELECT p FROM Pago p WHERE "
            + "(:estado IS NULL OR p.estado = :estado) AND "
            + "((:fechaDesde IS NULL AND :fechaHasta IS NULL) OR "
            + "(p.fechaPago BETWEEN :fechaDesde AND :fechaHasta)) "
            + " AND (:agenteId IS NULL OR p.contrato.agente.id = :agenteId)"
            + "ORDER BY p.fechaRegistro DESC")
    List<Pago> listarFiltro(LocalDateTime fechaDesde, LocalDateTime fechaHasta, String estado, Integer agenteId);

    @Query("SELECT p FROM Pago p WHERE "
            + "(:estado IS NULL OR p.estado = :estado) AND "
            + "((:fechaDesde IS NULL AND :fechaHasta IS NULL) OR "
            + "(p.fechaRegistro BETWEEN :fechaDesde AND :fechaHasta)) "
            + " AND (:agenteId IS NULL OR p.contrato.agente.id = :agenteId)"
            + "ORDER BY p.fechaRegistro DESC")
    List<Pago> listarFiltroFechaRegistro(LocalDateTime fechaDesde, LocalDateTime fechaHasta, String estado, Integer agenteId);
}
