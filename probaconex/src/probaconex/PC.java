
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuTimer;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.ProcStat;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Adam
 */
public class PC implements Serializable{

    private sigar1 sigar = null;
    private InetAddress ip;
    private Date timeStamp;
    private String OS=System.getProperty("os.name");
    private File[] roots=File.listRoots(); 
    public PC(sigar1 sigar) {
        this.sigar = sigar;
        try {
            this.ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
        timeStamp=new java.util.Date();
        timeStamp=new Timestamp(timeStamp.getTime());
    }


    public void getCPUinfo() {
        CpuInfo[] cpuInfoList;
        try {
            cpuInfoList = sigar.getCpuInfoList();       
            System.out.println("Processor Info:");
            System.out.println("CPU model: " + cpuInfoList[0].getModel());
            System.out.println("CPU vendor: " + cpuInfoList[0].getVendor());
            System.out.println("CPU cores: " + cpuInfoList[0].getTotalCores());
            System.out.println("CPU frequency: " + cpuInfoList[0].getMhz() + " Mhz");
        } catch (SigarException ex) {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getSystemMemInfo() {
        try {
            Mem mem = sigar.getMem();
            System.out.println("Memory info:");
            System.out.println("Total memory: " + mem.getRam() + " MB");
            System.out.println("Free memory: " + (Math.rint(mem.getActualFree() / (Math.pow(1024, 2)) * 100) / 100) + " MB");
            System.out.println("Used memory: " + (Math.rint(mem.getActualUsed() / (Math.pow(1024, 2)) * 100) / 100) + " MB");
            System.out.println("Used memory in %: " + (Math.rint(mem.getUsedPercent() * 100) / 100) + " %");
        } catch (SigarException ex) {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  /*  public static void getJVMMemInfo() {
        // Total amount of free memory available to the JVM 
        System.out.println("JVM memory Info:");
        System.out.println("Free memory in JVM: "
                + (Math.rint(Runtime.getRuntime().freeMemory() / (Math.pow(1024, 2)) * 100) / 100) + "MB");

        //This will return Long.MAX_VALUE if there is no preset limit 
        double maxMemory = (Math.rint(Runtime.getRuntime().maxMemory() / (Math.pow(1024, 2)) * 100) / 100);
       // Maximum amount of memory the JVM will attempt to use 
        System.out.println("Maximum memory in JVM: "
                + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory) + "MB");

         //Total memory currently in use by the JVM 
        System.out.println("Total memory in JVM: "
                + (Math.rint(Runtime.getRuntime().totalMemory() / (Math.pow(1024, 2)) * 100) / 100) + "MB");

    }*/

    public void getMachineInfo() {

        System.out.println("Machine Info:");
        System.out.println("Machine name: " + ip.getHostName());
        System.out.println("Machine IP: " + ip.getHostAddress());
        /* TimeStamp               */
        System.out.println("Machine Timestamp:" + timeStamp);
        /* Operative System */
        System.out.println("Machine Operating System: " + OS);        
    }

    public  void getFileSystemInfo() {
        System.out.println("File System Info:");
        /* Get a list of all filesystem roots on this system */
        
        /* For each filesystem root, print some info */
        for (File root : roots) {
            System.out.println("File system root: " + root.getAbsolutePath());
            System.out.println("Total space (gigabytes): " + Math.rint(((root.getTotalSpace()) / Math.pow(1024, 3)) * 100) / 100);
            System.out.println("Free space (gigabytes): " + Math.rint(((root.getFreeSpace()) / Math.pow(1024, 3))* 100) / 100);
            System.out.println("Usable space (gigabytes): " + Math.rint(((root.getUsableSpace()) / Math.pow(1024, 3))* 100) / 100);
        }
    }

    public void getSwapInfo() {
        try {
            Swap swap = sigar.getSwap();
            System.out.println("Swap Info:");
            System.out.println("Total swap:" + Math.rint(((swap.getTotal()) / Math.pow(1024, 3)) * 100) / 100 + " GB");
            System.out.println("Free swap:" + Math.rint(((swap.getFree()) / Math.pow(1024, 3)) * 100) / 100 + " GB");
            System.out.println("Used swap:" + Math.rint(((swap.getUsed()) / Math.pow(1024, 3)) * 100) / 100 + " GB");
            System.out.println("PageIn swap:" + swap.getPageIn());
            System.out.println("PageOut swap:" + swap.getPageOut());
        } catch (SigarException ex) {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getCpuUse() {
        try {
            Cpu cpu = sigar.getCpu();
            CpuTimer a = new CpuTimer(this.sigar);
            a.start();
            System.out.println("CPU use:");
            System.out.println(a.getCpuUsage());
            a.stop();
            System.out.println("Total system cpu idle time:" + cpu.getIdle());
            System.out.println("Total system cpu io wait time:" + cpu.getWait());
            System.out.println("Total system cpu time servicing interrupts:" + cpu.getIrq());
            System.out.println("Total system cpu nice time:" + cpu.getNice());
            System.out.println("Total system cpu time servicing softirqs:" + cpu.getSoftIrq());
            System.out.println("Total system cpu involuntary wait time:" + cpu.getStolen());
            System.out.println("Total system cpu kernel time:" + cpu.getSys());
            System.out.println("kernel time:" + cpu.getTotal());
            System.out.println("Total system cpu user time:" + cpu.getUser());
        } catch (SigarException ex) {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void processInfo() {
        try {     
            ProcStat pro = sigar.getProcStat();
            long[] az = sigar.getProcList();
            System.out.println("Processes list:");
            for (long b : az) {
                ProcState i = sigar.getProcState(b);
                ProcMem g = sigar.getProcMem(b);
                System.out.println("********");
                System.out.println("Proces name:" + i.getName());
                System.out.println("Proces state:" + i.getState());
                System.out.println("Proces threads:" + i.getThreads());
                System.out.println("Proces size:" + g.getSize() / (1024 * 1024) + "MB");
                System.out.println("********");
            }
            //System.out.println("CPU use:"+ proc.getStartTime());
            System.out.println("Total processes:" + pro.getTotal());
            System.out.println("Idle processes:" + pro.getIdle());
            System.out.println("Running processes:" + pro.getRunning());
            System.out.println("Threads:" + pro.getThreads());

            // System.out.println("CPU use:"+proc.getPercent());
        } catch (SigarException ex) {
            Logger.getLogger(PC.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public InetAddress getIP() {
        return this.ip;
    }

    public void update(sigar1 sig) {
        this.sigar = sig;
    }
    public String getExtendedPCName(){
        return "Name: "+ip.getHostName()+" \n"+"IP: "+ip.getHostAddress()+" \n"+"Timestamp: "+this.timeStamp+" \n"+"OS: "+this.OS+" \n";
    }
    public String getaPCName(){
        return ip.getHostName()+" \nIP: "+ip.getHostAddress();
    }
    public void showAllInformation(){
        getMachineInfo();
        getCPUinfo();
        getCpuUse();
        processInfo();
        getSystemMemInfo();
        getSwapInfo();
        getFileSystemInfo();
    }
}
