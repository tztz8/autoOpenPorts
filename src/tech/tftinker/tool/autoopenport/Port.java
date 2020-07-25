package tech.tftinker.tool.autoopenport;

//import java.net.InetAddress;
//import java.net.UnknownHostException;

public class Port {
    public int portOut;
    public int portIn;
    public String ip;
    public String type;
    public String name;

    public Port(long portOut, long portIn, String ip, String type, String name){
        this.portOut = (int) portOut;
        this.portIn = (int) portIn;
        // TODO: add if the ip fits
        this.ip = ip;
        this.type = type;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Port{" +
                "portOut=" + portOut +
                ", portIn=" + portIn +
                ", ip='" + ip + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
