package br.com.selecao.locadora.repository;

import br.com.selecao.locadora.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Repositorio JPA para realizar operacoes de banco de dados na tabela Lote
public interface LoteRepository extends JpaRepository<Lote, Long> {

    // Busca todos os lotes que pertencem a um leilao especifico
    List<Lote> findByLeilaoId(Long leilaoId);

    // Ajuda a verificar se ja existe o numero do lote no mesmo leilao
    boolean existsByNumeroLoteAndLeilaoId(Integer numeroLote, Long leilaoId);
}