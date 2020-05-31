/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque.visao;

import br.com.controleestoque.controle.ProdutosControle;
import br.com.controleestoque.model.EmailJava;
import br.com.controleestoque.model.Produtos;
import java.awt.Color;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author luiz
 */
public class EntradaSaida extends javax.swing.JFrame {

    Produtos produtos = new Produtos();

    /**
     * Creates new form EntradaSaida
     */
    public EntradaSaida() {
        initComponents();
    }

    public void enviarEmail(String codigo) {
        EmailJava email = new EmailJava();
        email.verificarNecessidadeEmail(codigo);
    }

    public EntradaSaida(String codigo) {

        initComponents();
        this.buscarProduto(codigo);
        this.dataField.setText(produtos.getDataPC());
        this.setPesquisaFieldPlaceholder();
    }

    public void limparCampos() {
        quantAlterarField.setText("");
    }

    public void setPesquisaFieldPlaceholder() {
        buscarField.setText("Digite o codigo do produto");
        buscarField.setForeground(new Color(204, 204, 204));
        buscarField.requestFocus();
    }

    public void buscarProduto(String codigo) {

        ProdutosControle produtosControle = new ProdutosControle();
        Produtos produto = produtosControle.consultarProduto(codigo);
        if (produto.getCodigo() != null) {
            Float valorEstoque = produto.getValorUnitario() * produto.getQuantEstoque();

            codigoField.setText(produto.getCodigo());
            descricaoField.setText(produto.getDescricao());
            quantAtualField.setText(produto.getQuantEstoque().toString());
            valorUnitarioField.setText(produto.getValorUnitario().toString());
            valorEstoqueField.setText(valorEstoque.toString());
            quantMinField.setText(produto.getQuantMinima().toString());
            if (!produto.getImgPath().equals("")) {
                ImageIcon imgIcon = new ImageIcon(produto.getImgPath());
                imgIcon.setImage(imgIcon.getImage().getScaledInstance(300, 354, 100));
                imgLabel.setIcon(imgIcon);
                imgLabel.setText("");
            } else {
                imgLabel.setIcon(null);
                imgLabel.setText("Sem imagem");
            }

            this.addQuantButton.setEnabled(true);
            this.retirarQuantButton.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(null, "Produto não encontrado");
        }
    }

    public void alterarEstoque(String operacao) {
        ProdutosControle produtosControle = new ProdutosControle();

        if (!Pattern.matches("\\d+", quantAlterarField.getText())) {
            JOptionPane.showMessageDialog(null, "Preencha o campo quantidade a ser alterada corretamente");
            this.addQuantButton.setEnabled(true);
            this.retirarQuantButton.setEnabled(true);
        } else {
            if (operacao == "add") {
                produtosControle.alterarEstoque("add", codigoField.getText(), Integer.parseInt(quantAlterarField.getText()), dataField.getText());

                enviarEmail(codigoField.getText());
            } else if (operacao == "retirar") {
                if (Integer.valueOf(quantAtualField.getText()) - Integer.valueOf(quantAlterarField.getText()) >= 0) {
                    produtosControle.alterarEstoque("retirar", codigoField.getText(), Integer.parseInt(quantAlterarField.getText()), dataField.getText());
                    enviarEmail(codigoField.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "A quantidade em estoque não pode ficar menor que zero");
                }

            }
            this.buscarProduto(codigoField.getText());

        }

    }

