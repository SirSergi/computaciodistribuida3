
import java.net.InetAddress;
import java.util.HashMap;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author p4790084
 */
public class DatabasePC {
    private static DatabasePC database=new DatabasePC();
    private  HashMap<InetAddress,PC> PCList=new HashMap<>();
    
    private DatabasePC(){
        
    }
    
    public static DatabasePC getInstance(){
        if(database==null){
            database=new DatabasePC();
        }
        return database;
    }
    
    public HashMap<InetAddress,PC> getPCList(){
        return this.PCList;
    }
    public PC getPC(InetAddress ip){
        return this.PCList.get(ip);
    }
    public void addPC(PC pc){
        System.out.println();
       PCList.put(pc.getIP(),pc);
    }
    public void removePC(PC pc){
        PCList.remove(pc.getIP());
    }

}
