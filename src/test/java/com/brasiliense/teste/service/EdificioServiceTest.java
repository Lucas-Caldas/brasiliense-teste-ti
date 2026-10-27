package com.brasiliense.teste.service;

import com.brasiliense.teste.domain.Cidade;
import com.brasiliense.teste.domain.Edificio;
import com.brasiliense.teste.repository.CidadeRepository;
import com.brasiliense.teste.repository.EdificioRepository;
import com.brasiliense.teste.web.dto.ApartamentoRequest;
import com.brasiliense.teste.web.dto.EdificioRequest;
import com.brasiliense.teste.web.dto.PagamentoRequest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EdificioServiceTest {

    @Mock
    private EdificioRepository edificioRepository;

    @Mock
    private CidadeRepository cidadeRepository;

    @InjectMocks
    private EdificioService edificioService;

    private Cidade campinas;

    @BeforeEach
    void setUp() {
        campinas = new Cidade();
        campinas.setCodigo(1);
        campinas.setNome("Campinas");
        campinas.setEstado("SP");
    }

    @Test
    void listar_deveRetornarEdificiosDoRepositorio() {
        Edificio edificio = edificioBasico(1, "Esplanada");
        when(edificioRepository.findAllComDetalhes()).thenReturn(List.of(edificio));

        List<Edificio> resultado = edificioService.listar();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Esplanada");
        verify(edificioRepository).findAllComDetalhes();
    }

    @Test
    void buscar_quandoExiste_deveRetornarEdificio() {
        when(edificioRepository.findByIdComDetalhes(1)).thenReturn(Optional.of(edificioBasico(1, "Esplanada")));

        Edificio resultado = edificioService.buscar(1);

        assertThat(resultado.getCodigo()).isEqualTo(1);
        assertThat(resultado.getNome()).isEqualTo("Esplanada");
    }

    @Test
    void buscar_quandoNaoExiste_deveLancarNotFound() {
        when(edificioRepository.findByIdComDetalhes(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> edificioService.buscar(99))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void criar_quandoCodigoLivre_deveSalvarComApartamentoEPagamento() {
        when(edificioRepository.existsById(10)).thenReturn(false);
        when(cidadeRepository.findById(1)).thenReturn(Optional.of(campinas));
        when(edificioRepository.save(any(Edificio.class))).thenAnswer(inv -> inv.getArgument(0));

        EdificioRequest request = requestCompleto(10, "Novo Edificio");

        Edificio salvo = edificioService.criar(request);

        ArgumentCaptor<Edificio> captor = ArgumentCaptor.forClass(Edificio.class);
        verify(edificioRepository).save(captor.capture());
        Edificio enviado = captor.getValue();

        assertThat(salvo.getCodigo()).isEqualTo(10);
        assertThat(enviado.getCidade().getCodigo()).isEqualTo(1);
        assertThat(enviado.getApartamentos()).hasSize(1);
        assertThat(enviado.getApartamentos().iterator().next().getPagamentos()).hasSize(1);
    }

    @Test
    void criar_quandoCodigoJaExiste_deveLancarIllegalArgument() {
        when(edificioRepository.existsById(1)).thenReturn(true);

        assertThatThrownBy(() -> edificioService.criar(requestCompleto(1, "Duplicado")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Já existe");

        verify(edificioRepository, never()).save(any());
    }

    @Test
    void criar_quandoCidadeNaoExiste_deveLancarNotFound() {
        when(edificioRepository.existsById(10)).thenReturn(false);
        when(cidadeRepository.findById(99)).thenReturn(Optional.empty());

        EdificioRequest request = requestCompleto(10, "Sem cidade");
        request.setCodigoCidade(99);

        assertThatThrownBy(() -> edificioService.criar(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Cidade");
    }

    @Test
    void atualizar_deveLimparERecriarDetalhes() {
        Edificio existente = edificioBasico(1, "Antigo");
        when(edificioRepository.findByIdComDetalhes(1)).thenReturn(Optional.of(existente));
        when(cidadeRepository.findById(1)).thenReturn(Optional.of(campinas));
        when(edificioRepository.save(any(Edificio.class))).thenAnswer(inv -> inv.getArgument(0));

        EdificioRequest request = requestCompleto(1, "Atualizado");
        Edificio atualizado = edificioService.atualizar(1, request);

        assertThat(atualizado.getNome()).isEqualTo("Atualizado");
        assertThat(atualizado.getApartamentos()).hasSize(1);
        verify(edificioRepository).save(existente);
    }

    @Test
    void excluir_quandoExiste_deveChamarDelete() {
        Edificio existente = edificioBasico(1, "Esplanada");
        when(edificioRepository.findByIdComDetalhes(1)).thenReturn(Optional.of(existente));

        edificioService.excluir(1);

        verify(edificioRepository).delete(existente);
    }

    private Edificio edificioBasico(Integer codigo, String nome) {
        Edificio edificio = new Edificio();
        edificio.setCodigo(codigo);
        edificio.setNome(nome);
        edificio.setAndares(5);
        edificio.setCidade(campinas);
        return edificio;
    }

    private EdificioRequest requestCompleto(Integer codigo, String nome) {
        PagamentoRequest pagamento = new PagamentoRequest();
        pagamento.setCodigo(1000);
        pagamento.setDataPagamento(LocalDateTime.of(2011, 10, 10, 0, 0));
        pagamento.setValorPagamento(new BigDecimal("450.00"));

        ApartamentoRequest apto = new ApartamentoRequest();
        apto.setCodigo(500);
        apto.setMetragem(new BigDecimal("100.00"));
        apto.setAndar(1);
        apto.setNumeroQuartos(2);
        apto.setPagamentos(List.of(pagamento));

        EdificioRequest request = new EdificioRequest();
        request.setCodigo(codigo);
        request.setNome(nome);
        request.setAndares(10);
        request.setCodigoCidade(1);
        request.setApartamentos(List.of(apto));
        return request;
    }
}
