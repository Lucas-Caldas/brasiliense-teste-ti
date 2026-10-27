package com.brasiliense.teste.repository;

import com.brasiliense.teste.domain.Apartamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApartamentoRepository extends JpaRepository<Apartamento, Integer> {

    List<Apartamento> findByEdificioCodigo(Integer codigoEdificio);

    @Query("""
            select a from Apartamento a
            left join fetch a.edificio e
            left join fetch e.cidade
            left join fetch a.pagamentos
            where a.codigo = :codigo
            """)
    Optional<Apartamento> findByIdComDetalhes(Integer codigo);
}
