package br.com.selecao.locadora.service;

import br.com.selecao.locadora.business.CompradorBO;
import br.com.selecao.locadora.dto.request.CompradorRequestDTO;
import br.com.selecao.locadora.dto.response.CompradorResponseDTO;
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
@RequestMapping("/compradores")
public class CompradorService {

    @Autowired
    private CompradorBO compradorBO;

    @GetMapping("/leilao/{leilaoId}")
    public ResponseEntity<List<CompradorResponseDTO>> buscarPorLeilao(@PathVariable Long leilaoId) {
        return ResponseEntity.ok(compradorBO.buscarPorLeilao(leilaoId));
    }

    @PostMapping
    public ResponseEntity<CompradorResponseDTO> vincularComprador(@Valid @RequestBody CompradorRequestDTO dto) {
        return new ResponseEntity<>(compradorBO.vincularComprador(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/empresa/{empresaId}/leilao/{leilaoId}")
    public ResponseEntity<Map<String, Object>> desvincularComprador(
            @PathVariable Long empresaId,
            @PathVariable Long leilaoId) {

        compradorBO.desvincularComprador(empresaId, leilaoId);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("empresaId", empresaId);
        resposta.put("leilaoId", leilaoId);
        resposta.put("mensagem", "Comprador desvinculado com sucesso!");

        return ResponseEntity.ok(resposta);
    }
}