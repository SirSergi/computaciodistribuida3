

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author p4790084
 */
public class Server {

    private static final int port = 12345;

    public static void main(String[] args) throws IOException {
        ServerSocket socket=new ServerSocket(port);
        ExecutorService executor= Executors.newFixedThreadPool(20);
        System.out.println("Waiting for connexion");
        while(true){           
            Socket ss=socket.accept();
            Runnable worker=new ServerThread(ss);
            executor.execute(worker);
            System.out.println("Waiting for connexion");
        }
    }
    
}
