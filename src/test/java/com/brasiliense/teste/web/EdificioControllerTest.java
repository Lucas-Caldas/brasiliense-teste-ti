package com.brasiliense.teste.web;

import com.brasiliense.teste.domain.Cidade;
import com.brasiliense.teste.domain.Edificio;
import com.brasiliense.teste.service.EdificioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EdificioController.class)
class EdificioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EdificioService edificioService;

    @Test
    void listar_deveRetornar200() throws Exception {
        when(edificioService.listar()).thenReturn(List.of(edificio(1, "Esplanada")));

        mockMvc.perform(get("/api/edificios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value(1))
                .andExpect(jsonPath("$[0].nome").value("Esplanada"));
    }

    @Test
    void buscar_deveRetornar200() throws Exception {
        when(edificioService.buscar(1)).thenReturn(edificio(1, "Esplanada"));

        mockMvc.perform(get("/api/edificios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Esplanada"));
    }

    @Test
    void criar_deveRetornar201() throws Exception {
        when(edificioService.criar(any())).thenReturn(edificio(10, "Novo"));

        String body = objectMapper.writeValueAsString(Map.of(
                "codigo", 10,
                "nome", "Novo",
                "andares", 5,
                "codigoCidade", 1,
                "apartamentos", List.of()
        ));

        mockMvc.perform(post("/api/edificios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.codigo").value(10));
    }

    @Test
    void atualizar_deveRetornar200() throws Exception {
        when(edificioService.atualizar(eq(1), any())).thenReturn(edificio(1, "Atualizado"));

        String body = objectMapper.writeValueAsString(Map.of(
                "codigo", 1,
                "nome", "Atualizado",
                "andares", 5,
                "codigoCidade", 1,
                "apartamentos", List.of()
        ));

        mockMvc.perform(put("/api/edificios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Atualizado"));
    }

    @Test
    void excluir_deveRetornar204() throws Exception {
        doNothing().when(edificioService).excluir(1);

        mockMvc.perform(delete("/api/edificios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void criar_quandoPayloadInvalido_deveRetornar400() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "codigo", 10,
                "nome", "",
                "andares", 5,
                "codigoCidade", 1
        ));

        mockMvc.perform(post("/api/edificios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    private Edificio edificio(Integer codigo, String nome) {
        Cidade cidade = new Cidade();
        cidade.setCodigo(1);
        cidade.setNome("Campinas");
        cidade.setEstado("SP");

        Edificio edificio = new Edificio();
        edificio.setCodigo(codigo);
        edificio.setNome(nome);
        edificio.setAndares(5);
        edificio.setCidade(cidade);
        return edificio;
    }
}
