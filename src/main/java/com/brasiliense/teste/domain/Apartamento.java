package com.brasiliense.teste.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "TABELA_APARTAMENTO")
public class Apartamento {

    @Id
    @Column(name = "CODIGO_APARTAMENTO")
    private Integer codigo;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CODIGO_EDIFICIO", nullable = false)
    private Edificio edificio;

    @Column(name = "METRAGEM", nullable = false, precision = 18, scale = 2)
    private BigDecimal metragem;

    @Column(name = "ANDAR", nullable = false)
    private Integer andar;

    @Column(name = "NUMERO_QUARTOS", nullable = false)
    private Integer numeroQuartos;

    @JsonManagedReference
    @OneToMany(mappedBy = "apartamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PagamentoCondominio> pagamentos = new LinkedHashSet<>();

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Edificio getEdificio() {
        return edificio;
    }

    public void setEdificio(Edificio edificio) {
        this.edificio = edificio;
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

    public Set<PagamentoCondominio> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(Set<PagamentoCondominio> pagamentos) {
        this.pagamentos = pagamentos;
    }

    public void addPagamento(PagamentoCondominio pagamento) {
        pagamentos.add(pagamento);
        pagamento.setApartamento(this);
    }
}
