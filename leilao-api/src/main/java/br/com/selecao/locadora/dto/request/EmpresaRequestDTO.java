package br.com.selecao.locadora.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequestDTO {

    @NotBlank(message = "Razão social é obrigatória.")
    private String razaoSocial;

    @NotBlank(message = "CNPJ é obrigatório.")
    @Size(min = 14, max = 14, message = "CNPJ deve conter exatamente 14 numeros.")
    private String cnpj;

    private String logradouro;
    private String municipio;
    private String numero;
    private String complemento;
    private String bairro;
    private String cep;
    private String telefone;

    @NotBlank(message = "Email é obrigatório.")
    @Email(message = "Email invalido")
    private String email;

    private String site;

    @NotBlank(message = "Usuário é obrigatório.")
    private String usuario;
    private String senha;

}