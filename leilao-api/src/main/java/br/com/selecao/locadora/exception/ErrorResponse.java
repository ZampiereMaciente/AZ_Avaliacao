package br.com.selecao.locadora.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Objeto de resposta padrao para retornos de erros da API
public class ErrorResponse {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
}