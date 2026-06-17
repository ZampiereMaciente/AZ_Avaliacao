package br.com.selecao.locadora.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoteResponseDTO {

    private Long id;
    private Integer numeroLote;
    private String descricao;
    private BigDecimal quantidade;
    private BigDecimal valorInicial;
    private String unidade;
    private Long leilaoId;
    private String leilaoDescricao;
}