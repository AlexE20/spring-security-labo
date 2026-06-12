package com.server.app.repositories;

import com.server.app.entities.Categoria;
import com.server.app.entities.enums.TipoCategoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("SELECT c FROM Categoria c WHERE (:tipo IS NULL OR c.tipo = :tipo)")
    Page<Categoria> findAll(Pageable pageable, @Param("tipo") TipoCategoria tipo);
}
