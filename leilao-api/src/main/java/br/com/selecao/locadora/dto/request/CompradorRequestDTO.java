package br.com.selecao.locadora.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompradorRequestDTO {

    @NotNull(message = "O ID da empresa compradora é obrigatório.")
    private Long empresaId;

    @NotNull(message = "O ID do leilão é obrigatório.")
    private Long leilaoId;
}