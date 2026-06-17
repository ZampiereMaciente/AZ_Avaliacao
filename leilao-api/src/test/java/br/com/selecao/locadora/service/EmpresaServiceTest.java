package br.com.selecao.locadora.service;

import br.com.selecao.locadora.business.EmpresaBO;
import br.com.selecao.locadora.dto.request.EmpresaRequestDTO;
import br.com.selecao.locadora.dto.response.EmpresaResponseDTO;
import br.com.selecao.locadora.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmpresaService.class)
@MockBean(JpaMetamodelMappingContext.class)
@DisplayName("Testes de API REST - EmpresaService (Controller)")
public class EmpresaServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmpresaBO empresaBO;

    @Test
    @DisplayName("POST /empresas - Deve retornar 201 Created quando o JSON for válido")
    void deveRetornar201SeValido() throws Exception {
        EmpresaRequestDTO payloadValido = new EmpresaRequestDTO();
        payloadValido.setRazaoSocial("Locadora Express");
        payloadValido.setCnpj("12345678000199");
        payloadValido.setEmail("express@locadora.com");
        payloadValido.setUsuario("express.owner");
        payloadValido.setSenha("123456");

        EmpresaResponseDTO mockResposta = new EmpresaResponseDTO();
        mockResposta.setId(1L);
        mockResposta.setRazaoSocial("Locadora Express");

        Mockito.when(empresaBO.salvar(any(EmpresaRequestDTO.class))).thenReturn(mockResposta);

        mockMvc.perform(post("/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payloadValido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.razaoSocial").value("Locadora Express"));
    }

    @Test
    @DisplayName("GET /empresas - Deve retornar 200 OK e a lista de empresas")
    void deveRetornarTodasAsEmpresas() throws Exception {
        EmpresaResponseDTO e1 = new EmpresaResponseDTO();
        e1.setId(1L);
        e1.setRazaoSocial("Empresa Um");

        EmpresaResponseDTO e2 = new EmpresaResponseDTO();
        e2.setId(2L);
        e2.setRazaoSocial("Empresa Dois");

        Mockito.when(empresaBO.buscarTodos()).thenReturn(List.of(e1, e2));

        mockMvc.perform(get("/empresas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].razaoSocial").value("Empresa Um"))
                .andExpect(jsonPath("$[1].razaoSocial").value("Empresa Dois"));
    }

    @Test
    @DisplayName("GET /empresas/{id} - Deve retornar 200 OK ao buscar ID existente")
    void deveRetornarEmpresaPorId() throws Exception {
        EmpresaResponseDTO mockResposta = new EmpresaResponseDTO();
        mockResposta.setId(10L);
        mockResposta.setRazaoSocial("Empresa Encontrada");

        Mockito.when(empresaBO.buscarPorId(10L)).thenReturn(mockResposta);

        mockMvc.perform(get("/empresas/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.razaoSocial").value("Empresa Encontrada"));
    }

    @Test
    @DisplayName("GET /empresas/{id} - Deve retornar 404 Not Found se o ID não existir")
    void deveRetornar404AoBuscarIdInexistente() throws Exception {
        Mockito.when(empresaBO.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Empresa não encontrada para o id: 99"));

        mockMvc.perform(get("/empresas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /empresas/{id} - Deve retornar 200 OK ao atualizar os dados")
    void deveAtualizarComSucesso() throws Exception {
        EmpresaRequestDTO payloadAtualizacao = new EmpresaRequestDTO();
        payloadAtualizacao.setRazaoSocial("Nome Atualizado");
        payloadAtualizacao.setCnpj("12345678000199");
        payloadAtualizacao.setEmail("atualizado@locadora.com"); // Adicionado campo obrigatório
        payloadAtualizacao.setUsuario("usuario.atualizado");    // Adicionado campo obrigatório

        EmpresaResponseDTO mockResposta = new EmpresaResponseDTO();
        mockResposta.setId(1L);
        mockResposta.setRazaoSocial("Nome Atualizado");

        Mockito.when(empresaBO.atualizar(eq(1L), any(EmpresaRequestDTO.class))).thenReturn(mockResposta);

        mockMvc.perform(put("/empresas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payloadAtualizacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.razaoSocial").value("Nome Atualizado"));
    }

    @Test
    @DisplayName("DELETE /empresas/{id} - Deve retornar 200 OK e mensagem de sucesso")
    void deveDeletarComSucesso() throws Exception {
        Mockito.doNothing().when(empresaBO).deletar(5L);

        mockMvc.perform(delete("/empresas/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L))
                .andExpect(jsonPath("$.mensagem").value("Empresa deletada com sucesso!"));
    }
}