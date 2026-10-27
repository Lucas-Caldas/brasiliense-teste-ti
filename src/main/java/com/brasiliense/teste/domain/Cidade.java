package com.brasiliense.teste.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TABELA_CIDADE")
public class Cidade {

    @Id
    @Column(name = "CODIGO_CIDADE")
    private Integer codigo;

    @Column(name = "NOME_CIDADE", nullable = false, length = 50)
    private String nome;

    @Column(name = "ESTADO", nullable = false, length = 2)
    private String estado;

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
