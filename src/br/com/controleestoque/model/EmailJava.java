/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque.model;

import br.com.controleestoque.model.DAO.UsuarioDAO;
import br.com.controleestoque.model.DAO.ProdutosDAO;
import br.com.controleestoque.visao.ProgressBar;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author Luiz
 */
public class EmailJava {

    public String corpoEmail(String codigo) throws ClassNotFoundException, SQLException {
        ProdutosDAO pDAO = new ProdutosDAO();
        Produtos produto = pDAO.consultaEstoqueBaixo(codigo);
        String mensagem = "Atenção, os seguintes produtos estão com o estoque baixo, "
                + "favor repo-los o mais rápido possível:\n";
        StringBuilder mensagemCompleta = null;
        mensagemCompleta = new StringBuilder(mensagem).append(
                "----------------------------------------------------\n"
                + "Codigo: " + produto.getCodigo() + "\n"
                + "Descrição: " + produto.getDescricao() + "\n"
                + "Quantidade Minima: " + produto.getQuantMinima() + "\n"
                + "Quantidade em Estoque: " + produto.getQuantEstoque() + "\n"
                + "-----------------------------------------------------\n");
        return mensagemCompleta.toString();
    }

    public List<Usuario> getDestinatarios() throws ClassNotFoundException, SQLException {
        UsuarioDAO cadastroDAO = new UsuarioDAO();
        List<Usuario> listaEmail = cadastroDAO.consultarEmail();
        return listaEmail;
    }

    public void enviarEmail(String mensagem, List<Usuario> destinatario) throws EmailException {

        if (destinatario.size() > 0) {
            SimpleEmail email = new SimpleEmail();
            email.setHostName("smtp.gmail.com");
            email.setSSLOnConnect(true);
            email.setSmtpPort(587);
            for (int cont = 0; cont < destinatario.size(); cont++) {
                email.addTo(destinatario.get(cont).getEmail());
            }
            email.setFrom("email", "Controle de Estoque");
            email.setSubject("Estoque Baixo");
            email.setMsg(mensagem);
            email.setAuthentication("email", "senha");
            email.send();
            JOptionPane.showMessageDialog(null, "Email enviado");
        } else {
            JOptionPane.showMessageDialog(null, "Não existem destinatários");
        }
    }

    public void verificarNecessidadeEmail(String codigo) throws NullPointerException, ClassNotFoundException, SQLException, EmailException {
        ProdutosDAO pDAO = new ProdutosDAO();
        Produtos produto = pDAO.consultaEstoqueBaixo(codigo);

        if (produto.getCodigo() != null){
            new ProgressBar(150);
            this.enviarEmail(this.corpoEmail(codigo), this.getDestinatarios());
        }
    }

}
