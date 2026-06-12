package com.server.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.server.app.entities.enums.TipoCategoria;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "categorias")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCategoria tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_padre_id")
    @JsonIgnoreProperties({"categoriaPadre", "hibernateLazyInitializer"})
    @ToString.Exclude
    private Categoria categoriaPadre;
}
