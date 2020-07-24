package tech.tftinker.tool.autoopenport;

//import java.net.InetAddress;
//import java.net.UnknownHostException;

public class Port {
    public long portOut;
    public long portIn;
    public String ip;
    public String name;

    public Port(long portOut, long portIn, String ip, String name){
        this.portOut = portOut;
        this.portIn = portIn;
        // TODO: add if the ip fits
        this.ip = ip;
        this.name = name;
    }

//    public Port(int portOut, int portIn, String ip, String name){
//        new Port((long) portOut, (long) portIn,  ip,  name);
//    }
//
//    public Port(int port, String ip, String name){
//        new Port(port, port, ip, name);
//    }
//
//    public Port(int portOut, int portIn, String name){
//        InetAddress inetAddress;
//        try {
//            inetAddress = InetAddress.getLocalHost();
//            new Port(portOut, portIn, inetAddress.getHostAddress(), name);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//            System.exit(1);
//        }
//    }
//
//    public Port(int port, String name){
//        new Port(port, port, name);
//    }
}
