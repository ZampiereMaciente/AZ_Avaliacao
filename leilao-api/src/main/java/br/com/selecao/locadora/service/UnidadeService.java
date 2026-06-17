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
public class UnidadeService {

    @Autowired
    private UnidadeBO unidadeBO;

    // GET /unidades - Recupera todos os registros
    @GetMapping
    public ResponseEntity<List<UnidadeResponseDTO>> buscarTodos() {
        return new ResponseEntity<>(unidadeBO.buscarTodos(), HttpStatus.OK);
    }

    // GET /unidades/:id - Recupera somente o registro do ID informado (Exercício 3)
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(unidadeBO.buscarPorId(id));
    }

    // POST /unidades - Insere um novo registro (Exercício 3)
    @PostMapping
    public ResponseEntity<UnidadeResponseDTO> salvar(@Valid @RequestBody UnidadeRequestDTO dto) {
        return new ResponseEntity<>(unidadeBO.salvar(dto), HttpStatus.CREATED);
    }

    // PUT /unidades/:id - Altera o registro do ID informado (Exercício 3)
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UnidadeRequestDTO dto) {
        return ResponseEntity.ok(unidadeBO.atualizar(id, dto));
    }

    // DELETE /unidades/:id - Remove o registro do ID informado (Exercício 3)
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar(@PathVariable Long id) {
        unidadeBO.deletar(id);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("id", id);
        resposta.put("mensagem", "Unidade deletada com sucesso!");

        return ResponseEntity.ok(resposta);
    }
}