package com.brasiliense.teste.web;

import com.brasiliense.teste.service.RankingService;
import com.brasiliense.teste.web.dto.RankingItemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ranking-condominio")
@Tag(name = "Ranking", description = "Top 3 apartamentos pelo somatório de condomínio no período")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping
    @Operation(summary = "Consultar ranking de condomínio")
    public List<RankingItemResponse> ranking(
            @Parameter(description = "Data início (yyyy-MM-dd)", example = "2011-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data fim (yyyy-MM-dd)", example = "2011-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return rankingService.ranking(dataInicio, dataFim);
    }
}
