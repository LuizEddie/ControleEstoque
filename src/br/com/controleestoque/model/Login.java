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
public class Login { 
  
    private static String usuarioAtivo;
   
    public static String getUsuarioAtivo(){
        return usuarioAtivo;
    }
    
    public static void setUsuarioAtivo(String usuarioAtivo){
        Login.usuarioAtivo = usuarioAtivo;
    }
}
