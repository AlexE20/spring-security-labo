package com.server.app.services;

import com.server.app.dto.cuenta.CuentaDto;
import com.server.app.dto.cuenta.CuentaUpdateDto;
import com.server.app.entities.Cuenta;
import com.server.app.entities.User;
import com.server.app.exceptions.NotFoundException;
import com.server.app.repositories.CuentaRepository;
import com.server.app.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final UserRepository userRepository;

    @Transactional
    public Cuenta create(CuentaDto dto) {
        User usuario = userRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Cuenta cuenta = new Cuenta();
        cuenta.setAlias(dto.getAlias());
        cuenta.setMoneda(dto.getMoneda());
        cuenta.setSaldoBase(dto.getSaldoBase());
        cuenta.setTipo(dto.getTipo());
        cuenta.setUsuario(usuario);

        return cuentaRepository.save(cuenta);
    }

    public Page<Cuenta> findAll(int page, int size, Integer usuarioId) {
        return cuentaRepository.findAll(PageRequest.of(page, size), usuarioId);
    }

    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
    }

    @Transactional
    public Cuenta update(Long id, CuentaUpdateDto dto) {
        Cuenta cuenta = findById(id);

        if (dto.getAlias() != null && !dto.getAlias().isBlank()) {
            cuenta.setAlias(dto.getAlias());
        }
        if (dto.getMoneda() != null && !dto.getMoneda().isBlank()) {
            cuenta.setMoneda(dto.getMoneda());
        }
        if (dto.getSaldoBase() != null) {
            cuenta.setSaldoBase(dto.getSaldoBase());
        }
        if (dto.getTipo() != null) {
            cuenta.setTipo(dto.getTipo());
        }

        return cuentaRepository.save(cuenta);
    }

    @Transactional
    public void delete(Long id) {
        Cuenta cuenta = findById(id);
        cuentaRepository.delete(cuenta);
    }
}
