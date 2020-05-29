/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque.model.DAO;

import br.com.controleestoque.model.Produtos;
import br.com.controleestoque.model.ConexaoSQLite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


import org.apache.commons.io.FileUtils;

/**
 *
 * @author Luiz Eduardo
 */
public class ProdutosDAO {
    final static String IMGPATH = System.getProperty("user.home") + "\\ControleEstoque\\img\\";
   
    public void mensagemPane(String mensagem){
        JOptionPane.showMessageDialog(null, mensagem);
    }
    /* 
    public void cadastrar(Produtos p) throws IOException, FileNotFoundException, Exception{
        ConexaoJSON conJSON = new ConexaoJSON();
        
        JSONArray arrProdutos = new JSONArray(conJSON.lerJSON());
        JSONObject jsonObject = new JSONObject();
        Boolean cadastrado = false;
        
            for(int cont = 0; cont <arrProdutos.length(); cont++){
                JSONObject produtoJSON = (JSONObject) arrProdutos.get(cont);
            
                if(produtoJSON.getString("codigo").equals(p.getCodigo())){
                    JOptionPane.showMessageDialog(null, "O codigo ja esta cadastrado");
                    cadastrado = true;
                    break;
                }else{
                    cadastrado = false;
                }
            }
        
        if(cadastrado == false){
            jsonObject.put("codigo", p.getCodigo());
            jsonObject.put("descricao", p.getDescricao());
            jsonObject.put("quantEstoque", p.getQuantEstoque());
            jsonObject.put("valorUnitario", p.getValorUnitario());
            jsonObject.put("dataCadastro", p.getDataCadastro());
            jsonObject.put("dataUltimaAlteracao", p.getDataAlteracao());
            
            if(!p.getTempPath().equals("")){
                p.setImgPath(this.salvarImg(p.getCodigo(), p.getTempPath()));
            }else{
                p.setImgPath(p.getTempPath());
            }
            
            jsonObject.put("imgPath", p.getImgPath());
            
            arrProdutos.put(jsonObject);
                
            conJSON.escreverJSON(arrProdutos.toString());
            
            
            JOptionPane.showMessageDialog(null, "Produto cadastrado");
            
        }
    }
    
    public List<Produtos> consultaTudo() throws FileNotFoundException, IOException, JSONException, Exception{
        ConexaoJSON conJSON = new ConexaoJSON();
        
        JSONArray arrProdutos = new JSONArray(conJSON.lerJSON());
        
        List<Produtos> listaProdutos = new ArrayList<>();
       
        for(int i=0; i< arrProdutos.length(); i++){
            JSONObject prodJson = (JSONObject) arrProdutos.get(i);
            Produtos produto = new Produtos();
            produto.setCodigo(prodJson.getString("codigo"));
            produto.setDescricao(prodJson.getString("descricao"));
            produto.setQuantEstoque(prodJson.getInt("quantEstoque"));
            produto.setValorUnitario(prodJson.getFloat("valorUnitario"));
            produto.setDataAlteracao(prodJson.getString("dataUltimaAlteracao"));
            try{
                produto.setImgPath(prodJson.getString("imgPath"));
            }catch(JSONException e){
                produto.setImgPath("");
            }
            
            
            listaProdutos.add(produto);
        }
       
        return listaProdutos;
    }
    
    public Produtos consultarProduto(String codigo) throws FileNotFoundException, IOException, Exception{
        ConexaoJSON conJSON = new ConexaoJSON();
        Produtos produto = null;
        JSONArray arrProdutos = new JSONArray(conJSON.lerJSON());
        for(int cont=0; cont < arrProdutos.length(); cont++){
            JSONObject prodJSON = (JSONObject) arrProdutos.get(cont);
            
            if(prodJSON.getString("codigo").equals(codigo)){
                produto = new Produtos();
                produto.setCodigo(prodJSON.getString("codigo"));
                produto.setDescricao(prodJSON.getString("descricao"));
                produto.setQuantEstoque(prodJSON.getInt("quantEstoque"));
                produto.setValorUnitario(prodJSON.getFloat("valorUnitario"));
                produto.setDataAlteracao(prodJSON.getString("dataUltimaAlteracao"));
                produto.setDataCadastro(prodJSON.getString("dataCadastro"));
                try{
                    produto.setImgPath(prodJSON.getString("imgPath"));
                }catch(JSONException e){
                    produto.setImgPath("");
                }
                break;
            }
        }
        
        return produto;
    }
    
    public void alterarEstoque(String operacao, String codigo, Integer quant, String dataAlteracao) throws IOException, Exception{
        ConexaoJSON conJSON = new ConexaoJSON();
        
        JSONArray arrProdutos = new JSONArray(conJSON.lerJSON());
        
        for(int cont = 0; cont < arrProdutos.length(); cont++){
        JSONObject prodJSON = (JSONObject)arrProdutos.get(cont);
        
            if(prodJSON.get("codigo").equals(codigo)){
                
                if(quant <= 0){
                    JOptionPane.showMessageDialog(null, "A quantidade não pode ser Zero ou Negativa");
                }else{
                    if(operacao == "add"){
                        prodJSON.put("quantEstoque", (prodJSON.getInt("quantEstoque") + quant));
                        JOptionPane.showMessageDialog(null, "Quantidade adicionada com sucesso");
                    }else if(operacao == "retirar"){
                        if((prodJSON.getInt("quantEstoque") - quant)<0){
                            JOptionPane.showMessageDialog(null, "A quantidade não pode resultar em menos que zero");
                            break;
                        }else{
                            prodJSON.put("quantEstoque", (prodJSON.getInt("quantEstoque") - quant));
                            JOptionPane.showMessageDialog(null, "Quantidade retirada com sucesso");
                        }
                    }
                }
                
                prodJSON.put("dataUltimaAlteracao", dataAlteracao);
                conJSON.escreverJSON(arrProdutos.toString());
                break;
            }
        }
        
    }
    
    public void excluir(String codigo) throws IOException, Exception{
        ConexaoJSON conJSON = new ConexaoJSON();
        JSONArray arrProdutos = new JSONArray(conJSON.lerJSON());
        
        for(int cont = 0; cont < arrProdutos.length(); cont++){
            JSONObject prodJSON = (JSONObject) arrProdutos.get(cont);
            
            if(prodJSON.get("codigo").equals(codigo)){
                this.excluirImg(prodJSON.getString("imgPath"));
                arrProdutos.remove(cont);
                conJSON.escreverJSON(arrProdutos.toString());
                JOptionPane.showMessageDialog(null, "Produto excluido");
                break;
            }
        }    
    }
    
    public void alterarProduto(Produtos p, String codigo) throws IOException, Exception{
        ConexaoJSON conJSON = new ConexaoJSON();
        JSONArray arrProdutos = new JSONArray(conJSON.lerJSON());
        
        for(int cont = 0; cont < arrProdutos.length(); cont++){
            JSONObject prodJSON = (JSONObject) arrProdutos.get(cont);
            
            if(prodJSON.get("codigo").equals(codigo)){
               prodJSON.put("descricao", p.getDescricao());
               prodJSON.put("valorUnitario", p.getValorUnitario());
               prodJSON.put("dataUltimaAlteracao", p.getDataAlteracao());
               
               if(!p.getTempPath().equals("")){
                   p.setImgPath(this.salvarImg(codigo, p.getTempPath()));
               }else{
                   p.setImgPath(p.getTempPath());
               }              
               
               prodJSON.put("imgPath", p.getImgPath());
               
               conJSON.escreverJSON(arrProdutos.toString());
               JOptionPane.showMessageDialog(null, "Dados alterados com sucesso");
               break;
            }
        }
    }*/
    
/*-------------------------------------Manipulação de Imagem-------------------------------------------------------------*/
    public String visualizarImagem(){
        
        String imgPath = "";
        try{
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Imagens JPEG ou JPG", "jpg", "jpeg"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            int i = fileChooser.showOpenDialog(null);
        
            File arquivoImg = fileChooser.getSelectedFile();
            imgPath = arquivoImg.getAbsolutePath();
            
        }catch(Exception e){
            System.out.println(imgPath);
            mensagemPane("Operação cancelada");
        }
        
        return imgPath;
    }
    
