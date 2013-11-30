
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperic.sigar.Sigar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author p4790084
 */
public class ServerThread extends Thread {

    private int port = 3500;
    private InetAddress receiverHost;
    private Socket socket = null;
    private PC pc = null;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectInputStream inO;
    private ObjectOutputStream outO;

    public ServerThread(Socket ss) {
        this.socket = ss;
    }

    public void run() {
        try {
            System.out.println("Connexion acepted");
            inO = new ObjectInputStream(socket.getInputStream());
            pc = (PC) inO.readObject();
            if (pc != null) {
                System.out.println("Computer " + pc.getIP() + " has conected");
            }
            pc.getMachineInfo();
            DatabasePC.getInstance().addPC(pc);
            in = new DataInputStream(socket.getInputStream());
            int option = 0;
            while (option != 6) {
                option = in.read();
                if (option > 0 && option < 9) {
                    showInfo(option);
                } else {
                    this.disconnect();
                }
            }
        } catch (Exception ex) {
            System.out.println("Client disconnected");
        }
    }

    public void disconnect() throws Exception {
        try {
            System.out.println("Computer with ip: " + socket.getLocalAddress().getHostAddress() + " has disconnected");
            socket.close();
            System.out.println();
            DatabasePC.getInstance().removePC(pc);
            throw new Exception();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showInfo(int option) {
        try {
            System.out.println("The user " + socket.getLocalAddress().getHostAddress() + " has selected the option :" + option);
            switch (option) {
                case 1:
                    ShowAvailableResources();
                    break;
                case 2:
                    sendSpecificResource();
                    break;
                case 3:
                    showAllPCs();
                    break;
                case 4:
                    CallPCs();
                    break;
                case 5:
                    UpdateSpecificPC();
                    break;
                case 6:
                    try {
                        disconnect();
                    } catch (Exception e) {
                    }
                    break;
                case 7:
                   UpdatePC();
                    break;
                case 8:
                    
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ShowAvailableResources() throws IOException {
        String[] PClist = new String[DatabasePC.getInstance().getPCList().size() + 1];
        PClist[0] = "Available resources:\n";
        int i = 1;
        for (PC pc : DatabasePC.getInstance().getPCList().values()) {
            PClist[i] = "PC number: " + i + "\n" + pc.getExtendedPCName();
            i++;
        }
        outO = new ObjectOutputStream(socket.getOutputStream());
        outO.writeObject(PClist);
    }

    public void sendSpecificResource() {
        try {
            String ip = this.selectPC();
            outO = new ObjectOutputStream(socket.getOutputStream());
            outO.writeObject(DatabasePC.getInstance().getPC(InetAddress.getByName(ip)));
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
        }
    }

    public void showAllPCs() {
        try {
            outO = new ObjectOutputStream(socket.getOutputStream());
            outO.writeObject(DatabasePC.getInstance().getPCList());
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void CallPCs() {
        try {
            MulticastSocket s = new MulticastSocket(port);
            String message = "4";
            byte[] buf = new byte[1];
            buf = message.getBytes();
            System.out.println(buf.length);
            System.out.println(new String(buf, "UTF-8"));
            DatagramPacket pack = new DatagramPacket(buf, 0, buf.length, InetAddress.getByName("225.12.5.1"), port);
            s.send(pack);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void UpdatePC() {
        try {
            inO = new ObjectInputStream(socket.getInputStream());
            pc = (PC) inO.readObject();
            if (pc != null) {
                DatabasePC.getInstance().addPC(pc);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
        }
    }

    public void UpdateSpecificPC() {
        try { 
            String ip = selectPC();
            DatagramSocket s = new DatagramSocket();
            System.out.println(ip);
            String message = "4";
            byte[] buf = new byte[1];
            buf = message.getBytes();
            DatagramPacket pack = new DatagramPacket(buf, 0, buf.length, InetAddress.getByName(ip), port);
            s.send(pack);
        } catch (Exception e) {
        }
    }

    public String selectPC() throws IOException, ClassNotFoundException {
        int i = 1;
        String[] PClist = new String[DatabasePC.getInstance().getPCList().size() + 1];
        for (PC pc : DatabasePC.getInstance().getPCList().values()) {
            PClist[i] = pc.getaPCName();
            System.out.println(PClist[i]);
            i++;
        }
        outO = new ObjectOutputStream(socket.getOutputStream());
        outO.writeObject(PClist);
        inO = new ObjectInputStream(socket.getInputStream());
        return (String) inO.readObject();
    }
}
