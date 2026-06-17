package br.com.selecao.locadora.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
// Chave primaria composta para a entidade Comprador
public class CompradorId implements Serializable {

    @Column(name = "empresa")
    private Long empresaId;

    @Column(name = "leilao")
    private Long leilaoId;
}