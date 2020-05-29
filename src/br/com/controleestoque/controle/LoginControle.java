/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque.controle;

import br.com.controleestoque.model.DAO.LoginDAO;
import br.com.controleestoque.model.Login;
/**
 *
 * @author Luiz
 */
public class LoginControle {
    public Boolean login(String usuario, String senha){
        LoginDAO loginDAO = new LoginDAO();
        
        Boolean autorizarLogin = loginDAO.login(usuario, senha);
        
        return autorizarLogin;
    }
    
}
