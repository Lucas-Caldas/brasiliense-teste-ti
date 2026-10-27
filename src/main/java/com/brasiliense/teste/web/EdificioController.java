package com.brasiliense.teste.web;

import com.brasiliense.teste.domain.Edificio;
import com.brasiliense.teste.service.EdificioService;
import com.brasiliense.teste.web.dto.EdificioRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/edificios")
@Tag(name = "Edifícios", description = "CRUD com detalhe de edifício, apartamentos e pagamentos")
public class EdificioController {

    private final EdificioService edificioService;

    public EdificioController(EdificioService edificioService) {
        this.edificioService = edificioService;
    }

    @GetMapping
    @Operation(summary = "Listar edifícios com apartamentos e pagamentos")
    public List<Edificio> listar() {
        return edificioService.listar();
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Buscar edifício por código")
    public Edificio buscar(@PathVariable Integer codigo) {
        return edificioService.buscar(codigo);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar edifício com detalhe (apartamentos e pagamentos)")
    public Edificio criar(@Valid @RequestBody EdificioRequest request) {
        return edificioService.criar(request);
    }

    @PutMapping("/{codigo}")
    @Operation(summary = "Atualizar edifício e seus detalhes")
    public Edificio atualizar(@PathVariable Integer codigo, @Valid @RequestBody EdificioRequest request) {
        return edificioService.atualizar(codigo, request);
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir edifício")
    public void excluir(@PathVariable Integer codigo) {
        edificioService.excluir(codigo);
    }
}
