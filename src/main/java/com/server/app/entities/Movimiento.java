package com.server.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "movimientos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal monto;

    @Column(name = "moneda_original", nullable = false, length = 10)
    private String monedaOriginal;

    @Column(name = "tasa_cambio", nullable = false, precision = 19, scale = 6)
    private BigDecimal tasaCambio;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column
    private String descripcion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}
