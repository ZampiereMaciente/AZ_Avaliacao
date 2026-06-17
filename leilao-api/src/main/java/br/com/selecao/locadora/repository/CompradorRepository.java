package br.com.selecao.locadora.repository;

import br.com.selecao.locadora.entity.Comprador;
import br.com.selecao.locadora.entity.CompradorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// Repositorio JPA para realizar operacoes de banco de dados na tabela Comprador
public interface CompradorRepository extends JpaRepository<Comprador, CompradorId> {

    // Busca todos os compradores vinculados a um leilao especifico
    List<Comprador> findByIdLeilaoId(Long leilaoId);
}