/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque.model.DAO;

import br.com.controleestoque.model.ConexaoSQLite;
import br.com.controleestoque.model.Login;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 *
 * @author Luiz
 */
public class LoginDAO {
    
    public Boolean login(String usuario, String senha1){
        ConexaoSQLite con = new ConexaoSQLite();
        ResultSet rs;
        Boolean autorizarLogin = false;
        try{
            Statement stm = con.conectarSQLite().createStatement();
            byte[] senha = this.encriptar(senha1);
            rs = stm.executeQuery("SELECT * FROM pessoas WHERE usuario = '"+usuario+"' AND senha = '"+Arrays.toString(senha)+"'");
           
            while(rs.next()){
                autorizarLogin = true;
                Login.setUsuarioAtivo(rs.getString("usuario"));
            }
            
            rs.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Usuario ou senha incorreto");
        }
        return autorizarLogin;  
    }
    
    public byte[] encriptar(String senha1){
        String senha = senha1;
        MessageDigest algorithm;
        byte[] messageDigest = null;
        try{
            algorithm = MessageDigest.getInstance("SHA-256");
            messageDigest = algorithm.digest(senha.getBytes("UTF-8"));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Erro na criptografia");
        }
        return messageDigest;
    }
    
}
