package com.server.app.repositories;

import com.server.app.entities.Movimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    @Query("SELECT m FROM Movimiento m WHERE (:cuentaId IS NULL OR m.cuenta.id = :cuentaId)")
    Page<Movimiento> findAll(Pageable pageable, @Param("cuentaId") Long cuentaId);
}
