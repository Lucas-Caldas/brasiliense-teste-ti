package com.brasiliense.teste.web;

import com.brasiliense.teste.domain.Cidade;
import com.brasiliense.teste.repository.CidadeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cidades")
@Tag(name = "Cidades", description = "Consulta de cidades cadastradas")
public class CidadeController {

    private final CidadeRepository cidadeRepository;

    public CidadeController(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

    @GetMapping
    @Operation(summary = "Listar cidades")
    public List<Cidade> listar() {
        return cidadeRepository.findAll();
    }
}
