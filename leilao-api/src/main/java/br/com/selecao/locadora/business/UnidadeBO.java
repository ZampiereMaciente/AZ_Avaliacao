package br.com.selecao.locadora.business;

import br.com.selecao.locadora.dto.request.UnidadeRequestDTO;
import br.com.selecao.locadora.dto.response.UnidadeResponseDTO;
import br.com.selecao.locadora.entity.Unidade;
import br.com.selecao.locadora.exception.DuplicateResourceException;
import br.com.selecao.locadora.exception.ResourceNotFoundException;
import br.com.selecao.locadora.repository.UnidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
// Classe de regras de negocio para Unidades
public class UnidadeBO {

    @Autowired
    private UnidadeRepository unidadeRepository;

    // Retorna todos os registros mapeados para DTO
    public List<UnidadeResponseDTO> buscarTodos() {
        return unidadeRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Busca um registro pelo ID ou lanca excecao caso nao exista
    public UnidadeResponseDTO buscarPorId(Long id) {
        Unidade unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada para o id: " + id));
        return converterParaDTO(unidade);
    }

    // Insere uma nova unidade verificando duplicidade de nome
    public UnidadeResponseDTO salvar(UnidadeRequestDTO dto) {
        if (unidadeRepository.existsByNome(dto.getNome())) {
            throw new DuplicateResourceException("Já existe uma unidade cadastrada com o nome: " + dto.getNome());
        }

        Unidade unidade = new Unidade();
        unidade.setNome(dto.getNome());

        unidade = unidadeRepository.save(unidade);
        return converterParaDTO(unidade);
    }

    // Atualiza a unidade garantindo que o novo nome nao colida com outro registro
    public UnidadeResponseDTO atualizar(Long id, UnidadeRequestDTO dto) {
        Unidade unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada para o id: " + id));

        Unidade existente = unidadeRepository.findByNome(dto.getNome());
        if (existente != null && !existente.getId().equals(id)) {
            throw new DuplicateResourceException("Já existe outra unidade cadastrada com o nome: " + dto.getNome());
        }

        unidade.setNome(dto.getNome());

        unidade = unidadeRepository.save(unidade);
        return converterParaDTO(unidade);
    }

    // Remove a unidade do banco de dados pelo seu ID
    public void deletar(Long id) {
        Unidade unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade não encontrada para o id: " + id));
        unidadeRepository.delete(unidade);
    }

    private UnidadeResponseDTO converterParaDTO(Unidade unidade) {
        return new UnidadeResponseDTO(unidade.getId(), unidade.getNome());
    }
}