package br.com.selecao.locadora.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoteRequestDTO {

    @NotNull(message = "O número do lote é obrigatório.")
    private Integer numeroLote;

    @NotBlank(message = "A descrição do lote é obrigatória.")
    private String descricao;

    @NotNull(message = "A quantidade é obrigatória.")
    @Positive(message = "A quantidade deve ser maior que zero.")
    private BigDecimal quantidade;

    @NotNull(message = "O valor inicial é obrigatório.")
    @Positive(message = "O valor inicial deve ser maior que zero.")
    private BigDecimal valorInicial;

    private String unidade;

    @NotNull(message = "O ID do leilão vinculado é obrigatório.")
    private Long leilaoId;
}