/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import server.Server;
import server.ServerThread;
import server.ServerThreadBus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Client extends JFrame implements ActionListener{
    //back-end
    private Thread thread;
    private BufferedWriter os;
    private BufferedReader is;
    private Socket socketOfClient;
    private List<String> onlineList;
    private int id;


    // UI

    private JButton jButton1;
    private JButton jButton2;
    private JComboBox<String> jComboBox1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTabbedPane jTabbedPane1;
    private JTextArea jTextArea1;
    private JTextArea jTextArea2;
    private JTextField jTextField1;

    public Client(String text) {
        initComponents();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        jTextArea1.setEditable(false);
        jTextArea2.setEditable(false);
        onlineList = new ArrayList<>();
        setUpSocket();
        id = -1;
    }

    private void initComponents() {

        jPanel3 = new JPanel();

        jTabbedPane1 = new JTabbedPane();
        jPanel1 = new JPanel();

        jScrollPane2 = new JScrollPane();
        jTextArea2 = new JTextArea();
        jPanel2 = new JPanel();

        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jTextField1 = new JTextField();
        jButton1 = new JButton();
        jComboBox1 = new JComboBox<>();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jScrollPane1.setPreferredSize(new Dimension(450,450));
        jPanel1.add(jScrollPane2);

        jTabbedPane1.addTab("Danh sách online", jPanel1);



        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton1.setText("Gửi");
        jButton1.addActionListener(this);
        jButton1.setActionCommand("SENDMSG");

        jComboBox1.addActionListener(this);
        jComboBox1.setActionCommand("JCOMBOX");

        jLabel1.setText("Chọn người nhân");

        jLabel2.setText("Nhập tin nhắn");

        jLabel3.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setText("{Người nhận}");


        jPanel2.add(jLabel3);
        jPanel2.add(jScrollPane1);
        jPanel2.add(jLabel1);
        jPanel2.add(jComboBox1);
        jPanel2.add(jLabel2);
        jPanel2.add(jTextField1);
        jPanel2.add(jButton1);
        jPanel2.setLayout(new BoxLayout(jPanel2,BoxLayout.Y_AXIS));


        jTabbedPane1.addTab("Nhắn tin", jPanel2);

        jTabbedPane1.addTab("Gửi File",jPanel3);

        jButton2 = new JButton("Đăng xuất");
        jButton2.addActionListener(this);
        jButton2.setActionCommand("EXIT");
        jTabbedPane1.addTab("Đăng xuất",jButton2);

        add(jTabbedPane1);
        pack();
        setSize(450,450);
    }// </editor-fold>//GEN-END:initComponents



    private void setUpSocket() {
        try {
            thread = new Thread() {
                @Override
                public void run() {

                    try {
                        socketOfClient = new Socket("localhost", 9999);
                        System.out.println("Kết nối thành công!");
                        os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));
                        is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));
                        String message;
                        while (true) {
                            message = is.readLine();
                            if(message==null){
                                break;
                            }
                            String[] messageSplit = message.split(",");
                            if(messageSplit[0].equals("get-id")){
                                id = Integer.parseInt(messageSplit[1]);
                                setTitle("Client "+ id);
                            }
                            if (messageSplit[0].equals("update-online-list")) {
                                onlineList = new ArrayList<>();
                                String online ="";
                                String[] onlineSplit = messageSplit[1].split("-");
                                for(int i=0; i<onlineSplit.length; i++){
                                    onlineList.add(onlineSplit[i]);
                                    online+="Client "+onlineSplit[i]+" đang online\n";
                                }
                                jTextArea2.setText(online);
                                updateCombobox();
                            }
                            if(messageSplit[0].equals("global-message")){
                                jTextArea1.setText(jTextArea1.getText()+messageSplit[1]+"\n");
                                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                            }
                        }
                    os.close();
                    is.close();
                    socketOfClient.close();
                    } catch (UnknownHostException e) {
                        return;
                    } catch (IOException e) {
                        return;
                    }
                }
            };
            thread.run();
        } catch (Exception e) {
        }
    }
    private void updateCombobox(){
        jComboBox1.removeAllItems();
        String idString = ""+this.id;
        for(String e : onlineList){
            if(!e.equals(idString)){
                jComboBox1.addItem("Client "+ e);
            }
        }
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String cmt = e.getActionCommand();

        if (cmt == "JCOMBOX"){
                jLabel3.setText("Đang nhắn với "+jComboBox1.getSelectedItem());
        }else if (cmt == "SENDMSG"){
            String messageContent = jTextField1.getText();
            if(messageContent.isEmpty()){
                JOptionPane.showMessageDialog(rootPane, "Bạn chưa nhập tin nhắn");
                return;
            }
            try {
                String[] parner = ((String)jComboBox1.getSelectedItem()).split(" ");
                os.write("send-to-person"+","+messageContent+","+this.id+","+parner[1]);
                os.newLine();
                os.flush();

                jTextArea1.setText(jTextArea1.getText()+"Bạn (tới Client "+parner[1]+"): "+messageContent+"\n");
                jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
            }

            jTextField1.setText("");
        }
        else if (cmt == "EXIT"){

            try {
            os.write("log-out"+","+this.id);
            os.newLine();
            os.flush();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
            }
            try {
                socketOfClient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            System.exit(1);

        }
    }
}
