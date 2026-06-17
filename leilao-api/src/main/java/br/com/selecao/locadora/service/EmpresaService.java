package br.com.selecao.locadora.service;

import br.com.selecao.locadora.business.EmpresaBO;
import br.com.selecao.locadora.dto.response.EmpresaResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.selecao.locadora.dto.request.EmpresaRequestDTO;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/empresas")
// Endpoints REST para gerenciamento de Empresas
public class EmpresaService {

    @Autowired
    private EmpresaBO empresaBO;

    // Retorna a lista de todas as empresas cadastradas
    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>> buscarTodos() {

        return ResponseEntity.ok(empresaBO.buscarTodos());
    }

    // Busca uma empresa especifica pelo seu ID
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                empresaBO.buscarPorId(id)
        );
    }

    // Insere uma nova empresa no sistema
    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> salvar(
            @Valid @RequestBody EmpresaRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(empresaBO.salvar(dto));
    }

    // Atualiza os dados de uma empresa existente
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpresaRequestDTO dto) {

        return ResponseEntity.ok(
                empresaBO.atualizar(id, dto)
        );
    }

    // Remove a empresa do sistema pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<java.util.Map<String, Object>> deletar(
            @PathVariable Long id) {

        empresaBO.deletar(id);

        java.util.Map<String, Object> resposta = new java.util.HashMap<>();
        resposta.put("id", id);
        resposta.put("mensagem", "Empresa deletada com sucesso!");

        return ResponseEntity.ok(resposta);
    }
}