package com.cartoes.api.controllers;

import java.util.List;
import java.util.Optional;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.cartoes.api.entities.Cartao;
import com.cartoes.api.response.Response;
import com.cartoes.api.services.CartaoService;
import com.cartoes.api.utils.ConsistenciaException;
 
@RestController
@RequestMapping("/api/cartao")
@CrossOrigin(origins = "*")
public class CartaoController {
 
   	private static final Logger log = LoggerFactory.getLogger(ClienteController.class);
 
   	@Autowired
   	private CartaoService cartaoService;
 
   	/**
   	 * Retorna os cartões do informado no parâmetro
   	 *
   	 * @param Id do cliente a ser consultado
   	 * @return Lista de cartões que o cliente possui
   	 */
   	@GetMapping(value = "/cliente/{clienteId}")
   	public ResponseEntity<Response<List<Cartao>>> buscarPorClienteId(@PathVariable("clienteId") int clienteId) {
 
   			Response<List<Cartao>> response = new Response<List<Cartao>>();
   		
         	try {
 
                	log.info("Controller: buscando cartões do cliente de ID: {}", clienteId);
 
                	Optional<List<Cartao>> listaCartoes = cartaoService.buscarPorClienteId(clienteId);
 
                	response.setDados(listaCartoes.get());
                	 
                	return ResponseEntity.ok(response);
 
         	} catch (ConsistenciaException e) {
                	log.info("Controller: Inconsistência de dados: {}", e.getMessage());
                	response.adicionarErro("Controller: Inconsistência de dados: {}", e.getMensagem());
                	return ResponseEntity.badRequest().body(response);
         	} catch (Exception e) {
                	log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
                	response.adicionarErro("Ocorreu um erro na aplicação: {}", e.getMessage());
                	return ResponseEntity.status(500).body(response);
         	}
 
   	}
 
   	/**
   	 * Persiste um cliente na base.
   	 *
   	 * @param Dados de entrada do cartao
   	 * @return Dados do cartao persistido
   	 */
   	@PostMapping
   	public ResponseEntity<Response<Cartao>> salvar(@RequestBody Cartao cartao) {
   		
   			Response<Cartao> response = new Response<Cartao>();
 
         	try {
 
                	log.info("Controller: salvando o cartao: {}", cartao.toString());
         	
                	response.setDados(this.cartaoService.salvar(cartao));
                	 
                	return ResponseEntity.ok(response);
 
         	} catch (ConsistenciaException e) {
                	log.info("Controller: Inconsistência de dados: {}", e.getMessage());
                	response.adicionarErro(e.getMensagem());
                	return ResponseEntity.badRequest().body(response);
         	} catch (Exception e) {
                	log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
                	response.adicionarErro("Ocorreu um erro na aplicação: {}", e.getMessage());
                	return ResponseEntity.status(500).body(response);
         	}
 
   	}
   	
   	/**
   	 * Exclui um cartão a partir do id informado no parâmtero
   	 * @param id do cartão a ser excluído
   	 * @return Sucesso/erro
   	 */
   	@DeleteMapping(value = "excluir/{id}")
   	public ResponseEntity<Response<String>> excluirPorId(@PathVariable("id") int id){
   		
   			Response<String> response = new Response<String>();
         	
         	try {
 
                	log.info("Controller: excluíndo cartão de ID: {}", id);
 
                	cartaoService.excluirPorId(id);
 
                	response.setDados("Cartao de id: " + id + " excluído com sucesso");
                	 
                	return ResponseEntity.ok(response);
 
         	} catch (ConsistenciaException e) {
                	log.info("Controller: Inconsistência de dados: {}", e.getMessage());
                	response.adicionarErro(e.getMensagem());
                	return ResponseEntity.badRequest().body(response);
         	} catch (Exception e) {
                	log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
                	response.adicionarErro("Ocorreu um erro na aplicação: {}", e.getMessage());
                	return ResponseEntity.status(500).body(response);
         	}
         	
   	}
 
}
