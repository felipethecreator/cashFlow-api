package com.cashflow.api.expense.controller;

import com.cashflow.api.expense.dto.input.CreateExpense;
import com.cashflow.api.expense.dto.input.UpdateExpense;
import com.cashflow.api.expense.dto.output.ExpenseResponse;
import com.cashflow.api.expense.service.ExpenseService;
import com.cashflow.api.common.security.AuthenticatedUser;
import com.cashflow.api.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(
        name = "Expenses",
        description = "Gerenciamento de despesas recorrentes do usuário"
)
@SecurityRequirement(name = "bearer-jwt")
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    @Operation(
            summary = "Listar despesas",
            description = "Retorna todas as despesas ativas do usuário autenticado. " +
                    "Despesas inativas não são incluídas nesta listagem."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Despesas listadas com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado - Token JWT inválido ou ausente",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<List<ExpenseResponse>> getMyExpenses(
            Authentication authentication
    ) {
        User user = AuthenticatedUser.require(authentication);
        return ResponseEntity.ok(expenseService.getUserExpenses(user.getId()));
    }

    @PostMapping
    @Operation(
            summary = "Criar despesa",
            description = "Cria uma nova despesa recorrente. A despesa pode ser configurada como " +
                    "recorrente (gera pagamentos todo mês) ou única. É necessário informar " +
                    "uma categoria válida que pertença ao usuário."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Despesa criada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos - Validação falhou (ex: valor negativo, dia inválido)",
                    content = @Content(
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria não encontrada ou não pertence ao usuário",
                    content = @Content(
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<ExpenseResponse> createExpense(
            Authentication authentication,
            @Valid @RequestBody CreateExpense request
    ) {
        User user = AuthenticatedUser.require(authentication);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(expenseService.createExpense(user.getId(), request));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar despesa",
            description = "Atualiza todos os campos de uma despesa existente. " +
                    "Permite alterar nome, valor, categoria, dia de vencimento, " +
                    "prioridade e status (ativo/inativo, recorrente/única)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Despesa atualizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Despesa não encontrada ou não pertence ao usuário",
                    content = @Content(
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<ExpenseResponse> updateExpense(
            Authentication authentication,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateExpense request
    ) {
        User user = AuthenticatedUser.require(authentication);
        return ResponseEntity.ok(expenseService.updateExpense(user.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar despesa",
            description = "Remove permanentemente uma despesa do sistema. " +
                    "ATENÇÃO: Esta ação não pode ser desfeita. " +
                    "Todos os pagamentos relacionados também serão deletados."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Despesa deletada com sucesso - Sem conteúdo no retorno"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Despesa não encontrada ou não pertence ao usuário",
                    content = @Content(
                            mediaType = "application/json"
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autenticado",
                    content = @Content(
                            mediaType = "application/json"
                    )
            )
    })
    public ResponseEntity<Void> deleteExpense(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        User user = AuthenticatedUser.require(authentication);
        expenseService.deleteExpense(user.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
