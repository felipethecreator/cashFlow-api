package com.cashflow.api.category.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateCategory(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
        String name,

        @Size(max = 50, message = "Ícone deve ter no máximo 50 caracteres")
        String icon,

        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Cor deve ser HEX válido (ex: #FF5733)")
        String color
) {}