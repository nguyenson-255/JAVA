package io;

import model.User;

import java.io.*;
import java.util.ArrayList;

public class File {


    public static void luuFile(String path, ArrayList<User> ds){
        try{
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ds);
            oos.flush();
            oos.close();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static ArrayList<User> docFile(String path){
        ArrayList<User> ds = new ArrayList<>();
        try{
            java.io.File f = new java.io.File(path);
            if ( f.exists()){
                FileInputStream fis = new FileInputStream(f);
                ObjectInputStream ois = new ObjectInputStream(fis);
                ds = (ArrayList<User>) ois.readObject();
                ois.close();
                fis.close();
            }



        }catch (Exception e){
            e.printStackTrace();
        }
        return ds;
    }
}
