package br.com.selecao.locadora.business;

import br.com.selecao.locadora.dto.request.LoteRequestDTO;
import br.com.selecao.locadora.dto.response.LoteResponseDTO;
import br.com.selecao.locadora.entity.Leilao;
import br.com.selecao.locadora.entity.Lote;
import br.com.selecao.locadora.exception.DuplicateResourceException;
import br.com.selecao.locadora.exception.ResourceNotFoundException;
import br.com.selecao.locadora.repository.LeilaoRepository;
import br.com.selecao.locadora.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoteBO {

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private LeilaoRepository leilaoRepository;

    public List<LoteResponseDTO> buscarPorLeilao(Long leilaoId) {
        List<Lote> lotes = loteRepository.findByLeilaoId(leilaoId);
        return lotes.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public LoteResponseDTO buscarPorId(Long id) {
        Lote lote = loteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado para o id: " + id));
        return converterParaDTO(lote);
    }

    public LoteResponseDTO salvar(LoteRequestDTO dto) {
        // Evita duplicar o número de lote no mesmo leilão
        if (loteRepository.existsByNumeroLoteAndLeilaoId(dto.getNumeroLote(), dto.getLeilaoId())) {
            throw new DuplicateResourceException("Já existe um lote com o número " + dto.getNumeroLote() + " neste leilão.");
        }

        Leilao leilao = leilaoRepository.findById(dto.getLeilaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Leilão não encontrado para o id: " + dto.getLeilaoId()));

        Lote lote = new Lote();
        lote.setNumeroLote(dto.getNumeroLote());
        lote.setDescricao(dto.getDescricao());
        lote.setQuantidade(dto.getQuantidade());
        lote.setValorInicial(dto.getValorInicial());
        lote.setUnidade(dto.getUnidade());
        lote.setLeilao(leilao);

        lote = loteRepository.save(lote);
        return converterParaDTO(lote);
    }

    public LoteResponseDTO atualizar(Long id, LoteRequestDTO dto) {
        Lote lote = loteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado para o id: " + id));

        Leilao leilao = leilaoRepository.findById(dto.getLeilaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Leilão não encontrado para o id: " + dto.getLeilaoId()));

        // Se o número mudou, valida duplicidade
        if (!lote.getNumeroLote().equals(dto.getNumeroLote()) &&
                loteRepository.existsByNumeroLoteAndLeilaoId(dto.getNumeroLote(), dto.getLeilaoId())) {
            throw new DuplicateResourceException("Já existe um lote com o número " + dto.getNumeroLote() + " neste leilão.");
        }

        lote.setNumeroLote(dto.getNumeroLote());
        lote.setDescricao(dto.getDescricao());
        lote.setQuantidade(dto.getQuantidade());
        lote.setValorInicial(dto.getValorInicial());
        lote.setUnidade(dto.getUnidade());
        lote.setLeilao(leilao);

        lote = loteRepository.save(lote);
        return converterParaDTO(lote);
    }

    public void deletar(Long id) {
        Lote lote = loteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lote não encontrado para o id: " + id));
        loteRepository.delete(lote);
    }

    private LoteResponseDTO converterParaDTO(Lote lote) {
        return new LoteResponseDTO(
                lote.getId(),
                lote.getNumeroLote(),
                lote.getDescricao(),
                lote.getQuantidade(),
                lote.getValorInicial(),
                lote.getUnidade(),
                lote.getLeilao().getId(),
                lote.getLeilao().getDescricao()
        );
    }
}