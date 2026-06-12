package com.server.app.services;

import com.server.app.dto.categoria.CategoriaDto;
import com.server.app.entities.Categoria;
import com.server.app.entities.enums.TipoCategoria;
import com.server.app.exceptions.NotFoundException;
import com.server.app.repositories.CategoriaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional
    public Categoria create(CategoriaDto dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setTipo(dto.getTipo());

        if (dto.getCategoriaPadreId() != null) {
            Categoria padre = findById(dto.getCategoriaPadreId());
            categoria.setCategoriaPadre(padre);
        }

        return categoriaRepository.save(categoria);
    }

    public Page<Categoria> findAll(int page, int size, TipoCategoria tipo) {
        return categoriaRepository.findAll(PageRequest.of(page, size), tipo);
    }

    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría no encontrada"));
    }

    @Transactional
    public Categoria update(Long id, CategoriaDto dto) {
        Categoria categoria = findById(id);

        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            categoria.setNombre(dto.getNombre());
        }
        if (dto.getTipo() != null) {
            categoria.setTipo(dto.getTipo());
        }
        if (dto.getCategoriaPadreId() != null) {
            Categoria padre = findById(dto.getCategoriaPadreId());
            categoria.setCategoriaPadre(padre);
        } else {
            categoria.setCategoriaPadre(null);
        }

        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void delete(Long id) {
        Categoria categoria = findById(id);
        categoriaRepository.delete(categoria);
    }
}
