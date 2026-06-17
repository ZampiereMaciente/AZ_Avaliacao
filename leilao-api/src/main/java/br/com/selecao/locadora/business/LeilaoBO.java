package br.com.selecao.locadora.business;

import br.com.selecao.locadora.dto.request.LeilaoRequestDTO;
import br.com.selecao.locadora.dto.response.LeilaoResponseDTO;
import br.com.selecao.locadora.entity.Empresa;
import br.com.selecao.locadora.entity.Leilao;
import br.com.selecao.locadora.entity.Lote;
import br.com.selecao.locadora.exception.DeleteConflictException;
import br.com.selecao.locadora.exception.DuplicateResourceException;
import br.com.selecao.locadora.exception.ResourceNotFoundException;
import br.com.selecao.locadora.repository.EmpresaRepository;
import br.com.selecao.locadora.repository.LeilaoRepository;
import br.com.selecao.locadora.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeilaoBO {

    @Autowired
    private LeilaoRepository leilaoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private LoteRepository loteRepository;

    public List<LeilaoResponseDTO> buscarTodos() {
        List<Leilao> leiloes = leilaoRepository.findAll();
        return leiloes.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public LeilaoResponseDTO buscarPorId(Long id) {
        Leilao leilao = leilaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leilão não encontrado para o id: " + id));
        return converterParaDTO(leilao);
    }

    public LeilaoResponseDTO salvar(LeilaoRequestDTO dto) {
        if (leilaoRepository.existsByCodigo(dto.getCodigo())) {
            throw new DuplicateResourceException("Já existe um leilão cadastrado com o código: " + dto.getCodigo());
        }

        Empresa vendedor = empresaRepository.findById(dto.getVendedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa vendedora não encontrada para o id: " + dto.getVendedorId()));

        Leilao leilao = new Leilao();
        leilao.setCodigo(dto.getCodigo());
        leilao.setDescricao(dto.getDescricao());
        leilao.setVendedor(vendedor);
        leilao.setInicioPrevisto(dto.getInicioPrevisto());

        leilao = leilaoRepository.save(leilao);
        return converterParaDTO(leilao);
    }

    public LeilaoResponseDTO atualizar(Long id, LeilaoRequestDTO dto) {
        Leilao leilao = leilaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leilão não encontrado para o id: " + id));

        Leilao leilaoExistente = leilaoRepository.findByCodigo(dto.getCodigo());
        if (leilaoExistente != null && !leilaoExistente.getId().equals(id)) {
            throw new DuplicateResourceException("Já existe outro leilão cadastrado com o código: " + dto.getCodigo());
        }

        Empresa vendedor = empresaRepository.findById(dto.getVendedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa vendedora não encontrada para o id: " + dto.getVendedorId()));

        leilao.setCodigo(dto.getCodigo());
        leilao.setDescricao(dto.getDescricao());
        leilao.setVendedor(vendedor);
        leilao.setInicioPrevisto(dto.getInicioPrevisto());

        leilao = leilaoRepository.save(leilao);
        return converterParaDTO(leilao);
    }

    public void deletar(Long id) {
        Leilao leilao = leilaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leilão não encontrado para o id: " + id));

        try {
            leilaoRepository.delete(leilao);
        } catch (Exception ex) {
            throw new DeleteConflictException("Não é possível excluir o leilão pois ele já possui lotes ou compradores vinculados.");
        }
    }

    private LeilaoResponseDTO converterParaDTO(Leilao leilao) {

        // Busca todos os lotes associados a este leilão específico
        List<Lote> lotes = loteRepository.findByLeilaoId(leilao.getId());


        BigDecimal totalGeral = lotes.stream()
                .map(lote -> lote.getQuantidade().multiply(lote.getValorInicial()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new LeilaoResponseDTO(
                leilao.getId(),
                leilao.getCodigo(),
                leilao.getDescricao(),
                leilao.getVendedor().getId(),
                leilao.getVendedor().getRazaoSocial(),
                leilao.getInicioPrevisto(),
                totalGeral
        );
    }
}