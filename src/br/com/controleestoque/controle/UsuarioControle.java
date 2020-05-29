/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque.controle;

import br.com.controleestoque.model.Usuario;
import br.com.controleestoque.model.DAO.UsuarioDAO;

/**
 *
 * @author Luiz
 */
public class UsuarioControle {
    
    public void cadastrarUsuario(String usuario, String senha, String email){

            Usuario cadastro = new Usuario();
            cadastro.setUsuario(usuario);
            cadastro.setSenha(senha);
            cadastro.setEmail(email);
            UsuarioDAO cadastroDAO = new UsuarioDAO();
            
            cadastroDAO.cadastrarUsuario(cadastro);
 
    }
    
    public void alterarSenha(String usuario, String senha) {

            Usuario cadastro = new Usuario(usuario, senha);
            UsuarioDAO cadastroDAO = new UsuarioDAO();
            
            cadastroDAO.alterarSenha(cadastro);

    }
    
    public Usuario pesquisarUsuario(String user){
        System.out.println(user);
        UsuarioDAO usuario = new UsuarioDAO();
        
        return usuario.pesquisarUsuario(user);
    }
    
    public void alterarUsuario(String usuario, String email, String senha){
        Usuario alteracao = new Usuario();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        
        alteracao.setEmail(email);
        alteracao.setUsuario(usuario);
        alteracao.setSenha(senha);
        
        usuarioDAO.alterarUsuario(alteracao);
    }
}
