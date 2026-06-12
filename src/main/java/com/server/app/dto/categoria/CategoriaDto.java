package com.server.app.dto.categoria;

import com.server.app.entities.enums.TipoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoriaDto {

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotNull(message = "El tipo de categoría es requerido")
    private TipoCategoria tipo;

    private Long categoriaPadreId;
}
