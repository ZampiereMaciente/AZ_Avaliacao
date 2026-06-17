package br.com.selecao.locadora.business;

import br.com.selecao.locadora.dto.request.EmpresaRequestDTO;
import br.com.selecao.locadora.dto.response.EmpresaResponseDTO;
import br.com.selecao.locadora.entity.Empresa;
import br.com.selecao.locadora.exception.DuplicateResourceException;
import br.com.selecao.locadora.exception.ResourceNotFoundException;
import br.com.selecao.locadora.repository.EmpresaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // Garante o Rollback automático de tudo para não poluir sua base real
@DisplayName("Testes de Integração - EmpresaBO")
public class EmpresaBOTest {

    @Autowired
    private EmpresaBO empresaBO;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Método auxiliar para gerar DTOs com dados sempre inéditos
    private EmpresaRequestDTO criarDtoComDadosUnicos() {
        String aleatorio = UUID.randomUUID().toString().substring(0, 5);
        String complementoCnpj = String.valueOf(System.currentTimeMillis()).substring(7, 13);
        String cnpjUnico = "12" + complementoCnpj + "000199";

        EmpresaRequestDTO dto = new EmpresaRequestDTO();
        dto.setRazaoSocial("Empresa Teste " + aleatorio);
        dto.setCnpj(cnpjUnico);
        dto.setEmail("teste" + aleatorio + "@provedor.com");
        dto.setUsuario("user." + aleatorio);
        dto.setSenha("senhaSegura123");
        dto.setLogradouro("Rua Teste");
        dto.setMunicipio("Cidade Teste");
        dto.setNumero("123");
        dto.setBairro("Bairro Teste");
        dto.setCep("00000000");
        return dto;
    }

    @Test
    @DisplayName("1. Deve cadastrar uma empresa com sucesso")
    void deveSalvarEmpresaComSucesso() {
        EmpresaRequestDTO dto = criarDtoComDadosUnicos();

        EmpresaResponseDTO resposta = empresaBO.salvar(dto);

        assertNotNull(resposta);
        assertNotNull(resposta.getId());
        assertEquals(dto.getRazaoSocial(), resposta.getRazaoSocial());
    }

    @Test
    @DisplayName("2. Deve criptografar a senha ao salvar nova empresa")
    void deveCriptografarSenhaAoSalvar() {
        EmpresaRequestDTO dto = criarDtoComDadosUnicos();

        EmpresaResponseDTO resposta = empresaBO.salvar(dto);

        // Busca direto no banco para checar a entidade mapeada
        Empresa empresaNoBanco = empresaRepository.findById(resposta.getId()).orElseThrow();

        // A senha no banco NÃO pode ser igual ao texto plano "senhaSegura123"
        assertNotEquals("senhaSegura123", empresaNoBanco.getSenha());
        // O encoder deve confirmar que a hash bate com a senha original
        assertTrue(passwordEncoder.matches("senhaSegura123", empresaNoBanco.getSenha()));
    }

    @Test
    @DisplayName("3. Deve lançar exceção ao tentar cadastrar CNPJ duplicado")
    void deveLancarExcecaoQuandoCnpjDuplicado() {
        EmpresaRequestDTO empresa1 = criarDtoComDadosUnicos();
        empresaBO.salvar(empresa1); // Salva a primeira com sucesso

        // Cria uma segunda empresa, mas força o uso do mesmo CNPJ da primeira
        EmpresaRequestDTO empresa2 = criarDtoComDadosUnicos();
        empresa2.setCnpj(empresa1.getCnpj());

        // Valida se a sua classe de negócios barra jogando a DuplicateResourceException
        assertThrows(DuplicateResourceException.class, () -> {
            empresaBO.salvar(empresa2);
        });
    }

    @Test
    @DisplayName("4. Deve buscar uma empresa por ID com sucesso")
    void deveBuscarEmpresaPorIdComSucesso() {
        EmpresaRequestDTO dto = criarDtoComDadosUnicos();
        EmpresaResponseDTO cadastrada = empresaBO.salvar(dto);

        // Executa a busca pelo ID gerado
        EmpresaResponseDTO encontrada = empresaBO.buscarPorId(cadastrada.getId());

        assertNotNull(encontrada);
        assertEquals(cadastrada.getId(), encontrada.getId());
        assertEquals(dto.getRazaoSocial(), encontrada.getRazaoSocial());
    }

    @Test
    @DisplayName("5. Deve lançar exceção ao buscar ID inexistente")
    void deveLancarExcecaoQuandoIdNaoExiste() {
        // Valida se o sistema retorna erro amigável ao buscar uma empresa fictícia
        assertThrows(ResourceNotFoundException.class, () -> {
            empresaBO.buscarPorId(999999L);
        });
    }
}