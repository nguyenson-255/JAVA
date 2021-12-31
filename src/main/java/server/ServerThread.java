/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 *
 * @author Admin
 */
public class ServerThread extends JFrame implements Runnable {

    private Socket socketOfServer;
    private int clientNumber;
    private BufferedReader is;
    private BufferedWriter os;
    private boolean isClosed;
    private String message;

    public int getClientNumber() {
        return clientNumber;
    }

    public ServerThread(Socket socketOfServer, int clientNumber) {
        this.socketOfServer = socketOfServer;
        this.clientNumber = clientNumber;
        System.out.println("Server thread number " + clientNumber + " Started");
        isClosed = false;
    }

    public String getMessage() {
        return message;
    }

    public BufferedWriter getOs() {
        return os;
    }

    @Override
    public void run() {
        try {
            is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
            os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
            System.out.println("Khời động luông mới thành công, ID là: " + clientNumber);
            os.write("get-id" + "," + this.clientNumber);
            os.newLine();
            os.flush();
            Server.serverThreadBus.sendOnlineList();
            Server.serverThreadBus.mutilCastSend("global-message"+","+"---Client "+this.clientNumber+" đã đăng nhập---");
            while (!isClosed) {
                message = is.readLine();
                if (message == null) {
                    break;
                }
                String[] messageSplit = message.split(",");
                System.out.println(message);
                if(messageSplit[0].equals("send-to-person")){
                    Server.serverThreadBus.sendMessageToPersion(Integer.parseInt(messageSplit[3]),"Client "+ messageSplit[2]+" (tới bạn): "+messageSplit[1]);
                }
                if (messageSplit[0].equals("log-out")){
                    isClosed = true;
                    Server.serverThreadBus.remove(Integer.parseInt(messageSplit[1]));
                    System.out.println(Integer.parseInt(messageSplit[1])+" đã thoát");
                    Server.serverThreadBus.sendOnlineList();
                    Server.serverThreadBus.mutilCastSend("global-message"+","+"---Client "+Integer.parseInt(messageSplit[1])+" đã thoát---");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
