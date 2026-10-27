package com.brasiliense.teste.web.dto;

import java.math.BigDecimal;

public class RankingItemResponse {

    private Integer codigoApartamento;
    private String nomeEdificio;
    private Integer andar;
    private BigDecimal metragem;
    private BigDecimal totalPago;

    public RankingItemResponse(Integer codigoApartamento, String nomeEdificio, Integer andar,
                               BigDecimal metragem, BigDecimal totalPago) {
        this.codigoApartamento = codigoApartamento;
        this.nomeEdificio = nomeEdificio;
        this.andar = andar;
        this.metragem = metragem;
        this.totalPago = totalPago;
    }

    public Integer getCodigoApartamento() {
        return codigoApartamento;
    }

    public String getNomeEdificio() {
        return nomeEdificio;
    }

    public Integer getAndar() {
        return andar;
    }

    public BigDecimal getMetragem() {
        return metragem;
    }

    public BigDecimal getTotalPago() {
        return totalPago;
    }
}
