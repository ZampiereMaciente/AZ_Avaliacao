package br.com.selecao.locadora.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "comprador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Entidade que representa a tabela Comprador para mapeamento muitos-para-muitos customizado
public class Comprador implements Serializable {

    // ID composto que mapeia a chave primaria da tabela comprador
    @EmbeddedId
    private CompradorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("empresaId")
    @JoinColumn(name = "empresa", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("leilaoId")
    @JoinColumn(name = "leilao", nullable = false)
    private Leilao leilao;
}