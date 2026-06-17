package br.com.selecao.locadora.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// DTO de requisicao para receber os dados de criacao ou edicao de Unidade
public class UnidadeRequestDTO {

    @NotBlank(message = "O nome da unidade é obrigatório")
    @Size(max = 100, message = "O nome da unidade deve conter no máximo 100 caracteres")
    private String nome;
}
