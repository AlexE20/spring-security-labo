package com.server.app.dto.movimiento;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovimientoDto {

    @NotNull(message = "El monto es requerido")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotBlank(message = "La moneda original es requerida")
    @Size(max = 10)
    private String monedaOriginal;

    @NotNull(message = "La tasa de cambio es requerida")
    @DecimalMin(value = "0.000001", message = "La tasa de cambio debe ser mayor a 0")
    private BigDecimal tasaCambio;

    @NotNull(message = "La fecha es requerida")
    private LocalDateTime fecha;

    private String descripcion;

    @NotNull(message = "La cuenta es requerida")
    private Long cuentaId;

    @NotNull(message = "La categoría es requerida")
    private Long categoriaId;
}
