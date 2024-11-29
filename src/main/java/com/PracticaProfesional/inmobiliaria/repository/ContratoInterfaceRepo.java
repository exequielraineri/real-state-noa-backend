package com.PracticaProfesional.inmobiliaria.repository;

import com.PracticaProfesional.inmobiliaria.entidades.Contrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumEstadoContrato;
import com.PracticaProfesional.inmobiliaria.entidades.util.EnumTipoContrato;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ContratoInterfaceRepo extends JpaRepository<Contrato, Integer> {

    @Override
    @Query("SELECT c FROM Contrato c WHERE c.activo=true")
    List<Contrato> findAll();

    @Query("SELECT c FROM Contrato c WHERE (:activo IS NULL OR c.activo=:activo) AND (:estado IS NULL OR c.estado=:estado) AND ((:fechaDesde IS NULL OR :fechaHasta IS NULL) OR c.fechaContrato BETWEEN :fechaDesde AND :fechaHasta) AND (:cliente IS NULL OR c.cliente.id=:cliente) AND (:tipoContrato IS NULL OR c.tipoContrato=:tipoContrato) ORDER BY c.fechaContrato ASC")
    public List<Contrato> filtrarContratos(boolean activo, EnumEstadoContrato estado, LocalDateTime fechaDesde, LocalDateTime fechaHasta, Integer cliente, EnumTipoContrato tipoContrato);
}
