package com.brasiliense.teste.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ApartamentoRequest {

    @NotNull
    private Integer codigo;

    @NotNull
    @Positive
    private BigDecimal metragem;

    @NotNull
    private Integer andar;

    @NotNull
    @Positive
    private Integer numeroQuartos;

    @Valid
    private List<PagamentoRequest> pagamentos = new ArrayList<>();

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public BigDecimal getMetragem() {
        return metragem;
    }

    public void setMetragem(BigDecimal metragem) {
        this.metragem = metragem;
    }

    public Integer getAndar() {
        return andar;
    }

    public void setAndar(Integer andar) {
        this.andar = andar;
    }

    public Integer getNumeroQuartos() {
        return numeroQuartos;
    }

    public void setNumeroQuartos(Integer numeroQuartos) {
        this.numeroQuartos = numeroQuartos;
    }

    public List<PagamentoRequest> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<PagamentoRequest> pagamentos) {
        this.pagamentos = pagamentos;
    }
}
