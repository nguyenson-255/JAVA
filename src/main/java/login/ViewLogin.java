package login;

import Register.ViewRegister;
import client.Client;
import io.File;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewLogin extends JFrame implements ActionListener {
    private  JButton btn1;
    private JLabel l1,l2,l3;
    private JButton btn;
    private JTextField t1,t2,t3;
    private ArrayList<User> ds;

    public ViewLogin() throws HeadlessException {
        JPanel p1,p2;
        setTitle("Register");
        p1 = new JPanel(new FlowLayout());
        p2 = new JPanel(new FlowLayout());


        l1 = new JLabel("username");
        t1 = new JTextField(20);
        l2 = new JLabel("password");
        t2 = new JPasswordField(20);
        btn = new JButton("Login");
        btn1 = new JButton("Register");
        l3 = new JLabel();

        p1.add(l1);
        p1.add(t1);
        p2.add(l2);
        p2.add(t2);

        add(p1);
        add(p2);
        add(btn);
        add(btn1);
        add(l3);
        ds = File.docFile("user.txt");
        btn.addActionListener(this);
        btn.setActionCommand("LOG");
        btn1.addActionListener(this);
        btn1.setActionCommand("RES");

        setSize(450,250);
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "LOG"){
            for (User t : ds){
                if (t.checkAccount(t1.getText(),t2.getText())){
                    setVisible(false);
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            Client c = new Client(t1.getText());
                        }
                    };
                    thread.start();
                    break;
                }

            }
        }else if (e.getActionCommand() == "RES"){
            ViewRegister v = new ViewRegister();
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        ViewLogin v = new ViewLogin();
    }


}
