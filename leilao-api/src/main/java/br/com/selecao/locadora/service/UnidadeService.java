package br.com.selecao.locadora.service;

import br.com.selecao.locadora.business.UnidadeBO;
import br.com.selecao.locadora.dto.request.UnidadeRequestDTO;
import br.com.selecao.locadora.dto.response.UnidadeResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "/unidades")
// Endpoints REST para gerenciamento de Unidades
public class UnidadeService {

    @Autowired
    private UnidadeBO unidadeBO;

    // GET /unidades - Retorna a lista de todas as unidades cadastradas
    @GetMapping
    public ResponseEntity<List<UnidadeResponseDTO>> buscarTodos() {
        return new ResponseEntity<>(unidadeBO.buscarTodos(), HttpStatus.OK);
    }

    // GET /unidades/:id - Busca uma unidade especifica pelo seu ID
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(unidadeBO.buscarPorId(id));
    }

    // POST /unidades - Cadastra uma nova unidade
    @PostMapping
    public ResponseEntity<UnidadeResponseDTO> salvar(@Valid @RequestBody UnidadeRequestDTO dto) {
        return new ResponseEntity<>(unidadeBO.salvar(dto), HttpStatus.CREATED);
    }

    // PUT /unidades/:id - Atualiza os dados de uma unidade existente
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UnidadeRequestDTO dto) {
        return ResponseEntity.ok(unidadeBO.atualizar(id, dto));
    }

    // DELETE /unidades/:id - Remove uma unidade do sistema pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar(@PathVariable Long id) {
        unidadeBO.deletar(id);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("id", id);
        resposta.put("mensagem", "Unidade deletada com sucesso!");

        return ResponseEntity.ok(resposta);
    }
}