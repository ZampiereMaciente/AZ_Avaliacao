package br.com.selecao.locadora.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// DTO de resposta para retornar os dados de vinculo do comprador
public class CompradorResponseDTO {

    private Long empresaId;
    private String empresaRazaoSocial;
    private String empresaCnpj;
    private Long leilaoId;
    private String leilaoDescricao;
}