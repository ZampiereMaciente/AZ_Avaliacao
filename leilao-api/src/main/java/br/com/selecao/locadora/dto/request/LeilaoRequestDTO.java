package br.com.selecao.locadora.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeilaoRequestDTO {

    @NotNull(message = "O código do leilão é obrigatório.")
    private Integer codigo;

    @NotBlank(message = "A descrição do leilão é obrigatória.")
    private String descricao;

    @NotNull(message = "O ID da empresa vendedora é obrigatório.")
    private Long vendedorId;

    @NotNull(message = "A data de início previsto é obrigatória.")
    private LocalDateTime inicioPrevisto;
}