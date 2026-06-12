package com.server.app.dto.cuenta;

import com.server.app.entities.enums.TipoCuenta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CuentaUpdateDto {

    @Size(max = 255)
    private String alias;

    @Size(max = 10)
    private String moneda;

    @DecimalMin(value = "0.00", message = "El saldo base no puede ser negativo")
    private BigDecimal saldoBase;

    private TipoCuenta tipo;
}
