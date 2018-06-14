/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import dao.TaskJpaController;
import entidade.Task;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author kassioluz
 */
public class Main extends JFrame {

    private static JFrame janela;
    private static JPanel painelPrincipal;
    private static JFrame frame;
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

    public static void main(String args[]) {
        montarTela();
    }

    public static void montarTela() {

        janela = new JFrame("Task");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        painelPrincipal = new JPanel();
        janela.add(painelPrincipal);

        JButton botaoAdicionar = new JButton("Nova Tarefa");
        botaoAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addForm();
            }
        });
        painelPrincipal.add(botaoAdicionar);

        JButton botaoTarefas = new JButton("Tarefas");
        botaoTarefas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        painelPrincipal.add(botaoTarefas);

        janela.pack();
        janela.setSize(450, 450);
        janela.setVisible(true);
    }

    private static void addForm() {

        frame = new JFrame();
        frame.setVisible(true);
        frame.setBounds(100, 100, 500, 489);
        frame.getContentPane().setLayout(null);

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

                salvar();
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

    }

}
