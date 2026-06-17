package br.com.selecao.locadora.repository;

import br.com.selecao.locadora.entity.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Repositorio JPA para realizar operacoes de banco de dados na tabela Unidade
public interface UnidadeRepository extends JpaRepository<Unidade, Long> {
    boolean existsByNome(String nome);
    Unidade findByNome(String nome);
}
