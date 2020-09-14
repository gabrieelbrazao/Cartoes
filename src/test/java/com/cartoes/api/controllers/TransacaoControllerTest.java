package com.cartoes.api.controllers;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cartoes.api.dtos.TransacaoDto;
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.services.TransacaoService;
import com.cartoes.api.utils.ConsistenciaException;
import com.cartoes.api.utils.ConversaoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransacaoControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private TransacaoService transacaoService;
	
	private Transacao criarTransacaoTestes() throws ParseException {
		
		Transacao transacao = new Transacao();
		
		transacao.setCartao(new Cartao());
		transacao.setCnpj("18808626000194");
		transacao.setJuros(5.00);
		transacao.setQdtParcelas(3);
		transacao.setValor(40.00);
		transacao.setDataTransacao(new SimpleDateFormat("dd/MM/yyyy").parse("13/09/2020"));
		
		return transacao;
		
	}
	
	@Test
	@WithMockUser
	public void testBuscarNumeroCartaoSucesso() throws Exception {
		
		Transacao transacao = criarTransacaoTestes();
		
		ArrayList<Transacao> list = new ArrayList<Transacao>();
		list.add(transacao);

		BDDMockito.given(transacaoService.buscarPorCartaoNumero(Mockito.anyString()))
			.willReturn(Optional.of(list));

		mvc.perform(MockMvcRequestBuilders.get("/api/transacao/cartao/5381579886310193")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.dados[0].id").value(transacao.getId()))
			.andExpect(jsonPath("$.dados[0].cartaoId").value(transacao.getCartao().getId()))
			.andExpect(jsonPath("$.dados[0].cnpj").value(transacao.getCnpj()))
			.andExpect(jsonPath("$.dados[0].juros").value(transacao.getJuros()))
			.andExpect(jsonPath("$.dados[0].qdtParcelas").value(transacao.getQdtParcelas()))
			.andExpect(jsonPath("$.dados[0].valor").value(transacao.getValor()))
			.andExpect(jsonPath("$.erros").isEmpty());
			
	}
	
	@Test
	@WithMockUser
	public void testBuscarNumeroCartaoInconsistencia() throws Exception {

		BDDMockito.given(transacaoService.buscarPorCartaoNumero(Mockito.anyString()))
			.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.get("/api/transacao/cartao/5381579886310193")
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros").value("Controller: Inconsistência de dados: Teste inconsistência"));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarSucesso() throws Exception {
		
		Transacao transacao = criarTransacaoTestes();
		
		TransacaoDto objEntrada = ConversaoUtils.Converter(transacao);
		objEntrada.setDataTransacao("13/09/2020");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(transacaoService.salvar(Mockito.any(Transacao.class)))
			.willReturn(transacao);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.dados.id").value(transacao.getId()))
			.andExpect(jsonPath("$.dados.cartaoId").value(transacao.getCartao().getId()))
			.andExpect(jsonPath("$.dados.cnpj").value(transacao.getCnpj()))
			.andExpect(jsonPath("$.dados.juros").value(transacao.getJuros()))
			.andExpect(jsonPath("$.dados.qdtParcelas").value(transacao.getQdtParcelas()))
			.andExpect(jsonPath("$.dados.valor").value(transacao.getValor()))
			.andExpect(jsonPath("$.erros").isEmpty());
			
	}

	@Test
	@WithMockUser
	public void testSalvarInconsistencia() throws Exception {
		
		Transacao transacao = criarTransacaoTestes();
		
		TransacaoDto objEntrada = ConversaoUtils.Converter(transacao);
		objEntrada.setDataTransacao("13/09/2020");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		BDDMockito.given(transacaoService.salvar(Mockito.any(Transacao.class)))
			.willThrow(new ConsistenciaException("Teste inconsistência"));

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros").value("Controller: Inconsistência de dados: Teste inconsistência"));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarCnpjInexistente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setValor("40.00");
		objEntrada.setQdtParcelas("3");
		objEntrada.setJuros("3.00");
		objEntrada.setCartaoId("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros").value("CNPJ não pode ser vazio."));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarCnpjInsuficiente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setCnpj("1880862600019");
		objEntrada.setValor("40.00");
		objEntrada.setQdtParcelas("3");
		objEntrada.setJuros("3.00");
		objEntrada.setCartaoId("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros", hasItem("CNPJ deve conter 14 caracteres.")))
			.andExpect(jsonPath("$.erros", hasItem("CNPJ inválido.")));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarCnpjExcedente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setCnpj("188086260001941");
		objEntrada.setValor("40.00");
		objEntrada.setQdtParcelas("3");
		objEntrada.setJuros("3.00");
		objEntrada.setCartaoId("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros", hasItem("CNPJ deve conter 14 caracteres.")))
			.andExpect(jsonPath("$.erros", hasItem("CNPJ inválido.")));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarCnpjInvalido() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setCnpj("1880862600019a");
		objEntrada.setValor("40.00");
		objEntrada.setQdtParcelas("3");
		objEntrada.setJuros("3.00");
		objEntrada.setCartaoId("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros").value("CNPJ inválido."));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarValorInexistente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setCnpj("18808626000194");
		objEntrada.setQdtParcelas("3");
		objEntrada.setJuros("3.00");
		objEntrada.setCartaoId("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros").value("Valor não pode ser vazio."));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarValorInsuficiente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setCnpj("18808626000194");
		objEntrada.setValor("");
		objEntrada.setQdtParcelas("3");
		objEntrada.setJuros("3.00");
		objEntrada.setCartaoId("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros", hasItem("Valor deve conter até 10 caracteres.")))
			.andExpect(jsonPath("$.erros", hasItem("Valor não pode ser vazio.")));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarValorExcedente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setCnpj("18808626000194");
		objEntrada.setValor("12345678910");
		objEntrada.setQdtParcelas("3");
		objEntrada.setJuros("3.00");
		objEntrada.setCartaoId("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros").value("Valor deve conter até 10 caracteres."));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarQdtParcelasInexistente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setCnpj("18808626000194");
		objEntrada.setValor("40.00");
		objEntrada.setJuros("3.00");
		objEntrada.setCartaoId("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros").value("Quantidade de parcelas não pode ser vazio."));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarQdtParcelasInsuficiente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setCnpj("18808626000194");
		objEntrada.setValor("40.00");
		objEntrada.setQdtParcelas("");
		objEntrada.setJuros("3.00");
		objEntrada.setCartaoId("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros", hasItem("Quantidade de parcelas não pode ser vazio.")))
			.andExpect(jsonPath("$.erros", hasItem("Quantidade de parcelas deve conter até 2 caracteres.")));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarQdtParcelasExcedente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setCnpj("18808626000194");
		objEntrada.setValor("40.00");
		objEntrada.setQdtParcelas("100");
		objEntrada.setJuros("3.00");
		objEntrada.setCartaoId("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros").value("Quantidade de parcelas deve conter até 2 caracteres."));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarJurosInexistente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setCnpj("18808626000194");
		objEntrada.setValor("40.00");
		objEntrada.setQdtParcelas("3");
		objEntrada.setCartaoId("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros").value("Juros não pode ser vazio."));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarJurosInsuficiente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setCnpj("18808626000194");
		objEntrada.setValor("40.00");
		objEntrada.setQdtParcelas("3");
		objEntrada.setJuros("");
		objEntrada.setCartaoId("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros", hasItem("Juros não pode ser vazio.")))
			.andExpect(jsonPath("$.erros", hasItem("Juros deve conter até 4 caracteres.")));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarJurosExcedente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setCnpj("18808626000194");
		objEntrada.setValor("40.00");
		objEntrada.setQdtParcelas("3");
		objEntrada.setJuros("12345");
		objEntrada.setCartaoId("1");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros").value("Juros deve conter até 4 caracteres."));
			
	}
	
	@Test
	@WithMockUser
	public void testSalvarCartaoIdInexistente() throws Exception {
		
		TransacaoDto objEntrada = new TransacaoDto();
		objEntrada.setDataTransacao("13/09/2020");
		objEntrada.setCnpj("18808626000194");
		objEntrada.setValor("40.00");
		objEntrada.setQdtParcelas("3");
		objEntrada.setJuros("3.00");
		
		String json = new ObjectMapper().writeValueAsString(objEntrada);

		mvc.perform(MockMvcRequestBuilders.post("/api/transacao")
			.content(json)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.dados").isEmpty())
			.andExpect(jsonPath("$.erros").value("O ID do cartão não pode ser vazio."));
			
	}

}
