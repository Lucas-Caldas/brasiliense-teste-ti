package com.brasiliense.teste.web;

import com.brasiliense.teste.service.RankingService;
import com.brasiliense.teste.web.dto.RankingItemResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RankingController.class)
class RankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RankingService rankingService;

    @Test
    void ranking_deveRetornar200ComItens() throws Exception {
        when(rankingService.ranking(
                eq(LocalDate.of(2011, 1, 1)),
                eq(LocalDate.of(2011, 12, 31))))
                .thenReturn(List.of(new RankingItemResponse(
                        1,
                        "Edifício Esplanada",
                        1,
                        new BigDecimal("100.00"),
                        new BigDecimal("780.00"))));

        mockMvc.perform(get("/api/ranking-condominio")
                        .param("dataInicio", "2011-01-01")
                        .param("dataFim", "2011-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigoApartamento").value(1))
                .andExpect(jsonPath("$[0].totalPago").value(780.00));
    }
}
