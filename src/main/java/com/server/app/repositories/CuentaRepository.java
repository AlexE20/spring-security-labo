package com.server.app.repositories;

import com.server.app.entities.Cuenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    @Query("SELECT c FROM Cuenta c WHERE (:usuarioId IS NULL OR c.usuario.id = :usuarioId)")
    Page<Cuenta> findAll(Pageable pageable, @Param("usuarioId") Integer usuarioId);
}
