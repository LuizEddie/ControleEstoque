/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque.model.DAO;

import br.com.controleestoque.controle.UsuarioControle;
import br.com.controleestoque.model.ConexaoSQLite;
import br.com.controleestoque.model.Usuario;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author Luiz
 */
public class UsuarioDAO {
    
    
    public void cadastrarUsuario(Usuario cadastro){
        try {
            ConexaoSQLite con = new ConexaoSQLite();
            Statement stm = con.conectarSQLite().createStatement();
            byte[] senha = this.encriptar(cadastro.getSenha());
            
            int resposta = stm.executeUpdate("INSERT INTO pessoas VALUES ('" +cadastro.getUsuario()+"',  '" + Arrays.toString(senha)+"', '"+cadastro.getEmail()+"')");
            
            if(resposta>0){
                JOptionPane.showMessageDialog(null, "Cadastro efetuado com sucesso");
            }else{
                JOptionPane.showMessageDialog(null, "Erro ao efetuar cadastro");
            }
            
            stm.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao executar requisição");
        }
    }
    
    
    public void alterarSenha(Usuario alteracao){
        try {
            ConexaoSQLite con = new ConexaoSQLite();
            Statement stm = con.conectarSQLite().createStatement();
            byte[] senha = this.encriptar(alteracao.getSenha());
            
            int alterar = stm.executeUpdate("UPDATE pessoas SET senha = '"+Arrays.toString(senha)+"' WHERE usuario = '"+alteracao.getUsuario()+"'");
            
            if(alterar>0){
                JOptionPane.showMessageDialog(null, "Alteração feita com sucesso");
            }else if(alterar == 0){
                JOptionPane.showMessageDialog(null, "Erro ao alterar senha");
            }
            
            stm.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao executar requisição");
        }
    }
    public byte[] encriptar(String senha1){
       String senha = senha1;
       byte[] messageDigest = null;
       try {
     
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            messageDigest = algorithm.digest(senha.getBytes("UTF-8"));
              
       } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
           JOptionPane.showMessageDialog(null, "Erro ao encriptar senha");
       }
        return messageDigest;
    }
    
    public List<Usuario> consultarEmail(){
        List<Usuario> listaEmail = null;
        try {
            
            
            ConexaoSQLite con = new ConexaoSQLite();
            Statement stm = con.conectarSQLite().createStatement();
            ResultSet rs;
            listaEmail = new ArrayList<>();
            
            rs = stm.executeQuery("SELECT email FROM 'pessoas'");
            
            while(rs.next()){
                Usuario email = new Usuario();
                email.setEmail(rs.getString("email"));
                listaEmail.add(email);
            }
            
          
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao executar requisição");
        }
        return listaEmail;
    }
    
    public void alterarUsuario(Usuario usuario){
        
        try{
            ConexaoSQLite con = new ConexaoSQLite();
            Statement stm = con.conectarSQLite().createStatement();
            
            int update = stm.executeUpdate("UPDATE 'pessoas' SET email='"+usuario.getEmail()+"' WHERE usuario='"+usuario.getUsuario()+"'");
            
            if(update>0){
                if(!usuario.getSenha().equals("")){
                    if(Pattern.matches("\\S+", usuario.getSenha())){
                        alterarSenha(usuario);
                    }else{
                        JOptionPane.showMessageDialog(null, "Senha invalida");
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso");
                }
            }else{
                JOptionPane.showMessageDialog(null, "Erro ao atualizar dados");
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro ao executar requisição");
        }
        
    }
    
    public Usuario pesquisarUsuario(String user){
        System.out.println(user);
        Usuario usuario = null;
        try {
            
            usuario = new Usuario();
            ConexaoSQLite con = new ConexaoSQLite();
            Statement stm = con.conectarSQLite().createStatement();
            ResultSet rs;
            
            rs = stm.executeQuery("SELECT * FROM 'pessoas' WHERE usuario ='"+user+"'");
            
            while(rs.next()){ 
                usuario.setUsuario(rs.getString("usuario"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
            }
            
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao completar requisição");
        }
        
        return usuario;
    }
}
