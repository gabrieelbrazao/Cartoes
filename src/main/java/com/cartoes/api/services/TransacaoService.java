package com.cartoes.api.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cartoes.api.entities.Cartao;
import com.cartoes.api.entities.Transacao;
import com.cartoes.api.repositories.CartaoRepository;
import com.cartoes.api.repositories.TransacaoRepository;
import com.cartoes.api.utils.ConsistenciaException;

@Service
public class TransacaoService {
	private static final Logger log = LoggerFactory.getLogger(TransacaoService.class);
	
	@Autowired
   	private TransacaoRepository transacaoRepository;
	
	@Autowired
   	private CartaoRepository cartaoRepository;
	
	public Optional<List<Transacao>> buscarPorCartaoNumero(String cartaoNumero) throws ConsistenciaException {
     	log.info("Service: buscando as transações do cartão de número: {}", cartaoNumero);

     	Optional<List<Transacao>> transacoes = Optional.ofNullable(transacaoRepository.findByCartaoNumero(cartaoNumero));

     	if (!transacoes.isPresent() || transacoes.get().size() < 1) {
            log.info("Service: Nenhuma transação encontrada para o cartão de número: {}", cartaoNumero);
            throw new ConsistenciaException("Nenhuma transação encontrada para o cartão de número: {}", cartaoNumero);
     	}

     	return transacoes;
	}
	
	public Transacao salvar(Transacao transacao) throws ConsistenciaException {
     	log.info("Service: salvando a transação: {}", transacao);
     	
     	Optional<Cartao> cartao = Optional.ofNullable(cartaoRepository.findByNumero(transacao.getCartao().getNumero()));
     	
     	if (!cartao.isPresent()) {
            log.info("Service: Nenhum cartão de número: {} foi encontrado", transacao.getCartao().getNumero());
            throw new ConsistenciaException("Nenhum cartão de número: {} foi encontrado", transacao.getCartao().getNumero());
     	}
     	
     	transacao.setCartao(cartaoRepository.findByNumero(transacao.getCartao().getNumero()));
     	
     	if(transacao.getCartao().getBloqueado()) {
     		log.info("Service: Não é possível incluir transações para este cartão, pois o mesmo encontra-se bloqueado");
            throw new ConsistenciaException("Não é possível incluir transações para este cartão, pois o mesmo encontra-se bloqueado");
     	}
     	
     	if (transacao.getId() > 0) {
            log.info("Service: Transações não podem ser alteradas, apenas incluídas");
            throw new ConsistenciaException("Transações não podem ser alteradas, apenas incluídas");
     	}
     	
     	if(transacao.getCartao().getDataValidade().before(new Date())) {
     		log.info("Service: Não é possível incluir transações para este cartão, pois o mesmo encontra-se vencido");
            throw new ConsistenciaException("Não é possível incluir transações para este cartão, pois o mesmo encontra-se vencido");
     	}
     		
     	return transacaoRepository.save(transacao);
	}
}
