package com.brasiliense.teste.service;

import com.brasiliense.teste.web.dto.RankingItemResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @Test
    void ranking_quandoPeriodoInvalido_deveLancarIllegalArgument() {
        RankingService rankingService = new RankingService(
                entityManager, "SELECT * FROM sp_ranking_condominio(:inicio, :termino)");

        assertThatThrownBy(() -> rankingService.ranking(
                LocalDate.of(2011, 12, 31),
                LocalDate.of(2011, 1, 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Data fim");
    }

    @Test
    void ranking_deveChamarProcedureEMapearColunas() {
        RankingService rankingService = new RankingService(
                entityManager, "SELECT * FROM sp_ranking_condominio(:inicio, :termino)");

        when(entityManager.createNativeQuery(contains("sp_ranking_condominio"))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.<Object[]>of(
                new Object[]{1, 1, "Edifício Esplanada", new BigDecimal("100.00"), 1,
                        2, "Campinas", "SP", new BigDecimal("780.00")}
        ));

        List<RankingItemResponse> resultado = rankingService.ranking(
                LocalDate.of(2011, 1, 1),
                LocalDate.of(2011, 12, 31));

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCodigoApartamento()).isEqualTo(1);
        assertThat(resultado.get(0).getNomeEdificio()).isEqualTo("Edifício Esplanada");
        assertThat(resultado.get(0).getAndar()).isEqualTo(1);
        assertThat(resultado.get(0).getMetragem()).isEqualByComparingTo("100.00");
        assertThat(resultado.get(0).getTotalPago()).isEqualByComparingTo("780.00");

        verify(entityManager).createNativeQuery(contains("sp_ranking_condominio"));
    }
}
