package com.cartoes.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.utils.ConsistenciaException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransacaoServiceTest {
	
	@MockBean
	private TransacaoRepository transacaoRepository;
	
	@MockBean
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private TransacaoService transacaoService;
	
	@Test
	public void testbuscarPorCartaoNumeroExistente() throws ConsistenciaException {
		
		ArrayList<Transacao> list = new ArrayList<Transacao>();
		
		list.add(new Transacao());
		
		BDDMockito.given(transacaoRepository.findByCartaoNumero(Mockito.anyString()))
			.willReturn(list);
		
		Optional<List<Transacao>> resultado = transacaoService.buscarPorCartaoNumero("5381579886310193");
		
		assertTrue(resultado.isPresent());
		
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testbuscarPorCartaoNumeroNaoExistente() throws ConsistenciaException {
		
		BDDMockito.given(transacaoRepository.findByCartaoNumero(Mockito.anyString()))
			.willReturn(new ArrayList<Transacao>());
		
		transacaoService.buscarPorCartaoNumero("5381579886310193");
		
	}
	
	@Test
	public void testSalvarComSucesso() throws ConsistenciaException {
		
		Cartao cartao = new Cartao();
		cartao.setDataValidade(new Date( new Date().getTime() + (1000 * 60 * 60 * 24) ));
		
		BDDMockito.given(cartaoRepository.findByNumero(null))
			.willReturn(cartao);
		
		Transacao transacao = new Transacao();
		transacao.setCartao(new Cartao());
		
		BDDMockito.given(transacaoRepository.save(Mockito.any(Transacao.class)))
			.willReturn(transacao);
		
		Transacao resultado = transacaoService.salvar(transacao);
		
		assertNotNull(resultado);
		
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarCartaoNaoEncontrado() throws ConsistenciaException {
		
		BDDMockito.given(cartaoRepository.findByNumero(Mockito.anyString()))
		.willReturn(null);
		
		Cartao cartao = new Cartao();
		cartao.setNumero("5381579886310193");
		
		Transacao transacao = new Transacao();
		transacao.setCartao(cartao);
		
		transacaoService.salvar(transacao);
		
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarCartaoBloqueado() throws ConsistenciaException {
		
		Cartao cartao = new Cartao();
		cartao.setNumero("5381579886310193");
		cartao.setBloqueado(true);
		
		BDDMockito.given(cartaoRepository.findByNumero(Mockito.anyString()))
		.willReturn(cartao);
		
		Transacao transacao = new Transacao();
		transacao.setCartao(cartao);
		
		transacaoService.salvar(transacao);
		
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarTransacaoExistente() throws ConsistenciaException {
		
		Cartao cartao = new Cartao();
		cartao.setNumero("5381579886310193");
		
		BDDMockito.given(cartaoRepository.findByNumero(Mockito.anyString()))
		.willReturn(cartao);
		
		Transacao transacao = new Transacao();
		transacao.setCartao(cartao);
		transacao.setId(1);
		
		transacaoService.salvar(transacao);
		
	}
	
	@Test(expected = ConsistenciaException.class)
	public void testSalvarCartaoVencido() throws ConsistenciaException {
		
		Cartao cartao = new Cartao();
		cartao.setNumero("5381579886310193");
		cartao.setDataValidade(new Date( new Date().getTime() - (1000 * 60 * 60 * 24) ));
		
		BDDMockito.given(cartaoRepository.findByNumero(Mockito.anyString()))
		.willReturn(cartao);
		
		Transacao transacao = new Transacao();
		transacao.setCartao(cartao);
		
		transacaoService.salvar(transacao);
		
	}

}
