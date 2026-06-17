package br.com.selecao.locadora.business;

import br.com.selecao.locadora.dto.request.EmpresaRequestDTO;
import br.com.selecao.locadora.dto.response.EmpresaResponseDTO;
import br.com.selecao.locadora.entity.Empresa;
import br.com.selecao.locadora.exception.DeleteConflictException;
import br.com.selecao.locadora.exception.ResourceNotFoundException;
import br.com.selecao.locadora.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import br.com.selecao.locadora.exception.DuplicateResourceException;

@Service
public class EmpresaBO {

    @Autowired
    private EmpresaRepository empresaRepository;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<EmpresaResponseDTO> buscarTodos() {
        List<Empresa> empresas = empresaRepository.findAll();
        return empresas.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public EmpresaResponseDTO buscarPorId(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada para o id: " + id));
        return converterParaDTO(empresa);
    }

    private EmpresaResponseDTO converterParaDTO(Empresa empresa) {
        return new EmpresaResponseDTO(
                empresa.getId(),
                empresa.getRazaoSocial(),
                empresa.getCnpj(),
                empresa.getEmail(),
                empresa.getTelefone(),
                empresa.getSite(),
                empresa.getCep(),
                empresa.getLogradouro(),
                empresa.getNumero(),
                empresa.getComplemento(),
                empresa.getBairro(),
                empresa.getMunicipio(),
                empresa.getUsuario()
        );
    }

    public EmpresaResponseDTO salvar(EmpresaRequestDTO dto) {
        if (empresaRepository.existsByCnpj(dto.getCnpj())) {
            throw new DuplicateResourceException("Já existe uma empresa com este CNPJ.");
        }

        if (empresaRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Já existe uma empresa com este e-mail.");
        }

        if (empresaRepository.existsByUsuario(dto.getUsuario())) {
            throw new DuplicateResourceException("Já existe uma empresa com este usuário.");
        }

        Empresa empresa = new Empresa();
        preencherDados(empresa, dto);

        // 🔐 CRIPTOGRAFIA REAL NA INCLUSÃO: Transforma a senha limpa em Hash BCrypt irreversível
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
        empresa.setSenha(senhaCriptografada);

        empresa = empresaRepository.save(empresa);
        return converterParaDTO(empresa);
    }

    public EmpresaResponseDTO atualizar(Long id, EmpresaRequestDTO dto) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada para o id: " + id));

        // AJUSTE FINO: Validação para impedir atualização para dados duplicados de outras empresas
        Empresa empresaPorCnpj = empresaRepository.findByCnpj(dto.getCnpj());
        if (empresaPorCnpj != null && !empresaPorCnpj.getId().equals(id)) {
            throw new DuplicateResourceException("Já existe outra empresa cadastrada com este CNPJ.");
        }

        preencherDados(empresa, dto);

        // 🔐 CRIPTOGRAFIA REAL NA ALTERAÇÃO:
        // Só gera um novo Hash se o usuário digitou uma nova senha na tela do Vue.
        // Se veio em branco ou nulo, o método preencherDados já manteve os outros dados e a senha anterior intacta.
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            String novaSenhaCriptografada = passwordEncoder.encode(dto.getSenha());
            empresa.setSenha(novaSenhaCriptografada);
        }

        empresa = empresaRepository.save(empresa);
        return converterParaDTO(empresa);
    }

    public void deletar(Long id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada para o id: " + id));

        try {
            empresaRepository.delete(empresa);
        } catch (Exception ex) {
            throw new DeleteConflictException("Não é possível excluir a empresa pois existem registros vinculados (Leilões ou Compradores).");
        }
    }

    private void preencherDados(Empresa empresa, EmpresaRequestDTO dto) {
        empresa.setRazaoSocial(dto.getRazaoSocial());
        empresa.setCnpj(dto.getCnpj());
        empresa.setLogradouro(dto.getLogradouro());
        empresa.setMunicipio(dto.getMunicipio());
        empresa.setNumero(dto.getNumero());
        empresa.setComplemento(dto.getComplemento());
        empresa.setBairro(dto.getBairro());
        empresa.setCep(dto.getCep());
        empresa.setTelefone(dto.getTelefone());
        empresa.setEmail(dto.getEmail());
        empresa.setSite(dto.getSite());
        empresa.setUsuario(dto.getUsuario());
    }
}