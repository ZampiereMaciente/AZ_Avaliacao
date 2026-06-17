package br.com.selecao.locadora.service;

import br.com.selecao.locadora.business.LeilaoBO;
import br.com.selecao.locadora.dto.request.LeilaoRequestDTO;
import br.com.selecao.locadora.dto.response.LeilaoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/leiloes")
// Endpoints REST para gerenciamento de Leiloes
public class LeilaoService {

    @Autowired
    private LeilaoBO leilaoBO;

    // Retorna todos os leiloes cadastrados
    @GetMapping
    public ResponseEntity<List<LeilaoResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(leilaoBO.buscarTodos());
    }

    // Busca um leilao especifico pelo seu ID
    @GetMapping("/{id}")
    public ResponseEntity<LeilaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(leilaoBO.buscarPorId(id));
    }

    // Cadastra um novo leilao
    @PostMapping
    public ResponseEntity<LeilaoResponseDTO> salvar(@Valid @RequestBody LeilaoRequestDTO dto) {
        return new ResponseEntity<>(leilaoBO.salvar(dto), HttpStatus.CREATED);
    }

    // Atualiza os dados de um leilao existente
    @PutMapping("/{id}")
    public ResponseEntity<LeilaoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LeilaoRequestDTO dto) {
        return ResponseEntity.ok(leilaoBO.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<java.util.Map<String, Object>> deletar(@PathVariable Long id) {
        leilaoBO.deletar(id);

        // Criamos o mapa usando Object para aceitar tanto Strings quanto numeros
        java.util.Map<String, Object> resposta = new java.util.HashMap<>();
        resposta.put("id", id);
        resposta.put("mensagem", "Leilão deletado com sucesso!");

        return ResponseEntity.ok(resposta);
    }
}