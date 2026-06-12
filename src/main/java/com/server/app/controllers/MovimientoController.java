package com.server.app.controllers;

import com.server.app.dto.movimiento.MovimientoDto;
import com.server.app.dto.response.Pagination;
import com.server.app.dto.response.PaginationMeta;
import com.server.app.entities.Movimiento;
import com.server.app.services.MovimientoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movimientos")
@AllArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<Movimiento> create(@Valid @RequestBody MovimientoDto dto) {
        return ResponseEntity.ok(movimientoService.create(dto));
    }

    @GetMapping
    public ResponseEntity<Pagination<Movimiento>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long cuentaId
    ) {
        Page<Movimiento> p = movimientoService.findAll(page, size, cuentaId);
        return ResponseEntity.ok(new Pagination<>(
                p.getContent(),
                new PaginationMeta(p.getNumber(), p.getSize(), p.getTotalPages(), p.getTotalElements())
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> findById(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movimiento> update(@PathVariable Long id, @Valid @RequestBody MovimientoDto dto) {
        return ResponseEntity.ok(movimientoService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movimientoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
