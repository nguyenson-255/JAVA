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
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Client extends JFrame implements ActionListener{




    //back-end
    private Thread thread;
    private BufferedWriter os;
    private BufferedReader is;
    private Socket socketOfClient;
    private List<String> onlineList;
    private int id;


    // UI

    private JButton jButton4;
    private JButton jButton;
    private JButton jButton1;
    private JButton jButton2;
    private JComboBox<String> jComboBox1;
    private JComboBox<String> jComboBox2;
    private JLabel jLabel;
    private JLabel jfilename;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JPanel jPanel;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTabbedPane jTabbedPane1;
    private JTextArea jTextArea1;
    private JTextArea jTextArea2;
    private JTextField jTextField1;
    private JPanel jPanel4;
    private JLabel jLabel4;
    private File[] fileToSend;

    public Client(String text) {
        fileToSend = new File[1];
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
        jPanel3.setLayout(new BoxLayout(jPanel3,BoxLayout.Y_AXIS));

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
        jComboBox2 = new JComboBox<>();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();


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

        jComboBox2.addActionListener(this);
        jComboBox2.setActionCommand("JCOMBOX1");

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


        jLabel = new JLabel("File");
        jLabel.setFont(new Font("Arial", Font.BOLD, 25));
        jLabel.setBorder(new EmptyBorder(20, 0, 10, 0));
        jLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        jfilename = new JLabel("Choose file");
        jfilename.setFont(new Font("Arial", Font.BOLD, 20));
        jfilename.setBorder(new EmptyBorder(50, 0, 00, 0));
        jfilename.setAlignmentX(Component.CENTER_ALIGNMENT);

        jPanel = new JPanel();
        jPanel.setBorder(new EmptyBorder(75, 0, 10, 0));

        jButton = new JButton("SendFile");
        jButton.setPreferredSize(new Dimension(150, 75));
        jButton.setFont(new Font("Arial", Font.BOLD, 20));

        jButton4 = new JButton("ChooseFile");
        jButton4.setPreferredSize(new Dimension(150, 75));
        jButton4.setFont(new Font("Arial", Font.BOLD, 20));

        jButton.addActionListener(this);
        jButton.setActionCommand("SFILE");
        jButton4.addActionListener(this);
        jButton4.setActionCommand("CHO");


        jPanel4 = new JPanel(new FlowLayout());
        jPanel.add(jButton);
        jPanel.add(jButton4);
        jPanel4.add(jComboBox2);
        jPanel.add(jComboBox2);

        jLabel4.setFont(new Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel4.setText("{Người nhận}");

        jPanel3.add(jLabel4);
        jPanel3.add(jPanel4);
        jPanel3.add(jfilename);
        jPanel3.add(jPanel);

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
                        socketOfClient = new Socket("localhost", 7777);
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
                            if (messageSplit[0].equals("receive-file")){
                                String fileContent = messageSplit[2];
                                fileContent = fileContent.replaceAll("[|]",",");
                                byte[] fileContentBytes = fileContent.getBytes();

                                int input = JOptionPane.showConfirmDialog(null,
                                        "Do you want to Download File "+messageSplit[1]+"?", "Select an Option...",JOptionPane.YES_NO_OPTION);

                                if (input == 0){
                                        File f = new File(messageSplit[1]);
                                        FileOutputStream fos = new FileOutputStream(f);
                                        fos.write(fileContentBytes);
                                        fos.close();
                                }else if (input == 1){

                                }


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
        jComboBox2.removeAllItems();

        String idString = ""+this.id;
        for(String e : onlineList){
            if(!e.equals(idString)){
                jComboBox1.addItem("Client "+ e);
                jComboBox2.addItem("Client "+ e);
            }
        }
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String cmt = e.getActionCommand();

        if (cmt == "SFILE"){

            if (fileToSend[0] == null){
                jfilename.setText("Please choose file first");

            }else {
                try {
                    FileInputStream fis = new FileInputStream(fileToSend[0].getAbsolutePath());
                    String filename = fileToSend[0].getName();
                    byte[] filecontentBytes = new byte[(int) fileToSend[0].length()];
                    fis.read(filecontentBytes);
                    String filecontent = new String(filecontentBytes);

                    filecontent = filecontent.replaceAll("\t", "");
                    filecontent = filecontent.replaceAll("\n", "");
                    filecontent = filecontent.replaceAll("\r", "");
                    filecontent = filecontent.replaceAll(",","|");

                    String[] parner = ((String)jComboBox2.getSelectedItem()).split(" ");
                    os.write("send-file-to-person"+","+filename+","+filecontent+","+this.id+","+parner[1]);
                    os.newLine();
                    os.flush();

                    jTextArea1.setText(jTextArea1.getText()+"Bạn (tới Client "+parner[1]+"): "+filename+"\n");
                    jTextArea1.setCaretPosition(jTextArea1.getDocument().getLength());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(rootPane, "Có lỗi xảy ra");
                }


            }

        }
        else if (cmt == "CHO"){
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setDialogTitle("chosse file send");
            if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                fileToSend[0] = jFileChooser.getSelectedFile();
                jfilename.setText("the file you want to send is : "+ fileToSend[0].getName());
            }
        }
        else if (cmt == "JCOMBOX1"){
            jLabel4.setText("Đang nhắn với "+jComboBox2.getSelectedItem());
        }else if (cmt == "JCOMBOX"){
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
