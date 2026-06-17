package br.com.selecao.locadora.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Objeto de resposta especifico para erros de validacao de campos
public class ValidationErrorResponse {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private Map<String, String> errors;
}