    /**
     * Creates new form EntradaSaidaFrame
     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        buscarField = new javax.swing.JTextField();
        buscarButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        codigoField = new javax.swing.JTextField();
        descricaoField = new javax.swing.JTextField();
        quantAtualField = new javax.swing.JTextField();
        valorUnitarioField = new javax.swing.JTextField();
        valorEstoqueField = new javax.swing.JTextField();
        quantAlterarField = new javax.swing.JTextField();
        addQuantButton = new javax.swing.JButton();
        retirarQuantButton = new javax.swing.JButton();
        data = new javax.swing.JLabel();
        dataField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        imgLabel = new javax.swing.JLabel();
        quantMinLabel = new javax.swing.JLabel();
        quantMinField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Entrada/Saida");
        setLocation(new java.awt.Point(250, 95));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        buscarField.setToolTipText("Digite o codigo do produto");
        buscarField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                buscarFieldFocusLost(evt);
            }
        });
        buscarField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                buscarFieldKeyPressed(evt);
            }
        });

        buscarButton.setText("Buscar");
        buscarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Codigo");

        jLabel2.setText("Descrição");

        jLabel3.setText("Quantidade Atual");

        jLabel4.setText("Valor Unitário");

        jLabel5.setText("Quantidade a ser Alterada");

        jLabel6.setText("Valor Total em Estoque");

        codigoField.setEditable(false);
        codigoField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                codigoFieldFocusGained(evt);
            }
        });
        codigoField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                codigoFieldPropertyChange(evt);
            }
        });

        descricaoField.setEditable(false);
        descricaoField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descricaoFieldActionPerformed(evt);
            }
        });

        quantAtualField.setEditable(false);

        valorUnitarioField.setEditable(false);

        valorEstoqueField.setEditable(false);

        addQuantButton.setText("Adicionar");
        addQuantButton.setEnabled(false);
        addQuantButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addQuantButtonActionPerformed(evt);
            }
        });

        retirarQuantButton.setText("Retirar");
        retirarQuantButton.setEnabled(false);
        retirarQuantButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                retirarQuantButtonActionPerformed(evt);
            }
        });

        data.setText("Data de Hoje");

        dataField.setEditable(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        imgLabel.setText("Sem Imagem");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        quantMinLabel.setText("Quantidade Minima");

        quantMinField.setEditable(false);
        quantMinField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantMinFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(data, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(quantMinLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dataField)
                            .addComponent(valorEstoqueField)
                            .addComponent(valorUnitarioField)
                            .addComponent(quantAtualField)
                            .addComponent(descricaoField)
                            .addComponent(quantAlterarField)
                            .addComponent(codigoField)
                            .addComponent(quantMinField)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(addQuantButton, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(retirarQuantButton, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(buscarField, javax.swing.GroupLayout.PREFERRED_SIZE, 749, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buscarField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buscarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(codigoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(descricaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(quantMinLabel)
                            .addComponent(quantMinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(quantAtualField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(valorUnitarioField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(valorEstoqueField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(data)
                            .addComponent(dataField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(quantAlterarField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addQuantButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(retirarQuantButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void buscarFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarFieldFocusGained
        // TODO add your handling code here:
        if (buscarField.getText().equals("Digite o codigo do produto")) {
            buscarField.setCaretPosition(0);
            buscarField.setCaretColor(Color.BLACK);
        }
    }//GEN-LAST:event_buscarFieldFocusGained

    private void buscarFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarFieldFocusLost
        // TODO add your handling code here:
        if (buscarField.getText().equals("")) {
            buscarField.setText("Digite o codigo do produto");
            buscarField.setForeground(new Color(204, 204, 204));
        }
    }//GEN-LAST:event_buscarFieldFocusLost

    private void buscarFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarFieldKeyPressed
        // TODO add your handling code here:
        if (buscarField.getText().equals("Digite o codigo do produto")) {
            buscarField.setText("");
            buscarField.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_buscarFieldKeyPressed

    private void buscarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarButtonActionPerformed

        // TODO add your handling code here:
        this.buscarProduto(this.buscarField.getText());

        this.limparCampos();
    }//GEN-LAST:event_buscarButtonActionPerformed

    private void codigoFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_codigoFieldFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoFieldFocusGained

    private void codigoFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_codigoFieldPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoFieldPropertyChange

    private void descricaoFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descricaoFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_descricaoFieldActionPerformed

    private void addQuantButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addQuantButtonActionPerformed

        this.alterarEstoque("add");
        this.limparCampos();

    }//GEN-LAST:event_addQuantButtonActionPerformed

    private void retirarQuantButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_retirarQuantButtonActionPerformed

        // TODO add your handling code here:
        this.alterarEstoque("retirar");
        this.limparCampos();

    }//GEN-LAST:event_retirarQuantButtonActionPerformed

    private void quantMinFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantMinFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantMinFieldActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        new ProgressBar(50);
        Principal principal = new Principal();
        principal.setVisible(true);
    }//GEN-LAST:event_formWindowClosed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EntradaSaida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EntradaSaida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EntradaSaida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EntradaSaida.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EntradaSaida().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addQuantButton;
    private javax.swing.JButton buscarButton;
    private javax.swing.JTextField buscarField;
    private javax.swing.JTextField codigoField;
    private javax.swing.JLabel data;
    private javax.swing.JTextField dataField;
    private javax.swing.JTextField descricaoField;
    private javax.swing.JLabel imgLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField quantAlterarField;
    private javax.swing.JTextField quantAtualField;
    private javax.swing.JTextField quantMinField;
    private javax.swing.JLabel quantMinLabel;
    private javax.swing.JButton retirarQuantButton;
    private javax.swing.JTextField valorEstoqueField;
    private javax.swing.JTextField valorUnitarioField;
    // End of variables declaration//GEN-END:variables
}
