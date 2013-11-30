
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author p4790084
 */
public class Client {

    private static ClientTools tools;
    
    public static void main(String[] args) throws IOException {    
       
        String ip = 
          (args.length > 0) ? args[0] : "172.16.51.9";
       
        tools = new ClientTools(InetAddress.getByName(ip));
        tools.createSocket();
        int option = 0;
        while (option != 6) {
            option = menu();
            showInfo(option);
        }
    }
    
    public static int menu() {
        int option = 0;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("1. List of available resources.");
        System.out.println("2. Check the status of a specific resource.");
        System.out.println("3. Check the status of all available resources.");
        System.out.println("4. Updating status of all available resources.");
        System.out.println("5. Updating the status of a specific resource.");
        System.out.println("6. Exit.");
        System.out.print("Choose: ");
        while (option < 1 || option > 7) {
            try {
                option = Integer.parseInt(reader.readLine());
                if (option < 1 || option > 6) {
                    System.out.print("Incorrect value, choose again: ");
                }
            } catch (Exception ex) {
                System.out.print("Incorrect value, choose again: ");
            }
        }
        return option;
    }

    public static void showInfo(int option) {

        switch (option) {
            case 1:
                try {
                    tools.showAvailablePCs();
                    break;
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            case 2:
                tools.showSpecificPC();
                break;
            case 3:
                tools.showAllPCs();
                break;
            case 4:
                tools.UpdatePCs();
                break;
            case 5:
                tools.updateSpecificPC();
                break;
            case 6:
                tools.closeSocket();
                break;
        }
    }
}
