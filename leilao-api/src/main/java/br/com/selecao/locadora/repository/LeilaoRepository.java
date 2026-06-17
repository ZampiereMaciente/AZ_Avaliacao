package br.com.selecao.locadora.repository;

import br.com.selecao.locadora.entity.Leilao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Repositorio JPA para realizar operacoes de banco de dados na tabela Leilao
public interface LeilaoRepository extends JpaRepository<Leilao, Long> {

    // Metodo util para validar se ja existe um leilao com o mesmo codigo cadastrado
    boolean existsByCodigo(Integer codigo);

    Leilao findByCodigo(Integer codigo);
}