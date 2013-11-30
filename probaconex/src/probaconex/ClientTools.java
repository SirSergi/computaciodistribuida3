
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author plb1
 */
public class ClientTools {

    private PC pc = null;
    private InetAddress ip = null;
    private final int port = 12345;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private ObjectInputStream inO;
    private ObjectOutputStream outO;
    private ClientThread thread;
    private MulticastSocket s;

    public ClientTools(InetAddress ip) {
        this.ip = ip;
    }

    public void sendPC(int mode) {
        try {
            if (mode == 0) {
                pc = new PC(new sigar1());
                outO = new ObjectOutputStream(socket.getOutputStream());
                outO.writeObject(pc);
            } else {
                System.out.println("Sending 7");
                out = new DataOutputStream(socket.getOutputStream());
                out.write(7);
                pc = new PC(new sigar1());
                outO = new ObjectOutputStream(socket.getOutputStream());
                outO.writeObject(pc);
            }

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createSocket() {
        try {
            socket = new Socket(ip, port);
            sendPC(0);

            s = new MulticastSocket(3500);
            s.joinGroup(InetAddress.getByName("225.12.5.1"));
            thread = new ClientThread(s, this);
            thread.start();
            //out.flush();
            // outO.close();
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showAvailablePCs() throws IOException {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            out.write(1);
            inO = new ObjectInputStream(socket.getInputStream());
            String[] pcList = (String[]) inO.readObject();

            for (String pcList1 : pcList) {
                System.out.println(pcList1);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
        }
    }

    public void closeSocket() {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            out.write(6);
            socket.close();
            s.close();
        } catch (IOException ex) {
            System.out.println("Client tancat");
        }
    }

    public void showSpecificPC() {
        try {
            selectPC(2);
            inO = new ObjectInputStream(socket.getInputStream());
            pc = (PC) inO.readObject();
            pc.showAllInformation();
            System.out.println();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
        } catch (IOException e1) {
        }
    }

    public void showAllPCs() {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            out.write(3);
            inO = new ObjectInputStream(socket.getInputStream());
            HashMap<InetAddress, PC> PCList = (HashMap<InetAddress, PC>) inO.readObject();
            for (PC specificPC : PCList.values()) {
                System.out.println("------------------------");
                specificPC.showAllInformation();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void UpdatePCs() {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            out.write(4);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateSpecificPC() {
        selectPC(5);

    }

    public void selectPC(int optionSelect) {
        int ipCursor = 0;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            out.write(optionSelect);
            inO = new ObjectInputStream(socket.getInputStream());
            String[] pcList = (String[]) inO.readObject();
            String[] ipList = new String[pcList.length];

            System.out.println("\nResources avaliables for explore: ");
            for (String pcList1 : pcList) {
                if (pcList1 != null) {
                    System.out.println("PC number " + (ipCursor + 1) + " : " + pcList1);
                    String[] proba = pcList1.split(" ");
                    ipList[ipCursor] = proba[2];
                    ipCursor++;
                }
            }

            System.out.print("Choose an specific computer to see all the information:");
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(System.in));

            int option = 0;
            while (option > (ipCursor) || option <= 0) {
                try {
                    option = Integer.parseInt(reader2.readLine());
                    if (option > (ipCursor) || option <= 0) {
                        System.out.print("The computer selected doesn't exist. The system only have " + (ipCursor) + " connected, choose a pertinent value: ");
                    }
                } catch (Exception e) {
                    System.out.print("The computer selected doesn't exist. The system only have " + (ipCursor) + " connected, choose a pertinent value: ");
                }
            }
            outO = new ObjectOutputStream(socket.getOutputStream());
            outO.writeObject(ipList[option - 1]);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException e) {
        } catch (IOException e1) {
        }
    }
}
