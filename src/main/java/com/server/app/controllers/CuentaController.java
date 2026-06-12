package com.server.app.controllers;

import com.server.app.dto.cuenta.CuentaDto;
import com.server.app.dto.cuenta.CuentaUpdateDto;
import com.server.app.dto.response.Pagination;
import com.server.app.dto.response.PaginationMeta;
import com.server.app.entities.Cuenta;
import com.server.app.services.CuentaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuentas")
@AllArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<Cuenta> create(@Valid @RequestBody CuentaDto dto) {
        return ResponseEntity.ok(cuentaService.create(dto));
    }

    @GetMapping
    public ResponseEntity<Pagination<Cuenta>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer usuarioId
    ) {
        Page<Cuenta> p = cuentaService.findAll(page, size, usuarioId);
        return ResponseEntity.ok(new Pagination<>(
                p.getContent(),
                new PaginationMeta(p.getNumber(), p.getSize(), p.getTotalPages(), p.getTotalElements())
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> findById(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> update(@PathVariable Long id, @Valid @RequestBody CuentaUpdateDto dto) {
        return ResponseEntity.ok(cuentaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cuentaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
