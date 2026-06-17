package br.com.selecao.locadora.repository;

import br.com.selecao.locadora.entity.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, Long> {

    // Busca todos os lotes que pertencem a um leilão específico
    List<Lote> findByLeilaoId(Long leilaoId);

    // Ajuda a verificar se já existe o número do lote no mesmo leilão
    boolean existsByNumeroLoteAndLeilaoId(Integer numeroLote, Long leilaoId);
}