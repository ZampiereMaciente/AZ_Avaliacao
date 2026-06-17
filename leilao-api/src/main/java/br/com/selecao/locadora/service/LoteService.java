package br.com.selecao.locadora.service;

import br.com.selecao.locadora.business.LoteBO;
import br.com.selecao.locadora.dto.request.LoteRequestDTO;
import br.com.selecao.locadora.dto.response.LoteResponseDTO;
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
@RequestMapping("/lotes")
public class LoteService {

    @Autowired
    private LoteBO loteBO;

    @GetMapping("/leilao/{leilaoId}")
    public ResponseEntity<List<LoteResponseDTO>> buscarPorLeilao(@PathVariable Long leilaoId) {
        return ResponseEntity.ok(loteBO.buscarPorLeilao(leilaoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(loteBO.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<LoteResponseDTO> salvar(@Valid @RequestBody LoteRequestDTO dto) {
        return new ResponseEntity<>(loteBO.salvar(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LoteRequestDTO dto) {
        return ResponseEntity.ok(loteBO.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar(@PathVariable Long id) {
        loteBO.deletar(id);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("id", id);
        resposta.put("mensagem", "Lote deletado com sucesso!");

        return ResponseEntity.ok(resposta);
    }
}