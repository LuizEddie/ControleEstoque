/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque.model;

import br.com.controleestoque.model.DAO.UsuarioDAO;
import br.com.controleestoque.model.DAO.ProdutosDAO;
import br.com.controleestoque.visao.ProgressBar;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author Luiz
 */
public class EmailJava {

    public String corpoEmail(String codigo) {
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

    public List<Usuario> getDestinatarios() {
        UsuarioDAO cadastroDAO = new UsuarioDAO();
        List<Usuario> listaEmail = cadastroDAO.consultarEmail();
        return listaEmail;
    }

    public void enviarEmail(String mensagem, List<Usuario> destinatario) {
        String emailRemetente = "emailRemetente@email.com";
        String senha = "senhaRemetente";
        /* Para o email funcionar, caso seja do google, nele deve estar habilitado o acesso
        de apps menos seguros: 
        Link para desabilitar:
        https://myaccount.google.com/u/0/security?hl=pt-BR*/
        
        try {
            
            if (destinatario.size() > 0) {
                SimpleEmail email = new SimpleEmail();
                
                email.setDebug(false);
                 
                email.setHostName("smtp.gmail.com");
                //email.setSslSmtpPort("465");
                
                email.setSmtpPort(587);
                email.setAuthenticator(new DefaultAuthenticator(emailRemetente, senha));
                email.setSSLOnConnect(true);
                email.setStartTLSRequired(true);
                email.setStartTLSEnabled(true);
                try{
                email.setFrom(emailRemetente, "Controle de Estoque");
                email.setSubject("Estoque Baixo");
                email.setMsg(mensagem);
                for (int cont = 0; cont < destinatario.size(); cont++) {
                    email.addTo(destinatario.get(cont).getEmail());
                }
                
                new ProgressBar(150);

                email.send();
              
                JOptionPane.showMessageDialog(null, "Email enviado");
                }catch(Exception e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, e);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Não existem destinatários");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao enviar email: " + ex);
        }
    }

    public void verificarNecessidadeEmail(String codigo) {
        ProdutosDAO pDAO = new ProdutosDAO();
        Produtos produto = pDAO.consultaEstoqueBaixo(codigo);

        if (produto.getCodigo() != null) {
            this.enviarEmail(this.corpoEmail(codigo), this.getDestinatarios());
        }
    }

}
