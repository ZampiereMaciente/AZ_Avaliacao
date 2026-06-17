package br.com.selecao.locadora.business;

import br.com.selecao.locadora.dto.request.CompradorRequestDTO;
import br.com.selecao.locadora.dto.response.CompradorResponseDTO;
import br.com.selecao.locadora.entity.Comprador;
import br.com.selecao.locadora.entity.CompradorId;
import br.com.selecao.locadora.entity.Empresa;
import br.com.selecao.locadora.entity.Leilao;
import br.com.selecao.locadora.exception.DuplicateResourceException;
import br.com.selecao.locadora.exception.ResourceNotFoundException;
import br.com.selecao.locadora.repository.CompradorRepository;
import br.com.selecao.locadora.repository.EmpresaRepository;
import br.com.selecao.locadora.repository.LeilaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
// Classe de regras de negocio para Compradores
public class CompradorBO {

    @Autowired
    private CompradorRepository compradorRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private LeilaoRepository leilaoRepository;

    // Retorna a lista de compradores de um leilao
    public List<CompradorResponseDTO> buscarPorLeilao(Long leilaoId) {
        List<Comprador> compradores = compradorRepository.findByIdLeilaoId(leilaoId);
        return compradores.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Cria o vinculo de comprador de uma empresa em um leilao especifico
    public CompradorResponseDTO vincularComprador(CompradorRequestDTO dto) {
        CompradorId idComposto = new CompradorId(dto.getEmpresaId(), dto.getLeilaoId());

        if (compradorRepository.existsById(idComposto)) {
            throw new DuplicateResourceException("Esta empresa já está vinculada como compradora neste leilão.");
        }

        Empresa empresa = empresaRepository.findById(dto.getEmpresaId())
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada para o id: " + dto.getEmpresaId()));

        Leilao leilao = leilaoRepository.findById(dto.getLeilaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Leilão não encontrado para o id: " + dto.getLeilaoId()));

        Comprador comprador = new Comprador(idComposto, empresa, leilao);
        comprador = compradorRepository.save(comprador);

        return converterParaDTO(comprador);
    }

    // Remove o vinculo existente de comprador
    public void desvincularComprador(Long empresaId, Long leilaoId) {
        CompradorId idComposto = new CompradorId(empresaId, leilaoId);
        Comprador comprador = compradorRepository.findById(idComposto)
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo de comprador não encontrado para os IDs informados."));

        compradorRepository.delete(comprador);
    }

    private CompradorResponseDTO converterParaDTO(Comprador comprador) {
        return new CompradorResponseDTO(
                comprador.getEmpresa().getId(),
                comprador.getEmpresa().getRazaoSocial(),
                comprador.getEmpresa().getCnpj(),
                comprador.getLeilao().getId(),
                comprador.getLeilao().getDescricao()
        );
    }
}