package br.com.selecao.locadora.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
// Excecao customizada para indicar conflito de integridade referencial ao deletar registros
public class DeleteConflictException extends RuntimeException {
    public DeleteConflictException(String message) {
        super(message);
    }
}