    public String salvarImg(String codigo, String sourcePath){
        String savePath = "";
        try{
            if(!sourcePath.equals("") && !sourcePath.equals(null)){
                File source = new File(sourcePath);
                File dest = new File(IMGPATH,codigo+".jpg");
               
                if(dest.exists()){
                    if(!sourcePath.equals(dest.getPath())){  
                        int resposta = JOptionPane.showConfirmDialog(null, "Já existe uma imagem, deseja substituir?","Substituir",JOptionPane.YES_NO_OPTION);
            
                        if(resposta == JOptionPane.YES_OPTION){
                       
                            FileUtils.copyFile(source, dest);
                            JOptionPane.showMessageDialog(null, "Imagem salva");
                            savePath = dest.getPath();
                        }else if(resposta == JOptionPane.NO_OPTION){
                            JOptionPane.showMessageDialog(null, "A imagem não foi salva");
                        }
                    }else if(sourcePath.equals("") || sourcePath.equals(dest.getPath())){
                        savePath = sourcePath;
                    }
                }else{
                    FileUtils.copyFile(source, dest);
                    JOptionPane.showMessageDialog(null, "Imagem salva");
                    savePath = dest.getPath();
                }
            }
        }catch(Exception e){
            mensagemPane("Erro ao salvar imagem "  + e);
        }
        
        return savePath;
    }
    
