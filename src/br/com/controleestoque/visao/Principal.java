/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.controleestoque.visao;

import br.com.controleestoque.controle.LoginControle;
import br.com.controleestoque.controle.ProdutosControle;
import br.com.controleestoque.model.Produtos;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Luiz
 */
public class Principal extends javax.swing.JFrame {
    DefaultTableModel model = null;
    Produtos produtos = new Produtos();
    private String sourcePath = "";
    
    /**
     * Creates new form NewJFrame
     */
    public Principal() {
        initComponents();
        if(jTabbedPane2.getSelectedIndex() == 0){
            this.setPesquisaFieldPlaceholder();
        
            this.atualizarTabela();
            dataCadastroField.setText(produtos.getDataPC());
            this.limparCampos();
           
        }
    }
//-------------------------------------------------Referente ao cadastro------------------------
        public void limparCampos(){
        codigoField.setText("");
        descricaoField.setText("");
        quantInicialField.setText("");
        quantMinField.setText("");
        valorUniField.setText("");
        imgLabel.setIcon(null);
        imgLabel.setText("Sem imagem");
        sourcePath="";
    }

//-----------------------------------------Referente a consulta-----------------------------
    
    public void setPesquisaFieldPlaceholder(){
        pesquisaField.setText("Digite o codigo ou descrição do produto");
        pesquisaField.setForeground(new Color(204,204,204));
        pesquisaField.requestFocus();
    }
    
    public void removerLinhas(){
        model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col){
                return false;
            }

            @Override
            public Class getColumnClass(int i) {
                return getValueAt(0, i).getClass(); //To change body of generated methods, choose Tools | Templates.
            }
            
        };
        
        model.addColumn("Codigo");
        model.addColumn("Descrição");
        model.addColumn("Quantidade Mínima");
        model.addColumn("Quantidade Estoque");
        model.addColumn("Valor Unitario");
        model.addColumn("Valor total em estoque");
        model.addColumn("Ultima Alteração");
        model.addColumn("Imagem");

        produtosTabel.setRowHeight(100);
        produtosTabel.setModel(model);
        /*produtosTabel.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
                Integer minimo = (Integer) model.getValueAt(row, 2);
                Integer estoque = (Integer) model.getValueAt(row, 3);
                
                if(isSelected){
                    setBackground(new Color(75,110,175));
                }else{
                    if ( minimo > estoque){
                        setBackground(Color.red.brighter());
                    }else if(minimo.equals(estoque)){
                        setBackground(Color.white);
                    }else if(minimo < estoque){
                        setBackground(Color.green.brighter());
                    }
                }
                
                return this;//To change body of generated methods, choose Tools | Templates.
            }
            
        });*/
        this.mudarCor();
    }

    public void mudarCor(){
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
                Integer minimo = (Integer) model.getValueAt(row, 2);
                Integer estoque = (Integer) model.getValueAt(row, 3);
                
                if(isSelected){
                    setBackground(new Color(75,110,175,95));
                }else{
                    if ( minimo > estoque){
                        setBackground(new Color(255,102,102));
                    }else if(minimo.equals(estoque)){
                        setBackground(new Color(255,255,102));
                    }else if(minimo < estoque){
                        setBackground(new Color(102,255,102));
                    }
                } 
                return this;//To change body of generated methods, choose Tools | Templates.
            }
        };
        
        produtosTabel.getColumnModel().getColumn(0).setCellRenderer(renderer);
        produtosTabel.getColumnModel().getColumn(1).setCellRenderer(renderer);
        produtosTabel.getColumnModel().getColumn(2).setCellRenderer(renderer);
        produtosTabel.getColumnModel().getColumn(3).setCellRenderer(renderer);
        produtosTabel.getColumnModel().getColumn(4).setCellRenderer(renderer);
        produtosTabel.getColumnModel().getColumn(5).setCellRenderer(renderer);
        produtosTabel.getColumnModel().getColumn(6).setCellRenderer(renderer);
    }
    
    public void atualizarTabela(){
              
        this.removerLinhas();
        
        ProdutosControle produtoControle = new ProdutosControle();
        List<Produtos> listaProdutos;
        ImageIcon imgIcon;
        
            listaProdutos = produtoControle.consultarTudo();
            for(int cont = 0; cont < listaProdutos.size(); cont++){
                imgIcon = new ImageIcon(listaProdutos.get(cont).getImgPath());
                imgIcon.setImage(imgIcon.getImage().getScaledInstance(95, 95, 100));
                model.addRow(new Object[]{
                    listaProdutos.get(cont).getCodigo(),
                    listaProdutos.get(cont).getDescricao(),
                    listaProdutos.get(cont).getQuantMinima(),
                    listaProdutos.get(cont).getQuantEstoque(),
                    listaProdutos.get(cont).getValorUnitario(),
                    (listaProdutos.get(cont).getQuantEstoque() * listaProdutos.get(cont).getValorUnitario()),
                    listaProdutos.get(cont).getDataAlteracao(),
                    imgIcon
                });
            }
       
    }
    
    public void preencherTabelaConsulta(String codigo){
        this.removerLinhas();
        
        ProdutosControle produtosControle = new ProdutosControle();
        ImageIcon imgIcon;
        
       
            List<Produtos> produtos = produtosControle.consultarProdutoSemCodigo(codigo);
            
            if(produtos.size() > 0){
                for (int i = 0; i <produtos.size(); i++){
                
                
                    imgIcon = new ImageIcon(produtos.get(i).getImgPath());
                    imgIcon.setImage(imgIcon.getImage().getScaledInstance(95, 95, 100));
            
                    model.addRow(new Object[]{
                        produtos.get(i).getCodigo(),
                        produtos.get(i).getDescricao(),
                        produtos.get(i).getQuantMinima(),
                        produtos.get(i).getQuantEstoque(),
                        produtos.get(i).getValorUnitario(),
                        (produtos.get(i).getValorUnitario() * produtos.get(i).getQuantEstoque()),
                        produtos.get(i).getDataAlteracao(),
                        imgIcon
                    });
                }
            }else{
                JOptionPane.showMessageDialog(null, "Produto não encontrado");
                this.atualizarTabela();
            }
      
       
    }
    
    public void excluir(){
        ProdutosControle produtosControle = new ProdutosControle();
        
        produtosControle.excluir(produtosTabel.getValueAt(produtosTabel.getSelectedRow(), 0).toString());
    }
    
