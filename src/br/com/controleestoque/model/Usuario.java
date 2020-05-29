/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque.model;

/**
 *
 * @author Luiz
 */
public class Usuario {
    private String usuario;
    private String senha;
    private String email;
    
    public Usuario(String usuario, String senha){
        this.usuario = usuario;
        this.senha = senha;
    }
    
    public Usuario(){}
    
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public String getEmail(){
        return this.email;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
}
