package br.com.selecao.locadora.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "lote")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Entidade que representa a tabela Lote no banco de dados
public class Lote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numerolote", nullable = false)
    private Integer numeroLote;

    @Column(name = "descricao", length = 60)
    private String descricao;

    @Column(name = "quantidade")
    private BigDecimal quantidade;

    @Column(name = "valorinicial")
    private BigDecimal valorInicial;

    @Column(name = "unidade", length = 128)
    private String unidade;

    // Varios lotes pertencem a um unico Leilao
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leilaoid", nullable = false)
    private Leilao leilao;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Define a data de criacao e atualizacao antes de persistir
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Atualiza a data de modificacao antes de atualizar o registro
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}