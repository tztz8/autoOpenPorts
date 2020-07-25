package tech.tftinker.tool.autoopenport;

//import java.net.InetAddress;
//import java.net.UnknownHostException;

public class Port {
    public long portOut;
    public long portIn;
    public String ip;
    public String type;
    public String name;
    public String[] portMapperAddArgs;

    public Port(long portOut, long portIn, String ip, String type, String name){
        this.portOut = portOut;
        this.portIn = portIn;
        // TODO: add if the ip fits
        this.ip = ip;
        this.type = type;
        this.name = name;
        this.portMapperAddArgs = new String[]{"-add",
                "-externalPort", this.getPortOut(),
                "-internalPort", this.getPortIn(),
                "-ip", this.ip,
                "-protocol", this.type,
                "-description", this.name};
    }

    public String getPortOut() {
        return "" + portOut;
    }

    public String getPortIn() {
        return "" + portIn;
    }
}
