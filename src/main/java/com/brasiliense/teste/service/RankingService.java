package com.brasiliense.teste.service;

import com.brasiliense.teste.web.dto.RankingItemResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RankingService {

    private final EntityManager entityManager;
    private final String procedureCall;

    public RankingService(
            EntityManager entityManager,
            @Value("${app.ranking.procedure-call}") String procedureCall) {
        this.entityManager = entityManager;
        this.procedureCall = procedureCall;
    }

    @Transactional(readOnly = true)
    public List<RankingItemResponse> ranking(LocalDate dataInicio, LocalDate dataFim) {
        if (dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("Data fim não pode ser anterior à data início");
        }

        Query query = entityManager.createNativeQuery(procedureCall);
        query.setParameter("inicio", Timestamp.valueOf(dataInicio.atStartOfDay()));
        query.setParameter("termino", Timestamp.valueOf(dataFim.atTime(23, 59, 0)));

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();
        return mapFromStoredProcedure(rows);
    }

    private List<RankingItemResponse> mapFromStoredProcedure(List<Object[]> rows) {
        List<RankingItemResponse> result = new ArrayList<>();
        for (Object[] row : rows) {
            result.add(new RankingItemResponse(
                    ((Number) row[0]).intValue(),
                    (String) row[2],
                    ((Number) row[4]).intValue(),
                    toBigDecimal(row[3]),
                    toBigDecimal(row[8])
            ));
        }
        return result;
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }
        return new BigDecimal(value.toString());
    }
}