/*-------------------------------Tabelas---------------------------------------------*/
    public void exportar(){
        JFileChooser filechooser = new JFileChooser();
        String[] escolhas = {"Tabela atual", "Modelo"};
        int tipoTabela = JOptionPane.showOptionDialog(null, "Deseja exportar a tabela atual ou um modelo?", "Exportar", JOptionPane.WARNING_MESSAGE, 0, null, escolhas, escolhas[0]);
        
        if(tipoTabela != JOptionPane.CLOSED_OPTION){
            int opcoes = filechooser.showSaveDialog(this.produtosTabel);
        
            if(opcoes == JFileChooser.APPROVE_OPTION){
                String nomeArquivo = filechooser.getSelectedFile().getName();
                String caminho = filechooser.getSelectedFile().getParentFile().getPath();
            
                int tamanho = nomeArquivo.length();
                String extensao = "";
                String arquivo = "";
            
                if(tamanho > 4){
                    extensao = nomeArquivo.substring(tamanho - 4, tamanho);
                }
            
                if(extensao.equals(".xls")){
                    arquivo = caminho + "\\" + nomeArquivo;
                }else{
                    arquivo = caminho + "\\" + nomeArquivo + ".xls";
                }
            
                if(tipoTabela == 0){
                    paraExcelAOI(produtosTabel, new File(arquivo));
                }else if(tipoTabela == 1){
                    exportarModelo(new File(arquivo));
                }
                    
            }
        }
    }
    
    public void paraExcelAOI(JTable tabela, File file){
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheetProdutos = workBook.createSheet("Produtos");
        String[] titulos = {"Codigo", "Descrição", "Quantidade Minima", "Quantidade em Estoque", "Valor Unitário", "Valor total em estoque", "Ultima Alteração","Imagem"};
        
        HSSFFont fonte = workBook.createFont();
        fonte.setBold(true);
        fonte.setFontHeight((short)400);
        CellStyle estiloCelula = workBook.createCellStyle();
        estiloCelula.setFont(fonte);
        
        Row rowTitulos = sheetProdutos.createRow(0);
        for(int cont= 0; cont < tabela.getColumnCount(); cont++){
            Cell cellNome = rowTitulos.createCell(cont);
            cellNome.setCellValue(titulos[cont]);
            cellNome.setCellStyle(estiloCelula);
            
            int widthUnits = 40 * 256;
            sheetProdutos.setColumnWidth(cont, widthUnits);
            short heightUnits = 30*20;
            cellNome.getRow().setHeight(heightUnits);            
        }
        
        for(int cont1 = 0; cont1 < tabela.getRowCount(); cont1++){
            Row row = sheetProdutos.createRow(cont1+1);
            for (int cont2 = 0; cont2 < tabela.getColumnCount(); cont2++){
                Cell cellNome = row.createCell(cont2);
                if(cont2 == 7){
                    try {
                        InputStream inputStream = new FileInputStream(tabela.getValueAt(cont1, cont2).toString());
                        byte[] bytes = IOUtils.toByteArray(inputStream);
                        inputStream.close();
                        
                        CreationHelper helper = workBook.getCreationHelper();
                        Drawing draw = sheetProdutos.createDrawingPatriarch();
                        
                        ClientAnchor anchor = helper.createClientAnchor();
                        anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                        int pictureIdx = workBook.addPicture(bytes,HSSFWorkbook.PICTURE_TYPE_PNG);

                        anchor.setCol1(cont2);
                        anchor.setRow1(cont1+1);
                        
                        Picture pict = draw.createPicture(anchor, pictureIdx);
                        
                        pict.resize(1, 1);
                       
                    } catch (Exception ex) {
                        cellNome.setCellValue("");
                    }

                }else{
                    if(tabela.getColumnName(cont2).equals("Codigo")||
                       tabela.getColumnName(cont2).equals("Descrição")||
                       tabela.getColumnName(cont2).equals("Ultima Alteração")){
                        
                        cellNome.setCellValue(tabela.getValueAt(cont1, cont2).toString());
                    }else if(tabela.getColumnName(cont2).equals("Quantidade Mínima")||
                            tabela.getColumnName(cont2).equals("Quantidade Estoque")){
                        
                            cellNome.setCellValue(Integer.parseInt(tabela.getValueAt(cont1, cont2).toString()));
                    }else if(tabela.getColumnName(cont2).equals("Valor Unitario") ||
                            tabela.getColumnName(cont2).equals("Valor total em estoque")){
                            
                            cellNome.setCellValue(Float.parseFloat(tabela.getValueAt(cont1, cont2).toString()));
                    }
                    
                }
                    int widthUnits = 40 * 256;
                    sheetProdutos.setColumnWidth(cont2, widthUnits);
                    short heightUnits = 60*20;
                    cellNome.getRow().setHeight(heightUnits);
            } 
        }
        
        try {
            new ProgressBar(100);
            FileOutputStream out = new FileOutputStream(file);
            workBook.write(out);
            out.close();
            
            JOptionPane.showMessageDialog(null, "Tabela exportada com sucesso");

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao exportar tabela");
        }
    }
       
    public void importarTabela(){
        JFileChooser filechooser = new JFileChooser();
        filechooser.setFileFilter(new FileNameExtensionFilter("Tabelas", "xls", "xlsx"));
        filechooser.setAcceptAllFileFilterUsed(false);
        int getArquivo = filechooser.showOpenDialog(produtosTabel);
        
        if(getArquivo == JFileChooser.APPROVE_OPTION){
            
            String caminhoArquivo = filechooser.getSelectedFile().getPath();
            String tipoArquivo = filechooser.getSelectedFile().getName();
            try{
                FileInputStream arquivo = new FileInputStream(new File(caminhoArquivo));
                if(tipoArquivo.endsWith(".xls")){
                    //-----Tabela----
                    HSSFWorkbook workBook = new HSSFWorkbook(arquivo);
                    HSSFSheet sheetProdutos = workBook.getSheetAt(0);
                    //----Imagens-----
                    HSSFPatriarch patriarch = sheetProdutos.createDrawingPatriarch();
                    List<HSSFShape> imagens = patriarch.getChildren();
                 
                    //------Processamento dos dados----
                    Iterator<Row> rowIterator = sheetProdutos.iterator();
                    
                    while(rowIterator.hasNext()){
                        Row row = rowIterator.next();
                        
                        ProdutosControle produtosControle = new ProdutosControle();
                        if(row.getRowNum()>0){
                            if(row.getCell(0) != null){
                                String codigo = row.getCell(0).getStringCellValue();
                                String descricao = row.getCell(1).getStringCellValue();
                                Integer quantInicial = Math.round((float) row.getCell(3).getNumericCellValue());
                                Float valorUnitario = (float) row.getCell(4).getNumericCellValue();
                                Integer quantMinima = Math.round((float) row.getCell(2).getNumericCellValue());
                                produtosControle.manipularProdutoExcel(codigo, 
                                    descricao, 
                                    quantInicial, 
                                    valorUnitario, 
                                    produtos.getDataPC(),
                                    "",
                                    quantMinima);
                            }else{
                                break;
                            }
                        }
                    }
                    
                    //-----Processamento das Imagens---
                    if(imagens.size()>0){
                        for(int cont =0; cont < imagens.size(); cont++){
                            ProdutosControle produtosControle = new ProdutosControle();
                            Picture inPic = (Picture) imagens.get(cont);
                            ClientAnchor cliente = inPic.getClientAnchor();
                            String nomeImagem = sheetProdutos.getRow(cliente.getRow1()).getCell(0).getStringCellValue();
                            PictureData picData = inPic.getPictureData();
                            byte[] data = picData.getData();

                            produtosControle.salvarImagemDaTabela(nomeImagem, data);
                        }                    
                    }
                    
                    arquivo.close();
                }else if(tipoArquivo.endsWith(".xlsx")){
                     //-----Tabela----
                    XSSFWorkbook workBook = new XSSFWorkbook(arquivo);
                    XSSFSheet sheetProdutos = workBook.getSheetAt(0);
                    //----Imagens-----
                    XSSFDrawing patriarch = sheetProdutos.createDrawingPatriarch();
                    List<XSSFShape> imagens = patriarch.getShapes();
                 
                    //------Processamento dos dados----
                    Iterator<Row> rowIterator = sheetProdutos.iterator();
                    
                    while(rowIterator.hasNext()){
                        Row row = rowIterator.next();
                        
                        ProdutosControle produtosControle = new ProdutosControle();
                        if(row.getRowNum()>0){
                            if(row.getCell(0) != null){
                                String codigo = row.getCell(0).getStringCellValue();
                                String descricao = row.getCell(1).getStringCellValue();
                                Integer quantInicial = Math.round((float) row.getCell(3).getNumericCellValue());
                                Float valorUnitario = (float) row.getCell(4).getNumericCellValue();
                                Integer quantMinima = Math.round((float) row.getCell(2).getNumericCellValue());
                                produtosControle.manipularProdutoExcel(codigo, 
                                    descricao, 
                                    quantInicial, 
                                    valorUnitario, 
                                    produtos.getDataPC(),
                                    "",
                                    quantMinima);
                            }else{
                                break;
                            }
                        }
                    }
                    
                    //-----Processamento das Imagens---
                    if(imagens.size()>0){
                        for(int cont =0; cont < imagens.size(); cont++){
                            ProdutosControle produtosControle = new ProdutosControle();
                            XSSFPicture inPic = (XSSFPicture) imagens.get(cont);
                            XSSFClientAnchor cliente = inPic.getClientAnchor();
                            String nomeImagem = sheetProdutos.getRow(cliente.getRow1()).getCell(0).getStringCellValue();
                            PictureData picData = inPic.getPictureData();
                            byte[] data = picData.getData();
                            produtosControle.salvarImagemDaTabela(nomeImagem, data);
                        }                    
                    }
                    
                    arquivo.close();
                }
                new ProgressBar(150); //Barra de progresso da operacao
                JOptionPane.showMessageDialog(null, "Tabela importada com sucesso");
                this.atualizarTabela();
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Erro ao importar a tabela: " + e);
            }
        }
    }
    
    public void exportarModelo(File file){
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheetProdutos = workBook.createSheet("Modelo");
        String[] titulos = {"Codigo", "Descrição", "Quantidade Minima", "Quantidade em Estoque", "Valor Unitário", "Valor total em estoque", "Ultima Alteração","Imagem"};
        
        HSSFFont fonte = workBook.createFont();
        fonte.setBold(true);
        fonte.setFontHeight((short)400);
        CellStyle estiloCelula = workBook.createCellStyle();
        estiloCelula.setFont(fonte);
        
        Row rowTitulos = sheetProdutos.createRow(0);
        for(int cont= 0; cont < titulos.length; cont++){
            Cell cellNome = rowTitulos.createCell(cont);
            cellNome.setCellValue(titulos[cont]);
            cellNome.setCellStyle(estiloCelula);
            
            int widthUnits = 40 * 256;
            sheetProdutos.setColumnWidth(cont, widthUnits);
            short heightUnits = 30*20;
            cellNome.getRow().setHeight(heightUnits);            
        }
        
        try {
            new ProgressBar(50);
            FileOutputStream out = new FileOutputStream(file);
            workBook.write(out);
            out.close();
            JOptionPane.showMessageDialog(null, "Modelo exportado com sucesso");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao exportar tabela");
        }
        
    }
    
    
 //-------------------------------------------Referente a tela------------------------   
    
    public void sair(){
        Login login = new Login();
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja sair?", "Sair", JOptionPane.YES_NO_OPTION);
        
        if(resposta == JOptionPane.YES_OPTION){
            this.setVisible(false);
            br.com.controleestoque.model.Login.setUsuarioAtivo("");
            login.setVisible(true);
        }
    }
    
    public void fecharPrograma(){
        int resposta = JOptionPane.showConfirmDialog(null, "Deseja fechar o programa?", "Fechar", JOptionPane.YES_NO_OPTION);
        
        if(resposta == JOptionPane.YES_OPTION){
            System.exit(0);
        }
    }
    
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        pesquisaField = new javax.swing.JTextField();
        pesquisaButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        produtosTabel = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        alterarButton = new javax.swing.JButton();
        consultaTdButton = new javax.swing.JButton();
        entradaSaidaButton = new javax.swing.JButton();
        excluirButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        quantInicialLabel = new javax.swing.JLabel();
        descricaoLabel = new javax.swing.JLabel();
        codigoLabel = new javax.swing.JLabel();
        dataCadastroLabel = new javax.swing.JLabel();
        valorUnitarioLabel = new javax.swing.JLabel();
        quantInicialField = new javax.swing.JTextField();
        descricaoField = new javax.swing.JTextField();
        codigoField = new javax.swing.JTextField();
        dataCadastroField = new javax.swing.JTextField();
        valorUniField = new javax.swing.JTextField();
        cadastroButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        imgLabel = new javax.swing.JLabel();
        imgButton = new javax.swing.JButton();
        quantMinLabel = new javax.swing.JLabel();
        quantMinField = new javax.swing.JTextField();
        jBarraMenu = new javax.swing.JMenuBar();
        jOpcoes = new javax.swing.JMenu();
        importarMenuItem = new javax.swing.JMenuItem();
        exportarMenuItem = new javax.swing.JMenuItem();
        alterarUsuarioMenuItem = new javax.swing.JMenuItem();
        logoutMenuItem = new javax.swing.JMenuItem();
        fecharMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Principal");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pesquisaField.setToolTipText("Digite o codigo do produto à ser buscado");
        pesquisaField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pesquisaFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                pesquisaFieldFocusLost(evt);
            }
        });
        pesquisaField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pesquisaFieldMousePressed(evt);
            }
        });
        pesquisaField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pesquisaFieldActionPerformed(evt);
            }
        });
        pesquisaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pesquisaFieldKeyPressed(evt);
            }
        });

        pesquisaButton.setText("Buscar pelo Codigo");
        pesquisaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pesquisaButtonActionPerformed(evt);
            }
        });

        produtosTabel.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        produtosTabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        produtosTabel.setToolTipText("Clique em uma linha para seleciona-la");
        produtosTabel.setCellSelectionEnabled(true);
        produtosTabel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                produtosTabelFocusLost(evt);
            }
        });
        produtosTabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                produtosTabelMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                produtosTabelMouseReleased(evt);
            }
        });
        jScrollPane1.setViewportView(produtosTabel);

        alterarButton.setText("Alterar Produto");
        alterarButton.setEnabled(false);
        alterarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alterarButtonActionPerformed(evt);
            }
        });

        consultaTdButton.setText("Consultar Tudo");
        consultaTdButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultaTdButtonActionPerformed(evt);
            }
        });

        entradaSaidaButton.setText("Entrada/Saida");
        entradaSaidaButton.setEnabled(false);
        entradaSaidaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                entradaSaidaButtonActionPerformed(evt);
            }
        });

        excluirButton.setText("Excluir Produto");
        excluirButton.setEnabled(false);
        excluirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                excluirButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(consultaTdButton, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                .addGap(36, 36, 36)
                .addComponent(entradaSaidaButton, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                .addGap(37, 37, 37)
                .addComponent(alterarButton, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addGap(35, 35, 35)
                .addComponent(excluirButton, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(alterarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(excluirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(consultaTdButton, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(entradaSaidaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(pesquisaField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pesquisaButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pesquisaField)
                    .addComponent(pesquisaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Consulta", jPanel1);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        quantInicialLabel.setText("Quantidade Inicial");

        descricaoLabel.setText("Descrição");

        codigoLabel.setText("Codigo");

        dataCadastroLabel.setText("Data Cadastro");

        valorUnitarioLabel.setText("Valor Unitário");

        quantInicialField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantInicialFieldActionPerformed(evt);
            }
        });
        quantInicialField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                quantInicialFieldKeyTyped(evt);
            }
        });

        descricaoField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descricaoFieldActionPerformed(evt);
            }
        });
        descricaoField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                descricaoFieldKeyTyped(evt);
            }
        });

        codigoField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                codigoFieldActionPerformed(evt);
            }
        });
        codigoField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codigoFieldKeyTyped(evt);
            }
        });

        dataCadastroField.setEditable(false);

        valorUniField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                valorUniFieldKeyTyped(evt);
            }
        });

        cadastroButton.setText("Cadastrar");
        cadastroButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cadastroButtonActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        imgLabel.setText("Sem Imagem");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        imgButton.setText("Upload Imagem");
        imgButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imgButtonActionPerformed(evt);
            }
        });

        quantMinLabel.setText("Quantidade Minima");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(dataCadastroLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dataCadastroField, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(valorUnitarioLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(valorUniField))
                            .addComponent(cadastroButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(codigoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(descricaoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE))
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(quantInicialLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(imgButton, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(quantMinLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(quantInicialField)
                                    .addComponent(descricaoField)
                                    .addComponent(quantMinField)
                                    .addComponent(codigoField))))
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(codigoField)
                            .addComponent(codigoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(descricaoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(descricaoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(quantMinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(quantMinLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(quantInicialLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(quantInicialField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(valorUnitarioLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(dataCadastroLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dataCadastroField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(valorUniField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(imgButton)
                        .addGap(18, 18, 18)
                        .addComponent(cadastroButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Cadastro", jPanel3);

        jOpcoes.setText("Opções");
        jOpcoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOpcoesActionPerformed(evt);
            }
        });

        importarMenuItem.setText("Importar Tabela");
        importarMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importarMenuItemActionPerformed(evt);
            }
        });
        jOpcoes.add(importarMenuItem);

        exportarMenuItem.setText("Exportar Tabela");
        exportarMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportarMenuItemActionPerformed(evt);
            }
        });
        jOpcoes.add(exportarMenuItem);

        alterarUsuarioMenuItem.setText("Alterar dados do usuario");
        alterarUsuarioMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alterarUsuarioMenuItemActionPerformed(evt);
            }
        });
        jOpcoes.add(alterarUsuarioMenuItem);

        logoutMenuItem.setText("Logout");
        logoutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutMenuItemActionPerformed(evt);
            }
        });
        jOpcoes.add(logoutMenuItem);

        fecharMenuItem.setText("Fechar");
        fecharMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fecharMenuItemActionPerformed(evt);
            }
        });
        jOpcoes.add(fecharMenuItem);

        jBarraMenu.add(jOpcoes);

        setJMenuBar(jBarraMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void pesquisaFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pesquisaFieldFocusGained
        // TODO add your handling code here:
        if(pesquisaField.getText().equals("Digite o codigo ou descrição do produto")){
            pesquisaField.setCaretPosition(0);
            pesquisaField.setCaretColor(Color.BLACK);
        }
    }//GEN-LAST:event_pesquisaFieldFocusGained

    private void pesquisaFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pesquisaFieldFocusLost
        // TODO add your handling code here:
        if(pesquisaField.getText().isEmpty()){
            pesquisaField.setText("Digite o codigo ou descrição do produto");
            pesquisaField.setForeground(new Color(204,204,204));
        }
    }//GEN-LAST:event_pesquisaFieldFocusLost

    private void pesquisaFieldMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pesquisaFieldMousePressed
        // TODO add your handling code here
        if(pesquisaField.getText().equals("Digite o codigo ou descrição do produto")){
            pesquisaField.setText("");
            pesquisaField.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_pesquisaFieldMousePressed

    private void pesquisaFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pesquisaFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pesquisaFieldActionPerformed

    private void pesquisaFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisaFieldKeyPressed
        // TODO add your handling code here:
        if(pesquisaField.getText().equals("Digite o codigo ou descrição do produto")){
            pesquisaField.setText("");
            pesquisaField.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_pesquisaFieldKeyPressed

    private void pesquisaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pesquisaButtonActionPerformed
        
            // TODO add your handling code here:
        this.preencherTabelaConsulta(this.pesquisaField.getText());
        
        this.entradaSaidaButton.setEnabled(false);
        this.alterarButton.setEnabled(false);
        this.excluirButton.setEnabled(false);
    }//GEN-LAST:event_pesquisaButtonActionPerformed

    private void produtosTabelFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_produtosTabelFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_produtosTabelFocusLost

    private void produtosTabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_produtosTabelMouseClicked
        // TODO add your handling code here:
        this.entradaSaidaButton.setEnabled(true);
        this.alterarButton.setEnabled(true);
        this.excluirButton.setEnabled(true);
    }//GEN-LAST:event_produtosTabelMouseClicked

    private void produtosTabelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_produtosTabelMouseReleased
        // TODO add your handling code here:
        produtosTabel.setCellSelectionEnabled(false);
        produtosTabel.setRowSelectionAllowed(true);
    }//GEN-LAST:event_produtosTabelMouseReleased

    private void alterarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alterarButtonActionPerformed
       
            //AlteracaoFrame alteracaoPanel = new AlteracaoFrame();
            AlteracaoFrame alteracaoPanel = null;
            
            alteracaoPanel = new AlteracaoFrame(produtosTabel.getValueAt(produtosTabel.getSelectedRow(), 0).toString());
            
            this.setVisible(false);
            alteracaoPanel.setVisible(true);
        
    }//GEN-LAST:event_alterarButtonActionPerformed

    private void consultaTdButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_consultaTdButtonActionPerformed

            // TODO add your handling code here:
        this.atualizarTabela();
      
        this.entradaSaidaButton.setEnabled(false);
        this.alterarButton.setEnabled(false);
        this.excluirButton.setEnabled(false);
    }//GEN-LAST:event_consultaTdButtonActionPerformed

    private void entradaSaidaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_entradaSaidaButtonActionPerformed
       
            // TODO add your handling code here:
            EntradaSaida entradaSaida = new EntradaSaida(produtosTabel.getValueAt(produtosTabel.getSelectedRow(), 0).toString());
            entradaSaida.setVisible(true);
            
            this.setVisible(false);

       
    }//GEN-LAST:event_entradaSaidaButtonActionPerformed

    private void excluirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_excluirButtonActionPerformed
        // TODO add your handling code here:

      

            int resposta = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir o produto selecionado?", "Excluir", JOptionPane.YES_NO_OPTION);
            if(resposta == JOptionPane.YES_OPTION){
                this.excluir();
                this.atualizarTabela();
                
            }
       
        this.entradaSaidaButton.setEnabled(false);
        this.alterarButton.setEnabled(false);
        this.excluirButton.setEnabled(false);

    }//GEN-LAST:event_excluirButtonActionPerformed

    private void quantInicialFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantInicialFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantInicialFieldActionPerformed

    private void quantInicialFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantInicialFieldKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_quantInicialFieldKeyTyped

    private void descricaoFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descricaoFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_descricaoFieldActionPerformed

    private void descricaoFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descricaoFieldKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_descricaoFieldKeyTyped

    private void codigoFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_codigoFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoFieldActionPerformed

    private void codigoFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoFieldKeyTyped
        // TODO add your handling code here
    }//GEN-LAST:event_codigoFieldKeyTyped

    private void valorUniFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorUniFieldKeyTyped
        // TODO add your handling code here:

    }//GEN-LAST:event_valorUniFieldKeyTyped

    private void cadastroButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cadastroButtonActionPerformed
        // TODO add your handling code here:

        ProdutosControle produtoControle = new ProdutosControle();
        
        if(Pattern.matches(".+", codigoField.getText()) && Pattern.matches(".+",descricaoField.getText())  && Pattern.matches("\\d+",quantInicialField.getText()) && Pattern.matches("[+-]?([0-9]*[.])?[0-9]+",valorUniField.getText()) && Pattern.matches("\\d+",quantMinField.getText())){
            
            produtoControle.cadastrarProduto(codigoField.getText(), descricaoField.getText(), Integer.parseInt(quantInicialField.getText()), Float.parseFloat(valorUniField.getText()), dataCadastroField.getText(), sourcePath, Integer.parseInt(quantMinField.getText()));
            this.atualizarTabela();     
        
        }else{        
            JOptionPane.showMessageDialog(null, "Por favor, preencher todos os campos");

        }
        this.limparCampos();
    }//GEN-LAST:event_cadastroButtonActionPerformed

    private void imgButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imgButtonActionPerformed
        // TODO add your handling code here:
        ProdutosControle produtosControle = new ProdutosControle();

        if(!Pattern.matches(".+",codigoField.getText())){
            JOptionPane.showMessageDialog(null, "Por favor, preencha o campo codigo");
        }else{
            
            this.sourcePath = produtosControle.visualizarImg();
            
            if(!sourcePath.equals("")){
                ImageIcon imgIcon = new ImageIcon(this.sourcePath);
                imgIcon.setImage(imgIcon.getImage().getScaledInstance(300, 354, 100));
                imgLabel.setIcon(imgIcon);
                imgLabel.setText("");
            }else{
                imgLabel.setText("Sem Imagem");
            }    
            
        }
    }//GEN-LAST:event_imgButtonActionPerformed

    private void logoutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutMenuItemActionPerformed
        // TODO add your handling code here:
        this.sair();
    }//GEN-LAST:event_logoutMenuItemActionPerformed

    private void fecharMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fecharMenuItemActionPerformed
        // TODO add your handling code here:
        this.fecharPrograma();
    }//GEN-LAST:event_fecharMenuItemActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowClosing

    private void exportarMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportarMenuItemActionPerformed
        // TODO add your handling code here:
        this.exportar();
    }//GEN-LAST:event_exportarMenuItemActionPerformed

    private void importarMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importarMenuItemActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Instruções para importar tabelas:\n"
                + "1. Ela deve estar em .xls ou .xlsx\n"
                + "2. Os campos devem estar preenchidos na ordem dos titulos\n"
                + "3. A primeira linha deve ter os seguintes titulos:\n" 
                + "Codigo | Descriçao | Quantidade Minima | Quantidade no Estoque | Valor unitario | Valor total em estoque | Ultima Alteração | Imagem\n"
                + "4. Caso haja imagens, ela deve estar posicionada corretamente na celula\n"
                + "5. Em caso de dúvida, exporte um modelo para servir como guia");
        this.importarTabela();
    }//GEN-LAST:event_importarMenuItemActionPerformed

    private void jOpcoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOpcoesActionPerformed
        // TODO add your handling code here:
       
    }//GEN-LAST:event_jOpcoesActionPerformed

    private void alterarUsuarioMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alterarUsuarioMenuItemActionPerformed
        // TODO add your handling code here:
        AlterarUsuario alterarUsuario = new AlterarUsuario();
        alterarUsuario.setVisible(true);
    }//GEN-LAST:event_alterarUsuarioMenuItemActionPerformed

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
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton alterarButton;
    private javax.swing.JMenuItem alterarUsuarioMenuItem;
    private javax.swing.JButton cadastroButton;
    private javax.swing.JTextField codigoField;
    private javax.swing.JLabel codigoLabel;
    private javax.swing.JButton consultaTdButton;
    private javax.swing.JTextField dataCadastroField;
    private javax.swing.JLabel dataCadastroLabel;
    private javax.swing.JTextField descricaoField;
    private javax.swing.JLabel descricaoLabel;
    private javax.swing.JButton entradaSaidaButton;
    private javax.swing.JButton excluirButton;
    private javax.swing.JMenuItem exportarMenuItem;
    private javax.swing.JMenuItem fecharMenuItem;
    private javax.swing.JButton imgButton;
    private javax.swing.JLabel imgLabel;
    private javax.swing.JMenuItem importarMenuItem;
    private javax.swing.JMenuBar jBarraMenu;
    private javax.swing.JMenu jOpcoes;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JMenuItem logoutMenuItem;
    private javax.swing.JButton pesquisaButton;
    private javax.swing.JTextField pesquisaField;
    private javax.swing.JTable produtosTabel;
    private javax.swing.JTextField quantInicialField;
    private javax.swing.JLabel quantInicialLabel;
    private javax.swing.JTextField quantMinField;
    private javax.swing.JLabel quantMinLabel;
    private javax.swing.JTextField valorUniField;
    private javax.swing.JLabel valorUnitarioLabel;
    // End of variables declaration//GEN-END:variables
}
