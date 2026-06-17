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
public class Leilao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false)
    private Integer codigo;

    @Column(name = "descricao", length = 60)
    private String descricao;

    // Relacionamento muitos para um: Vários leilões pertencem a uma Empresa (Vendedor)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor", nullable = false)
    private Empresa vendedor;

    @Column(name = "inicioprevisto")
    private LocalDateTime inicioPrevisto;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}