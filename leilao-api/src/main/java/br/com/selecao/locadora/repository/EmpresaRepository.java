package br.com.selecao.locadora.repository;

import br.com.selecao.locadora.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// Repositorio JPA para realizar operacoes de banco de dados na tabela Empresa
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    boolean existsByCnpj(String cnpj);

    boolean existsByEmail(String email);

    boolean existsByUsuario(String usuario);

    Empresa findByCnpj(String cnpj);
}