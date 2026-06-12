package com.server.app.entities;

import com.server.app.entities.enums.TipoCuenta;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "cuentas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String alias;

    @Column(nullable = false, length = 10)
    private String moneda;

    @Column(name = "saldo_base", nullable = false, precision = 19, scale = 4)
    private BigDecimal saldoBase;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCuenta tipo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;
}
