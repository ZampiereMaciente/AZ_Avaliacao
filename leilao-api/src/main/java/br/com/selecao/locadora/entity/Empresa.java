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
@Table(name = "empresa")
// Entidade que representa a tabela Empresa no banco de dados
public class Empresa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "razaosocial")
    private String razaoSocial;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "logradouro")
    private String logradouro;

    @Column(name = "municipio")
    private String municipio;

    @Column(name = "numero")
    private String numero;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "bairro")
    private String bairro;

    @Column(name = "cep")
    private String cep;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "email")
    private String email;

    @Column(name = "site")
    private String site;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "senha")
    private String senha;

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