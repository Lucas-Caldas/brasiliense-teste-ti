package com.brasiliense.teste.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.ArrayList;
import java.util.List;

public class EdificioRequest {

    @NotNull
    private Integer codigo;

    @NotBlank
    private String nome;

    @NotNull
    @Positive
    private Integer andares;

    @NotNull
    private Integer codigoCidade;

    @Valid
    private List<ApartamentoRequest> apartamentos = new ArrayList<>();

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAndares() {
        return andares;
    }

    public void setAndares(Integer andares) {
        this.andares = andares;
    }

    public Integer getCodigoCidade() {
        return codigoCidade;
    }

    public void setCodigoCidade(Integer codigoCidade) {
        this.codigoCidade = codigoCidade;
    }

    public List<ApartamentoRequest> getApartamentos() {
        return apartamentos;
    }

    public void setApartamentos(List<ApartamentoRequest> apartamentos) {
        this.apartamentos = apartamentos;
    }
}
