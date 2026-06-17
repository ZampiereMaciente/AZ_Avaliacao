package br.com.selecao.locadora.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leilao")
// Entidade que representa a tabela Leilao no banco de dados
public class Leilao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false)
    private Integer codigo;

    @Column(name = "descricao", length = 60)
    private String descricao;

    // Relacionamento muitos para um: Varios leiloes pertencem a uma Empresa (Vendedor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor", nullable = false)
    private Empresa vendedor;

    @Column(name = "inicioprevisto")
    private LocalDateTime inicioPrevisto;

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