package br.com.selecao.locadora.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeilaoResponseDTO {

    private Long id;
    private Integer codigo;
    private String descricao;
    private Long vendedorId;
    private String vendedorRazaoSocial;
    private LocalDateTime inicioPrevisto;

    private BigDecimal totalLeilao;
}