package com.cartoes.api.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cartoes.api.dtos.TransacaoDto;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.response.Response;
import com.cartoes.api.services.TransacaoService;
import com.cartoes.api.utils.ConsistenciaException;
import com.cartoes.api.utils.ConversaoUtils;

@RestController
@RequestMapping("/api/transacao")
@CrossOrigin(origins = "*")
public class TransacaoController {
	private static final Logger log = LoggerFactory.getLogger(ClienteController.class);
	
	@Autowired
   	private TransacaoService transacaoService;
	
	@GetMapping(value = "/cartao/{cartaoNumero}")
   	public ResponseEntity<Response<List<TransacaoDto>>> buscarNumeroCartao(@PathVariable("cartaoNumero") String cartaoNumero) {
		
		Response<List<TransacaoDto>> response = new Response<List<TransacaoDto>>();
		
        try {
            log.info("Controller: buscando transação pelo número de cartão: {}", cartaoNumero);
 
            Optional<List<Transacao>> transacoes = transacaoService.buscarPorCartaoNumero(cartaoNumero);
 
            response.setDados(ConversaoUtils.ConverterListaDeTransacoes(transacoes.get()));
        	return ResponseEntity.ok(response);
        } catch (ConsistenciaException e) {
            log.info("Controller: Inconsistência de dados: {}", e.getMessage());
            response.adicionarErro("Controller: Inconsistência de dados: {}", e.getMensagem());
        	return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
            response.adicionarErro("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
   	}
	
	@PostMapping
   	public ResponseEntity<Response<TransacaoDto>> salvar(@RequestBody TransacaoDto TransacaoDto) {
		
		Response<TransacaoDto> response = new Response<TransacaoDto>();
		
        try {
        	 log.info("Controller: salvando a transação: {}", TransacaoDto.toString());
             
        	 Transacao transacao = this.transacaoService.salvar(ConversaoUtils.Converter(TransacaoDto));
         	 response.setDados(ConversaoUtils.Converter(transacao));
         	 
         	 return ResponseEntity.ok(response);
        } catch (ConsistenciaException e) {
            log.info("Controller: Inconsistência de dados: {}", e.getMessage());
            response.adicionarErro("Controller: Inconsistência de dados: {}", e.getMensagem());
        	return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            log.error("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
            response.adicionarErro("Controller: Ocorreu um erro na aplicação: {}", e.getMessage());
        	return ResponseEntity.badRequest().body(response);
        }
   	}
}
