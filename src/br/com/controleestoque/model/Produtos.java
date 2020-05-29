/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque.model;

import java.util.Calendar;



public class Produtos {
   private String codigo;
   private String descricao;
   private Integer quantEstoque;
   private Integer quantMinima;
   private Float valorUnitario;
   private String dataCadastro;
   private String dataAlteracao;
   private String imgPath;
   private String tempPath;
   
   
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
 
    public Integer getQuantMinima(){
        return quantMinima;
    }
    
    public void setQuantMinima(Integer quantMinima){
        this.quantMinima = quantMinima;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantEstoque() {
        return quantEstoque;
    }

    public void setQuantEstoque(Integer quantEstoque) {
        this.quantEstoque = quantEstoque;
    }

    public Float getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Float valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }
    
    public void setDataCadastro(String data){
        this.dataCadastro = data;
    }
    
    public void setDataAlteracao(String data){
        this.dataAlteracao = data;
    }
    
    public String getDataAlteracao(){
        return dataAlteracao;
    }
    
    public String getDataPC(){
        Integer dia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Integer mes = Calendar.getInstance().get(Calendar.MONTH)+1;
        Integer ano = Calendar.getInstance().get(Calendar.YEAR);
        String data = (dia+"/"+mes+"/"+ano);
        
        return data;
    }
    
    public void setImgPath(String imgPath){
        this.imgPath = imgPath;
    }
    
    public String getImgPath(){
        return this.imgPath;
    }
    
    public void setTempPath(String tempPath){
        this.tempPath = tempPath;
    }
    
    public String getTempPath(){
        return this.tempPath;
    }

    public Produtos(String codigo, String descricao, Integer quantEstoque, Float valorUnitario, String data, Integer quantMinima) {
        this.setCodigo(codigo);
        this.setDescricao(descricao);
        this.setQuantEstoque(quantEstoque);
        this.setValorUnitario(valorUnitario);
        this.setDataCadastro(data);
        this.setDataAlteracao(data);
        this.setQuantMinima(quantMinima);
    }
    
    public Produtos(){}

   
   
}
