package com.brasiliense.teste.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "TABELA_EDIFICIO")
public class Edificio {

    @Id
    @Column(name = "CODIGO_EDIFICIO")
    private Integer codigo;

    @Column(name = "NOME_EDIFICIO", nullable = false, length = 50)
    private String nome;

    @Column(name = "ANDARES", nullable = false)
    private Integer andares;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CODIGO_CIDADE", nullable = false)
    private Cidade cidade;

    @JsonManagedReference
    @OneToMany(mappedBy = "edificio", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Apartamento> apartamentos = new LinkedHashSet<>();

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

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Set<Apartamento> getApartamentos() {
        return apartamentos;
    }

    public void setApartamentos(Set<Apartamento> apartamentos) {
        this.apartamentos = apartamentos;
    }

    public void addApartamento(Apartamento apartamento) {
        apartamentos.add(apartamento);
        apartamento.setEdificio(this);
    }
}
