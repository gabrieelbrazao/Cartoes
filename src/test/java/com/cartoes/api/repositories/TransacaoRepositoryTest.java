package com.cartoes.api.repositories;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Cliente;
import com.cartoes.api.entities.Transacao;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TransacaoRepositoryTest {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CartaoRepository cartaoRepository;
	
	@Autowired
	private TransacaoRepository transacaoRepository;
	
	private Cliente clienteTeste;
	private Cartao cartaoTeste;
	private Transacao transacaoTeste;
	
	private void CriarTransacaoTestes() throws ParseException {
		
		clienteTeste = new Cliente();
		clienteTeste.setNome("Nome Teste");
		clienteTeste.setCpf("05887098082");
		clienteTeste.setUf("CE");
		
		cartaoTeste = new Cartao();
		cartaoTeste.setNumero("0588709808286239");
		cartaoTeste.setDataValidade(new SimpleDateFormat("dd/MM/yyyy").parse("01/12/2020"));
		cartaoTeste.setBloqueado(false);
		cartaoTeste.setCliente(new Cliente());
		
		transacaoTeste = new Transacao();
		transacaoTeste.setCnpj("05887098082862");
		transacaoTeste.setValor(15.07);
		transacaoTeste.setQdtParcelas(3);
		transacaoTeste.setJuros(2.05);
	}

	@Before
	public void setUp() throws Exception {
		
		CriarTransacaoTestes();
		
		clienteRepository.save(clienteTeste);
		
		cartaoTeste.setCliente(clienteTeste);
		cartaoRepository.save(cartaoTeste);
		
		transacaoTeste.setCartao(cartaoTeste);
		transacaoRepository.save(transacaoTeste);
		
	}

	@After
	public void tearDown() throws Exception {
		
		clienteRepository.deleteAll();
		cartaoRepository.deleteAll();
		transacaoRepository.deleteAll();
		
	}

	@Test
	public void testFindByCartaoNumero() {
		
		List<Transacao> transacoes = transacaoRepository.findByCartaoNumero(transacaoTeste.getCartao().getNumero());
		
		if(transacoes.size() != 1)
			fail();
		
		Transacao transacao = transacoes.get(0);
		
		assertTrue(transacao.getCartao().getNumero().equals(transacaoTeste.getCartao().getNumero()));
		
	}

}
