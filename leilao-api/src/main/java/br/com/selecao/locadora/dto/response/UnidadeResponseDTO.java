package br.com.selecao.locadora.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// DTO de resposta para retornar dados de Unidade
public class UnidadeResponseDTO {

    private Long id;
    private String nome;

}