    public void salvarImagemDaTabela(String nome, byte[] data){
        String path = IMGPATH+nome+".jpg";
        try(OutputStream out = new FileOutputStream(path)){
            out.write(data);
            this.salvarImgPathDaTabela(nome, path);
        } catch (Exception ex) {
            mensagemPane("Erro ao salvar imagem " + ex);
        }
    }   
    
    public void excluirImg(String path){
        File imgPath = new File(path);
        imgPath.delete();
    }
    /*---------------------------------------------SQLite--------------------------------------------*/
    
    public Connection definirConexao(){
        Connection con = null;
        try {
            ConexaoSQLite conSQL = new ConexaoSQLite();
            con = conSQL.conectarSQLite();
            
        } catch (Exception ex) {
            mensagemPane("Erro na conexão com o banco de dados " + ex);
        }
        return con;
    }
    
    public void cadastrar(Produtos p){
        System.out.println(p.getTempPath());
        try{
            Statement stm = this.definirConexao().createStatement();
            
            Produtos pro = consultarProduto(p.getCodigo());
            if(pro.getCodigo() == null){
                if(!p.getTempPath().isEmpty()){
                    p.setImgPath(this.salvarImg(p.getCodigo(), p.getTempPath()));
                }else{
                    p.setImgPath("");
                }
            }
                        
            int insert = stm.executeUpdate("INSERT INTO 'produtos' (codigo, descricao, quantEstoque, quantMinima, valorUnitario, dataCadastro, dataAlteracao, imgPath)"
                + " VALUES( '"+p.getCodigo()+"','"+p.getDescricao()+"', "+p.getQuantEstoque()+", "+p.getQuantMinima()+", "+p.getValorUnitario()+", '"+p.getDataCadastro()+"', '"+p.getDataPC()+"', '"+p.getImgPath()+"');");                 
            
            if(insert>0){
                mensagemPane("Produto cadastrado com sucesso");
            }else{
                mensagemPane("Já existe um produto com esse codigo");
            }
            
        
            stm.close();
            
            
        }catch(Exception e){
            mensagemPane("Já existe um produto com este codigo");
        }
     
    }
    
    public void alterarProduto(Produtos p, String codigo){
        
        try{
            
            Statement stm = this.definirConexao().createStatement(); 
            
                System.out.println(p.getTempPath());
                if(!p.getTempPath().equals("") && p.getTempPath() != null){
                    p.setImgPath(this.salvarImg(codigo, p.getTempPath()));
                }else if(p.getTempPath()==""){
                    p.setImgPath("");
                }
                 
        
            int update = stm.executeUpdate("UPDATE 'produtos' SET descricao='"+p.getDescricao()+"', valorUnitario="+p.getValorUnitario()+", "
            + "dataAlteracao='"+p.getDataPC()+"', imgPath='"+p.getImgPath()+"', quantMinima="+p.getQuantMinima()+" WHERE codigo = '"+codigo+"';");
            
            if(update>0){
                mensagemPane("Produto alterado com sucesso");
            }else{
                mensagemPane("Erro ao alterar produto");
            }
            
            stm.close();        
            
        }catch(Exception e){
            e.printStackTrace();
            mensagemPane("Erro ao alterar dados do produto " + e);
        }
        
    }
    
