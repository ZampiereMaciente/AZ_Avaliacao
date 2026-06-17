package br.com.selecao.locadora.repository;

import br.com.selecao.locadora.entity.Leilao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeilaoRepository extends JpaRepository<Leilao, Long> {

    // Metodo util para validar se já existe um leilão com o mesmo código cadastrado
    boolean existsByCodigo(Integer codigo);

    Leilao findByCodigo(Integer codigo);
}