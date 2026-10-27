package com.brasiliense.teste.repository;

import com.brasiliense.teste.domain.PagamentoCondominio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoCondominioRepository extends JpaRepository<PagamentoCondominio, Integer> {

    List<PagamentoCondominio> findByApartamentoCodigo(Integer codigoApartamento);
}
