package com.server.app.services;

import com.server.app.dto.movimiento.MovimientoDto;
import com.server.app.entities.Categoria;
import com.server.app.entities.Cuenta;
import com.server.app.entities.Movimiento;
import com.server.app.entities.enums.TipoCategoria;
import com.server.app.exceptions.NotFoundException;
import com.server.app.repositories.CuentaRepository;
import com.server.app.repositories.MovimientoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final CategoriaService categoriaService;

    @Transactional
    public Movimiento create(MovimientoDto dto) {
        Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId())
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));

        Categoria categoria = categoriaService.findById(dto.getCategoriaId());

        BigDecimal montoConvertido = dto.getMonto().multiply(dto.getTasaCambio());
        actualizarSaldo(cuenta, categoria.getTipo(), montoConvertido);
        cuentaRepository.save(cuenta);

        Movimiento movimiento = new Movimiento();
        movimiento.setMonto(dto.getMonto());
        movimiento.setMonedaOriginal(dto.getMonedaOriginal());
        movimiento.setTasaCambio(dto.getTasaCambio());
        movimiento.setFecha(dto.getFecha());
        movimiento.setDescripcion(dto.getDescripcion());
        movimiento.setCuenta(cuenta);
        movimiento.setCategoria(categoria);

        return movimientoRepository.save(movimiento);
    }

    public Page<Movimiento> findAll(int page, int size, Long cuentaId) {
        return movimientoRepository.findAll(PageRequest.of(page, size), cuentaId);
    }

    public Movimiento findById(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movimiento no encontrado"));
    }

    @Transactional
    public Movimiento update(Long id, MovimientoDto dto) {
        Movimiento movimiento = findById(id);

        // Revertir el efecto del movimiento anterior
        BigDecimal montoAnterior = movimiento.getMonto().multiply(movimiento.getTasaCambio());
        actualizarSaldo(movimiento.getCuenta(), movimiento.getCategoria().getTipo(), montoAnterior.negate());
        cuentaRepository.save(movimiento.getCuenta());

        Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId())
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada"));
        Categoria categoria = categoriaService.findById(dto.getCategoriaId());

        // Aplicar el nuevo efecto
        BigDecimal montoNuevo = dto.getMonto().multiply(dto.getTasaCambio());
        actualizarSaldo(cuenta, categoria.getTipo(), montoNuevo);
        cuentaRepository.save(cuenta);

        movimiento.setMonto(dto.getMonto());
        movimiento.setMonedaOriginal(dto.getMonedaOriginal());
        movimiento.setTasaCambio(dto.getTasaCambio());
        movimiento.setFecha(dto.getFecha());
        movimiento.setDescripcion(dto.getDescripcion());
        movimiento.setCuenta(cuenta);
        movimiento.setCategoria(categoria);

        return movimientoRepository.save(movimiento);
    }

    @Transactional
    public void delete(Long id) {
        Movimiento movimiento = findById(id);

        // Revertir el efecto del movimiento en el saldo
        BigDecimal montoConvertido = movimiento.getMonto().multiply(movimiento.getTasaCambio());
        actualizarSaldo(movimiento.getCuenta(), movimiento.getCategoria().getTipo(), montoConvertido.negate());
        cuentaRepository.save(movimiento.getCuenta());

        movimientoRepository.delete(movimiento);
    }

    private void actualizarSaldo(Cuenta cuenta, TipoCategoria tipo, BigDecimal monto) {
        if (tipo == TipoCategoria.INGRESO) {
            cuenta.setSaldoBase(cuenta.getSaldoBase().add(monto));
        } else {
            cuenta.setSaldoBase(cuenta.getSaldoBase().subtract(monto));
        }
    }
}
