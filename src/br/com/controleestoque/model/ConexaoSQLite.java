/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque.model;

import java.io.File;
import java.sql.*;

import javax.swing.JOptionPane;
/**
 *
 * @author Luiz
 */
public class ConexaoSQLite {
    
    private Connection con;
    private Statement stm;
    final static String USERPATH = System.getProperty("user.home") + "\\ControleEstoque\\";
    
    public Connection conectarSQLite(){
        try{
            Class.forName("org.sqlite.JDBC");
        
            this.con = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.home") + "\\ControleEstoque\\db\\ControleEstoque.db");
            
        }catch(Exception e){
            System.out.println("Erro ao se conectar no banco de dados");
        }
        
        return con;
    }
    
    public void criarTabela(){
        try {
            this.stm = this.con.createStatement();
            this.stm.execute("CREATE TABLE IF NOT EXISTS pessoas ("
                    +"usuario varchar(70) PRIMARY KEY,"
                    +"senha varchar(25) NOT NULL,"
                    +"email varchar(100) NOT NULL);");
            this.stm.execute("CREATE TABLE IF NOT EXISTS produtos ("
                    +"codigo varchar(70) PRIMARY KEY,"
                    +"descricao varchar(100) NOT NULL,"
                    +"quantEstoque integer NOT NULL,"
                    +"quantMinima integer NOT NULL,"
                    +"valorUnitario real NOT NULL,"
                    +"dataCadastro varchar(25) NOT NULL,"
                    +"dataAlteracao varchar(25) NOT NULL,"
                    +"imgPath varchar(100));");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao criar a tabela: " + ex);
        }
    }
    
    public void iniciarDB(){
        
        try{
            this.conectarSQLite();
            this.criarTabela();
            JOptionPane.showMessageDialog(null, "Conexão estabelecida");
        }catch(Exception e){
            criarPasta();
        }
    }
    
    public void criarPasta(){
        File produtosPasta = new File(USERPATH,"db");
        produtosPasta.mkdirs();
        JOptionPane.showMessageDialog(null, "Pasta para armazenamento do banco de dados criada");
        this.criarDirImg();
        this.iniciarDB();
    }
    
    public void criarDirImg(){
        File imgPasta = new File(USERPATH,"img");
        imgPasta.mkdir();
        JOptionPane.showMessageDialog(null, "Pasta para armazenamento de imagens criado");
    }

}
