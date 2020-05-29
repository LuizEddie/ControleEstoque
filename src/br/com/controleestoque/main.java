/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque;
import br.com.controleestoque.model.ConexaoSQLite;
import br.com.controleestoque.visao.Login;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.apache.commons.mail.EmailException;
/**
 *
 * @author Luiz Eduardo
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, EmailException {
        // TODO code application logic here
        ConexaoSQLite conexao = new ConexaoSQLite();
        
        conexao.iniciarDB();
        
        Login login = new Login();
        login.setVisible(true);
        
    }

    
    
}
