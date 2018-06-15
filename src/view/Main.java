/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dao.TaskJpaController;
import dao.exceptions.NonexistentEntityException;
import entidade.Task;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author kassioluz
 */
public class Main extends JFrame {

    private static JFrame frame, frameTable;
    private static JTextField tfTitle;
    private static JTextArea tfDescription;
    private static JTextField tfDateInit;
    private static JTextField tfDateEnd;
    private static JTextField tfStatus;
    private static JComboBox<String> comboBox;

    static String title;
    static String description;
    static String dataInit;
    static String dataEnd;
    static String status;

    //Jtable
    static JPanel painelFundo;
    static private JPanel painelBotoes;
    static JTable tabela;
    static JScrollPane barraRolagem;
    private static JButton btInserir;
    private static JButton btExcluir;
    private static JButton btEditar;
    private static DefaultTableModel modelo = new DefaultTableModel();
    static Task taskEdit;

    public static void main(String args[]) {
        montarTela();
    }

    public static void montarTela() {
        criaJTable();
        criaJanela();
    }

    private static void addForm(long id) {

        frame = new JFrame();
        frame.setVisible(true);
        frame.setBounds(100, 100, 500, 489);
        frame.getContentPane().setLayout(null);
        frame.setLocationRelativeTo(null);

        JLabel lblPhone = new JLabel("Titulo:");
        lblPhone.setBounds(65, 68, 100, 20);
        frame.getContentPane().add(lblPhone);

        tfTitle = new JTextField();
        tfTitle.setBounds(150, 65, 247, 20);
        frame.getContentPane().add(tfTitle);
        tfTitle.setColumns(10);

        JLabel lbldescription = new JLabel("Descrição:");
        lbldescription.setBounds(65, 115, 100, 14);
        frame.getContentPane().add(lbldescription);

        tfDescription = new JTextArea();
        tfDescription.setBounds(150, 112, 247, 60);
        frame.getContentPane().add(tfDescription);
        tfDescription.setColumns(10);

        JLabel lbldateInit = new JLabel("Data Início:");
        lbldateInit.setBounds(65, 195, 100, 14);
        frame.getContentPane().add(lbldateInit);

        tfDateInit = new JTextField();
        tfDateInit.setBounds(150, 195, 247, 17);
        frame.getContentPane().add(tfDateInit);
        tfDateInit.setColumns(10);

        JLabel lbldateEnd = new JLabel("Data Fim:");
        lbldateEnd.setBounds(65, 230, 100, 14);
        frame.getContentPane().add(lbldateEnd);

        tfDateEnd = new JTextField();
        tfDateEnd.setBounds(150, 230, 247, 17);
        frame.getContentPane().add(tfDateEnd);
        tfDateEnd.setColumns(10);

        JLabel lblOccupation = new JLabel("Status");
        lblOccupation.setBounds(65, 280, 100, 14);
        frame.getContentPane().add(lblOccupation);

        comboBox = new JComboBox<String>();
        comboBox.addItem("Selecione");
        comboBox.addItem("Fazer");
        comboBox.addItem("Fazendo");
        comboBox.addItem("Feito");
        comboBox.setBounds(150, 280, 100, 20);
        frame.getContentPane().add(comboBox);

        if (id != 0) {

            tfTitle.setText(title);
            tfDescription.setText(description);
            tfDateInit.setText(dataInit);
            tfDateEnd.setText(dataEnd);

            if (status.equals("Fazer")) {
                comboBox.setSelectedIndex(1);
            } else if (status.equals("Fazendo")) {
                comboBox.setSelectedIndex(2);
            } else {
                comboBox.setSelectedIndex(3);
            }
        }

        JButton btnSubmit = new JButton("Salvar");
        btnSubmit.setBackground(Color.GRAY);
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setBounds(200, 340, 89, 23);
        frame.getContentPane().add(btnSubmit);

        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                title = tfTitle.getText();
                description = tfDescription.getText();
                dataInit = tfDateInit.getText();
                dataEnd = tfDateEnd.getText();

                status = comboBox.getSelectedItem().toString();
                if (status.equals("Selecione")) {
                    status = "Fazer";
                }

                if (id != 0) {
                    taskEdit.setId(id);
                    taskEdit.setTitle(title);
                    taskEdit.setDescription(description);
                    taskEdit.setDate_init(dataInit);
                    taskEdit.setDate_end(dataEnd);
                    taskEdit.setStatus(status);
                    editar();
                } else {
                    salvar();
                }
            }
        });
    }

    private static void salvar() {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDate_init(dataInit);
        task.setDate_end(dataEnd);
        task.setStatus(status);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TaskPU");
        TaskJpaController controller = new TaskJpaController(emf);

        controller.create(task);

        tfTitle.setText("");
        tfDescription.setText("");
        tfDateInit.setText("");
        tfDateEnd.setText("");

        atualizar(modelo);
        JOptionPane.showMessageDialog(null, "Salvo com sucesso");

    }

    public static void editar() {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TaskPU");
        TaskJpaController controller = new TaskJpaController(emf);
        
        try {
            controller.edit(taskEdit);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        atualizar(modelo);
        frame.dispose();
        
    }

    public static void criaJanela() {

        frameTable = new JFrame();

        btInserir = new JButton("Inserir");
        btExcluir = new JButton("Excluir");
        btEditar = new JButton("Editar");
        painelBotoes = new JPanel();
        barraRolagem = new JScrollPane(tabela);
        painelFundo = new JPanel();
        painelFundo.setLayout(new BorderLayout());
        painelFundo.add(BorderLayout.CENTER, barraRolagem);
        painelBotoes.add(btInserir);
        painelBotoes.add(btEditar);
        painelBotoes.add(btExcluir);
        painelFundo.add(BorderLayout.SOUTH, painelBotoes);

        frameTable.getContentPane().add(painelFundo);
        frameTable.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frameTable.setSize(800, 800);
        frameTable.setLocationRelativeTo(null);
        frameTable.setVisible(true);

        btInserir.addActionListener(new BtInserirListener());
        btEditar.addActionListener(new BtEditarListener());
        btExcluir.addActionListener(new BtExcluirListener());
    }

    private static void criaJTable() {
        tabela = new JTable(modelo);
        modelo.addColumn("Id");
        modelo.addColumn("Titulo");
        modelo.addColumn("Descrição");
        modelo.addColumn("Início");
        modelo.addColumn("Fim");
        modelo.addColumn("Status");
        tabela.getColumnModel().getColumn(0).setPreferredWidth(10);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(80);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(120);
        tabela.getColumnModel().getColumn(1).setPreferredWidth(120);
        atualizar(modelo);
    }

    public static void atualizar(DefaultTableModel modelo) {
        modelo.setNumRows(0);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TaskPU");
        TaskJpaController dao = new TaskJpaController(emf);

        for (Task t : dao.findTaskEntities()) {
            modelo.addRow(new Object[]{t.getId(), t.getTitle(), t.getDescription(), t.getDate_init(), t.getDate_end(), t.getStatus()});
        }
    }

    private static class BtInserirListener implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            addForm(0);
        }
    }

    private static class BtEditarListener implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            int linhaSelecionada = -1;
            linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada >= 0) {

                long idContato = (long) tabela.getValueAt(linhaSelecionada, 0);

                taskEdit = new Task();

                title = (String) tabela.getValueAt(linhaSelecionada, 1);
                description = (String) tabela.getValueAt(linhaSelecionada, 2);
                dataInit = (String) tabela.getValueAt(linhaSelecionada, 3);
                dataEnd = (String) tabela.getValueAt(linhaSelecionada, 4);
                status = (String) tabela.getValueAt(linhaSelecionada, 5);
                addForm(idContato);

                // AtualizarContato ic = new AtualizarContato(modelo, idContato, linhaSelecionada);
                // ic.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "É necesário selecionar uma linha.");
            }
        }

    }

    private static class BtExcluirListener implements ActionListener {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            int linhaSelecionada = -1;
            linhaSelecionada = tabela.getSelectedRow();
            if (linhaSelecionada >= 0) {
                long idContato = (long) tabela.getValueAt(linhaSelecionada, 0);
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("TaskPU");
                TaskJpaController dao = new TaskJpaController(emf);

                try {
                    dao.destroy(idContato);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

                modelo.removeRow(linhaSelecionada);
                JOptionPane.showMessageDialog(null, "Removido com sucesso");
            } else {
                JOptionPane.showMessageDialog(null, "É necesário selecionar uma linha.");
            }
        }

    }

}