    public void excluir(String codigo){
        
        try{
            Statement stm = this.definirConexao().createStatement();
            String imgPath = this.consultarImgPath(codigo);
        
            this.excluirImg(imgPath);
        
            stm.executeUpdate("DELETE FROM 'produtos' WHERE codigo = '"+codigo+"';");
        
            stm.close();
            
            mensagemPane("Produto excluido com sucesso");
        }catch(Exception e){
            mensagemPane("Erro ao excluir o produto ");
        }
    
    }
    
    public String consultarImgPath(String codigo){

        String imgPath = "";
        try{
            Statement stm = this.definirConexao().createStatement();
            ResultSet rs;        
            rs = stm.executeQuery("SELECT imgPath FROM 'produtos' WHERE codigo = '"+codigo+"';");
        
            while(rs.next()){
                imgPath = rs.getString("imgPath");
            }
            rs.close();
        }catch(Exception e){
            mensagemPane("Erro ao consultar produto " + e);
        }
        return imgPath;
    }
    
    public void salvarImgPathDaTabela(String codigo, String imgPath){
        
        try{
            Statement stm = this.definirConexao().createStatement();
        
            stm.executeUpdate("UPDATE 'produtos' SET imgPath = '"+imgPath+"' WHERE codigo='"+codigo+"'");
            stm.close();
        }catch(Exception e){
            mensagemPane("Erro ao salvar imagem" + e);
        }
            
    }

