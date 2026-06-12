package com.server.app.dto.cuenta;

import com.server.app.entities.enums.TipoCuenta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CuentaDto {

    @NotBlank(message = "El alias es requerido")
    private String alias;

    @NotBlank(message = "La moneda es requerida")
    @Size(max = 10, message = "La moneda no puede superar 10 caracteres")
    private String moneda;

    @NotNull(message = "El saldo base es requerido")
    @DecimalMin(value = "0.00", message = "El saldo base no puede ser negativo")
    private BigDecimal saldoBase;

    @NotNull(message = "El tipo de cuenta es requerido")
    private TipoCuenta tipo;

    @NotNull(message = "El usuario es requerido")
    private Integer usuarioId;
}
