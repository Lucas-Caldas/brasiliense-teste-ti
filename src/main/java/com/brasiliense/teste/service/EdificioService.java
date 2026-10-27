package com.brasiliense.teste.service;

import com.brasiliense.teste.domain.Apartamento;
import com.brasiliense.teste.domain.Cidade;
import com.brasiliense.teste.domain.Edificio;
import com.brasiliense.teste.domain.PagamentoCondominio;
import com.brasiliense.teste.repository.CidadeRepository;
import com.brasiliense.teste.repository.EdificioRepository;
import com.brasiliense.teste.web.dto.ApartamentoRequest;
import com.brasiliense.teste.web.dto.EdificioRequest;
import com.brasiliense.teste.web.dto.PagamentoRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EdificioService {

    private final EdificioRepository edificioRepository;
    private final CidadeRepository cidadeRepository;

    public EdificioService(EdificioRepository edificioRepository, CidadeRepository cidadeRepository) {
        this.edificioRepository = edificioRepository;
        this.cidadeRepository = cidadeRepository;
    }

    @Transactional(readOnly = true)
    public List<Edificio> listar() {
        return edificioRepository.findAllComDetalhes();
    }

    @Transactional(readOnly = true)
    public Edificio buscar(Integer codigo) {
        return edificioRepository.findByIdComDetalhes(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Edifício não encontrado: " + codigo));
    }

    @Transactional
    public Edificio criar(EdificioRequest request) {
        if (edificioRepository.existsById(request.getCodigo())) {
            throw new IllegalArgumentException("Já existe edifício com código " + request.getCodigo());
        }
        Edificio edificio = new Edificio();
        aplicar(edificio, request);
        return edificioRepository.save(edificio);
    }

    @Transactional
    public Edificio atualizar(Integer codigo, EdificioRequest request) {
        Edificio edificio = buscar(codigo);
        // recria os detalhes (apartamentos/pagamentos) a partir do payload
        edificio.getApartamentos().clear();
        aplicar(edificio, request);
        return edificioRepository.save(edificio);
    }

    @Transactional
    public void excluir(Integer codigo) {
        Edificio edificio = buscar(codigo);
        edificioRepository.delete(edificio);
    }

    private void aplicar(Edificio edificio, EdificioRequest request) {
        Cidade cidade = cidadeRepository.findById(request.getCodigoCidade())
                .orElseThrow(() -> new EntityNotFoundException("Cidade não encontrada: " + request.getCodigoCidade()));

        edificio.setCodigo(request.getCodigo());
        edificio.setNome(request.getNome());
        edificio.setAndares(request.getAndares());
        edificio.setCidade(cidade);

        for (ApartamentoRequest aptoReq : request.getApartamentos()) {
            Apartamento apto = new Apartamento();
            apto.setCodigo(aptoReq.getCodigo());
            apto.setMetragem(aptoReq.getMetragem());
            apto.setAndar(aptoReq.getAndar());
            apto.setNumeroQuartos(aptoReq.getNumeroQuartos());

            for (PagamentoRequest pagReq : aptoReq.getPagamentos()) {
                PagamentoCondominio pag = new PagamentoCondominio();
                pag.setCodigo(pagReq.getCodigo());
                pag.setDataPagamento(pagReq.getDataPagamento());
                pag.setValorPagamento(pagReq.getValorPagamento());
                apto.addPagamento(pag);
            }
            edificio.addApartamento(apto);
        }
    }
}
