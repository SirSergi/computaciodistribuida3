
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author p4790084
 */
public class ClientThread extends Thread {
    
    MulticastSocket s;
    ClientTools tools;
    public ClientThread(MulticastSocket s,ClientTools tools) {
        this.s=s;
        this.tools=tools;
    }

    public void run() {
        try {            
            byte[] buf = new byte[1];
            while (true) {
                DatagramPacket pack = new DatagramPacket(buf, buf.length);
                s.receive(pack);
                String num="4";
                if(new String(pack.getData(),"UTF-8").equals(num)){            
                    tools.sendPC(1);
                }
            }
        } catch (Exception e) {
            System.out.println("Thread closed");
        }
    }
}
