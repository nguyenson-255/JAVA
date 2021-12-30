/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Admin
 */
public class Server implements ActionListener{

    public static volatile ServerThreadBus serverThreadBus;
    public static Socket socketOfServer;

    private static JFrame jf;
    private static JButton btnConnect;
    private static JButton btnCancel;
    private static JTextField txt;
    private static int clientNumber = 0;
    private static ServerSocket listener = null;


    public static void main(String[] args) {
        Server a =new Server();
        a.UIView();
        serverThreadBus = new ServerThreadBus();
    }

    public void UIView() {

        jf = new JFrame("Server");
        JLabel lbl = new JLabel("Port");
        txt = new JTextField(10);
        btnConnect = new JButton("Connect");
        btnCancel = new JButton("Cancel");

        JPanel p =new JPanel(new FlowLayout());
        p.add(lbl);
        p.add(txt);
        JPanel p1 =new JPanel(new FlowLayout());
        p1.add(btnConnect);
        p1.add(btnCancel);
        jf.add(p);
        jf.add(p1);
        jf.setLayout(new BoxLayout(jf.getContentPane(),BoxLayout.Y_AXIS));
        jf.setSize(450,450);
        jf.setVisible(true);


        btnConnect.addActionListener(this);
        btnConnect.setActionCommand("Connect");
        btnCancel.addActionListener(this);
        btnCancel.setActionCommand("Cancel");

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand() == "Cancel"){
            Thread t =new Thread(){
                @Override
                public void run() {
                    System.out.println("Server is Closed...");
                    try {
                        listener.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            };
            t.start();
        }else if (e.getActionCommand() == "Connect"){
            Thread t =new Thread(){
                @Override
                public void run() {
                    try {
                        System.out.println("Server is waiting to accept user...");
                        if (txt.getText().equals("")){
                            System.exit(1);
                        }
                        listener = new ServerSocket(Integer.parseInt(txt.getText()));
                    } catch (IOException e) {
                        System.out.println(e);
                        System.exit(1);
                    }
                    ThreadPoolExecutor executor = new ThreadPoolExecutor(
                            10, // corePoolSize
                            100, // maximumPoolSize
                            10, // thread timeout
                            TimeUnit.SECONDS,
                            new ArrayBlockingQueue<>(8) // queueCapacity
                    );
                    try {
                        while (true) {
                            socketOfServer = listener.accept();
                            ServerThread serverThread = new ServerThread(socketOfServer, clientNumber++);
                            serverThreadBus.add(serverThread);
                            executor.execute(serverThread);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            };
            t.start();
        }
    }
}
