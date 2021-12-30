package Register;

import io.File;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ViewRegister extends JFrame implements ActionListener {

    private JLabel l1,l2,l3;
    private JButton btn;
    private JTextField t1,t2,t3;
    private ArrayList<User> ds;

    public ViewRegister() throws HeadlessException {
        JPanel p1,p2,p3,p4;
        setTitle("Register");
        p1 = new JPanel(new FlowLayout());
        p2 = new JPanel(new FlowLayout());
        p3 = new JPanel(new FlowLayout());
        p4 = new JPanel(new FlowLayout());


        l1 = new JLabel("username");
        t1 = new JTextField(20);
        l2 = new JLabel("password");
        t2 = new JPasswordField(20);
        l3 = new JLabel("name");
        t3 = new JTextField(20);
        btn = new JButton("Register");


        p1.add(l1);
        p1.add(t1);
        p2.add(l2);
        p2.add(t2);
        p3.add(l3);
        p3.add(t3);


        add(p1);
        add(p2);
        add(p3);
        add(btn);
        btn.addActionListener(this);
        setSize(450,250);
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        setVisible(true);
        ds = File.docFile("user.txt");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (t1.getText()!="" && t2.getText()!="" &&t3.getText()!=""){
            ds.add(new User(t1.getText(),t2.getText(),t3.getText()));
            File.luuFile("user.txt",ds);
        }

        ds = File.docFile("user.txt");
        for (User t:ds) {
            System.out.println(t.toString());
        }
    }

    public static void main(String[] args) {
        ViewRegister v = new ViewRegister();
    }
}
