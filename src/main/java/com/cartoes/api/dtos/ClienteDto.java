package com.cartoes.api.dtos;

public class ClienteDto {
	private String id;
   	private String nome;
   	private String cpf;
   	private String uf;
   	
   	public String getId() {
         	return id;
   	}
   	
   	public void setId(String id) {
         	this.id = id;
   	}
   	
   	public String getNome() {
         	return nome;
   	}
   	
   	public void setNome(String nome) {
         	this.nome = nome;
   	}
   	
   	public String getCpf() {
         	return cpf;
   	}
   	
   	public void setCpf(String cpf) {
         	this.cpf = cpf;
   	}
   	
   	public String getUf() {
         	return uf;
   	}
   	
   	public void setUf(String uf) {
         	this.uf = uf;
   	}
         	
   	@Override
   	public String toString() {
         	return "Cliente[id=" + id + ","
                       	+ "nome=" + nome + ","
                       	+ "cpf=" + cpf + ","
                       	+ "uf=" + uf + "]";
   	}

}
