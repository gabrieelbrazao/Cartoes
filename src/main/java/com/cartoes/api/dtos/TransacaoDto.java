package com.cartoes.api.dtos;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

public class TransacaoDto {
	private String id;
	private String dataTransacao;
	
	@NotEmpty(message = "CNPJ não pode ser vazio.")
	@Length(min = 14, max = 14, message = "CNPJ deve conter 14 caracteres.")
	@CNPJ(message = "CNPJ inválido.")
	private String cnpj;
	
	@NotEmpty(message = "Valor não pode ser vazio.")
	@Length(min = 1, max = 10, message = "Valor deve conter até 10 caracteres.")
	private String valor;
	
	@NotEmpty(message = "Quantidade de parcelas não pode ser vazio.")
	@Length(min = 1, max = 2, message = "Quantidade de parcelas deve conter até 2 caracteres.")
	private String qdtParcelas;
	
	@NotEmpty(message = "Juros não pode ser vazio.")
	@Length(min = 1, max = 4, message = "Juros deve conter até 4 caracteres.")
	private String juros;
	
	@NotEmpty(message = "O ID do cartão não pode ser vazio.")
	private String cartaoId;
	
	public String getId() {
     	return id;
	}
	
	public void setId(String id) {
     	this.id = id;
	}
	
	public String getDataTransacao() {
     	return dataTransacao;
	}
	
	public void setDataTransacao(String dataTransacao) {
     	this.dataTransacao = dataTransacao;
	}
	
	public String getCnpj() {
     	return cnpj;
	}
	
	public void setCnpj(String cnpj) {
     	this.cnpj = cnpj;
	}
	
	public String getValor() {
     	return valor;
	}
	
	public void setValor(String valor) {
     	this.valor = valor;
	}
	
	public String getQdtParcelas() {
     	return qdtParcelas;
	}
	
	public void setQdtParcelas(String qdtParcelas) {
     	this.qdtParcelas = qdtParcelas;
	}
	
	public String getJuros() {
     	return juros;
	}
	
	public void setJuros(String juros) {
     	this.juros = juros;
	}
	
	public String getCartaoId() {
     	return cartaoId;
	}
	
	public void setCartaoId(String cartaoId) {
     	this.cartaoId = cartaoId;
	}
	
	@Override
   	public String toString() {
		return "Transação[id=" + id + ","
			+ "dataTransacao=" + dataTransacao + ","
            + "cnpj=" + cnpj + ","
            + "valor=" + valor + ","
            + "qdtParcelas=" + qdtParcelas + ","
			+ "juros=" + juros + ","
			+ "cartaoId=" + cartaoId + "]";
   	}
}
