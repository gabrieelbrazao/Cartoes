package com.cartoes.api.dtos;

public class CartaoDto {
	private String id;
   	private String numero;
   	private String dataValidade;
   	private String bloqueado;
   	private String clienteId;
   	
   	public String getId() {
         	return id;
   	}
   	
   	public void setId(String id) {
         	this.id = id;
   	}
   	
   	public String getNumero() {
         	return numero;
   	}
   	
   	public void setNumero(String numero) {
         	this.numero = numero;
   	}
   	
   	public String getDataValidade() {
         	return dataValidade;
   	}
   	
   	public void setDataValidade(String dataValidade) {
         	this.dataValidade = dataValidade;
   	}
   	
   	public String getBloqueado() {
         	return bloqueado;
   	}
   	
   	public void setBloqueado(String bloqueado) {
         	this.bloqueado = bloqueado;
   	}
   	
   	public String getClienteId() {
         	return clienteId;
   	}
   	
   	public void setClienteId(String clienteId) {
         	this.clienteId = clienteId;
   	}
         	
   	@Override
   	public String toString() {
         	return "Cartao[id=" + id + ","
                       	+ "numero=" + numero + ","
                       	+ "dataValidade=" + dataValidade + ","
                       	+ "bloqueado=" + bloqueado + ","
                       	+ "clienteId=" + clienteId + "]";
   	}
}