    public List<Produtos> consultarProdutoSemCodigo(String pesquisa){
        List<Produtos> listaProdutos = null;
        
        try{
            listaProdutos = new ArrayList<>();
            Statement stm = this.definirConexao().createStatement();
            ResultSet rs;
            rs = stm.executeQuery("SELECT * FROM 'produtos' WHERE codigo='"+pesquisa+"' OR descricao LIKE '%"+pesquisa+"%';");
        
            while(rs.next()){
                Produtos produto = new Produtos();
                produto.setCodigo(rs.getString("codigo"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setQuantEstoque(rs.getInt("quantEstoque"));
                produto.setQuantMinima(rs.getInt("quantMinima"));
                produto.setValorUnitario(rs.getFloat("valorUnitario"));
                produto.setDataCadastro(rs.getString("dataCadastro"));
                produto.setDataAlteracao(rs.getString("dataAlteracao"));
                produto.setImgPath(rs.getString("imgPath"));
                listaProdutos.add(produto);
            }
        
            rs.close();
            mensagemPane("Consulta realizada");
        }catch(Exception e){
            mensagemPane("Erro ao consultar produto " + e);
        }
        
        return listaProdutos;
    }
    
    public Produtos consultarProduto(String pesquisa){
        Produtos produto = null;
        
        try{
            produto = new Produtos();
            Statement stm = this.definirConexao().createStatement();
            ResultSet rs;
            rs = stm.executeQuery("SELECT * FROM 'produtos' WHERE codigo='"+pesquisa+"';");
        
            while(rs.next()){
                produto.setCodigo(rs.getString("codigo"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setQuantEstoque(rs.getInt("quantEstoque"));
                produto.setQuantMinima(rs.getInt("quantMinima"));
                produto.setValorUnitario(rs.getFloat("valorUnitario"));
                produto.setDataCadastro(rs.getString("dataCadastro"));
                produto.setDataAlteracao(rs.getString("dataAlteracao"));
                produto.setImgPath(rs.getString("imgPath"));
            }   
        
            rs.close();
        }catch(Exception e){
            mensagemPane("Erro ao consultar produto " + e);
        }
        
        return produto;
    }
    
    public List<Produtos> consultaTudo(){
        List<Produtos> listaProdutos = null;
        
        try{
            Statement stm = this.definirConexao().createStatement();
            listaProdutos = new ArrayList<>();
            ResultSet rs;
        
            rs = stm.executeQuery("SELECT * FROM 'produtos'");
        
            while(rs.next()){
                Produtos produto = new Produtos();
                produto.setCodigo(rs.getString("codigo"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setQuantEstoque(rs.getInt("quantEstoque"));
                produto.setQuantMinima(rs.getInt("quantMinima"));
                produto.setValorUnitario(rs.getFloat("valorUnitario"));
                produto.setDataCadastro(rs.getString("dataCadastro"));
                produto.setDataAlteracao(rs.getString("dataAlteracao"));
                produto.setImgPath(rs.getString("imgPath"));
            
                listaProdutos.add(produto);
            }
        }catch(Exception e){
            mensagemPane("Erro ao consultar produtos " + e);
        }
        
        return listaProdutos;
    }
    
    public void alterarEstoque(String operacao, String codigo, Integer quant, String dataAlteracao) {
        try{
            Statement stm = this.definirConexao().createStatement();
            String operacaoAritmetica = null;
            if(quant <= 0){
                JOptionPane.showMessageDialog(null, "A quantidade não pode ser igual ou inferior a 0(Zero)");
            }else{
                if(operacao.equals("add")){
                    operacaoAritmetica = "+";
                }else if(operacao.equals("retirar")){
                    operacaoAritmetica = "-";
                }
                stm.executeUpdate("UPDATE 'produtos' SET quantEstoque = ((SELECT quantEstoque FROM 'produtos' WHERE codigo='"+codigo+"') "+operacaoAritmetica+" "
                        + ""+quant+"), dataAlteracao = '"+dataAlteracao+"' WHERE codigo='"+codigo+"';");
                
                stm.close();
                mensagemPane("Estoque alterado com sucesso");
            }
        }catch(Exception e){
            mensagemPane("Erro ao alterar estoque" + e);
        }
        
    }
    
    public Produtos consultaEstoqueBaixo(String codigo) {
        Produtos produto = null;
        try {
            Statement stm = this.definirConexao().createStatement();
            ResultSet rs;
            produto = new Produtos();
            rs = stm.executeQuery("SELECT codigo, descricao, quantMinima, quantEstoque FROM 'produtos'"
                    + "WHERE codigo='"+codigo+"' AND quantMinima > quantEstoque");
            
            while(rs.next()){
                produto.setCodigo(rs.getString("codigo"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setQuantMinima(rs.getInt("quantMinima"));
                produto.setQuantEstoque(rs.getInt("quantEstoque"));
            }
        } catch (Exception ex) {
            mensagemPane("Erro ao consultar estoque " + ex);        
        }
        return produto;
    }
    
    public void manipularProdutoExcel(Produtos p){
        try {
            Statement stm = this.definirConexao().createStatement();
            
            p.setImgPath("");
            stm.executeUpdate("INSERT INTO 'produtos' (codigo, descricao, quantEstoque, quantMinima, valorUnitario, dataCadastro, dataAlteracao, imgPath)"
                    + " VALUES( '"+p.getCodigo()+"','"+p.getDescricao()+"', "+p.getQuantEstoque()+", "+p.getQuantMinima()+", "+p.getValorUnitario()+", '"+p.getDataCadastro()+"', '"+p.getDataPC()+"', '"+p.getImgPath()+"');");
            
            stm.close();       
        } catch (Exception ex) {
            try {
                int resposta = JOptionPane.showConfirmDialog(null, "Já existe um produto com esse código, deseja sobreescrever seus dados?", "Sobreescrever?", JOptionPane.YES_NO_OPTION);
                
                Statement stm = this.definirConexao().createStatement();
                
                if(resposta == JOptionPane.YES_OPTION){
                    stm.executeUpdate("UPDATE 'produtos' SET descricao='"+p.getDescricao()+"', quantEstoque="+p.getQuantEstoque()+", quantMinima="+p.getQuantMinima()+", valorUnitario="+p.getValorUnitario()+", dataCadastro='"+p.getDataCadastro()+"', dataAlteracao='"+p.getDataPC()+"' WHERE codigo = '"+p.getCodigo()+"'");
                    stm.close();    
                }
                    
            } catch (Exception ex1) {
                mensagemPane("Erro ao alterar produto " + ex1);            
            }
        }
    }
    
    
}
