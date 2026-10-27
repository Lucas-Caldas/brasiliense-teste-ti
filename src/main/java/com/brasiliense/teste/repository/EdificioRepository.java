package com.brasiliense.teste.repository;

import com.brasiliense.teste.domain.Edificio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EdificioRepository extends JpaRepository<Edificio, Integer> {

    @Query("""
            select distinct e from Edificio e
            left join fetch e.cidade
            left join fetch e.apartamentos
            order by e.codigo
            """)
    List<Edificio> findAllComDetalhes();

    @Query("""
            select e from Edificio e
            left join fetch e.cidade
            left join fetch e.apartamentos
            where e.codigo = :codigo
            """)
    Optional<Edificio> findByIdComDetalhes(Integer codigo);
}
