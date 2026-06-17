package br.com.selecao.locadora.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// DTO de resposta para retornar dados de Empresa
public class EmpresaResponseDTO {

    private Long id;
    private String razaoSocial;
    private String cnpj;
    private String email;
    private String telefone;
    private String site;
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String municipio;
    private String usuario;
}