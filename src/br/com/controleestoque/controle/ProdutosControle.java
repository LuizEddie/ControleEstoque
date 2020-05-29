/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque.controle;


import br.com.controleestoque.model.Produtos;
import br.com.controleestoque.model.DAO.ProdutosDAO;
import java.util.List;
/**
 *
 * @author Luiz Eduardo
 */
public class ProdutosControle {
   
    public void cadastrarProduto(String codigo, String descricao, Integer quantInicial, Float valorUnitario, String dataCadastro, String sourcePath, Integer quantMinima){
        Produtos produto = new Produtos(codigo, descricao, quantInicial, valorUnitario, dataCadastro, quantMinima);
        produto.setTempPath(sourcePath);
        ProdutosDAO produtoDAO = new ProdutosDAO();
        
        produtoDAO.cadastrar(produto);
    }
    
    public List<Produtos> consultarTudo(){
        ProdutosDAO produtosDAO = new ProdutosDAO();
        
        return produtosDAO.consultaTudo();
    
    }
    
    public Produtos consultarProduto(String codigo){
        ProdutosDAO produtosDAO = new ProdutosDAO();
        
        return produtosDAO.consultarProduto(codigo);
    }
    
    public List<Produtos> consultarProdutoSemCodigo(String codigo){
        ProdutosDAO produtosDAO = new ProdutosDAO();
        
        return produtosDAO.consultarProdutoSemCodigo(codigo);
    }
    
    public void alterarEstoque(String operacao, String codigo, Integer valor, String data){
        ProdutosDAO produtosDAO = new ProdutosDAO();
        
        produtosDAO.alterarEstoque(operacao, codigo, valor, data);
    }
    
    public void excluir(String codigo){
        ProdutosDAO produtosDAO = new ProdutosDAO();
        
        produtosDAO.excluir(codigo);
    }
    
    public void alterar(String descricao, Float valorUnitario, String data, String codigo, String sourcePath, Integer quantMinima){
        Produtos produto = new Produtos();
        ProdutosDAO produtosDAO = new ProdutosDAO();
        produto.setDescricao(descricao);
        produto.setDataAlteracao(data);
        produto.setValorUnitario(valorUnitario);
        produto.setTempPath(sourcePath);
        produto.setQuantMinima(quantMinima);
        
        produtosDAO.alterarProduto(produto, codigo);
    }
    
    public String visualizarImg(){
        ProdutosDAO produtosDAO = new ProdutosDAO();
        
        String sourcePath = produtosDAO.visualizarImagem();
        
        return sourcePath;
    }
    
    public void salvarImagemDaTabela(String nome, byte[] data){
        ProdutosDAO produtosDAO = new ProdutosDAO();
        
        produtosDAO.salvarImagemDaTabela(nome, data);
    }
    
    public void manipularProdutoExcel(String codigo, String descricao, Integer quantInicial, Float valorUnitario, String dataCadastro, String sourcePath, Integer quantMinima){
        Produtos produto = new Produtos(codigo, descricao, quantInicial, valorUnitario, dataCadastro, quantMinima);
        ProdutosDAO produtoDAO = new ProdutosDAO();
        
        produtoDAO.manipularProdutoExcel(produto);
    }
    
    
}
