package tech.tftinker.tool.autoopenport;

import com.dosse.upnp.UPnP;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        if (UPnP.isUPnPAvailable()) { //is UPnP available?
            while (true) {
                // load config file
                Config config = Config.readConfigFile("./config.json");
                if (config == null){
                    System.out.println("Error has happen");
                    System.exit(1);
                } else {
                    //System.out.println("Wait Time (min): " + (((double) config.waitTime)/60));
                }

                // Load modified WaifUPnP
                for(Port port: config.ports){
                    System.out.println("Opening " + port);
                    Map<String, String> outPut = UPnP.isMappedTCPReturnOutPut(port.portOut);
                    if (outPut.get("NewInternalPort") != null) { //is the port already mapped?
                        System.out.println("UPnP port forwarding not enabled: port is already mapped");
                        if (outPut.get("NewInternalPort").equalsIgnoreCase("" + port.portIn) &&
                                outPut.get("NewInternalClient").equalsIgnoreCase(port.ip)){
                            System.out.println("It is the same set port");
                        } else {
                            System.out.println(outPut.get("NewPortMappingDescription") + " is use the port at : " + outPut.get("NewInternalClient"));
                        }
                    } else if (UPnP.openPortTCP(port.portOut, port.portIn, port.ip, port.name)) { //try to map port
                        System.out.println("UPnP port forwarding enabled");
                    } else {
                        System.out.println("UPnP port forwarding failed");
                    }
                }

                System.out.println("Waiting for " + (((double) config.waitTime)/60) + "min");
                TimeUnit.SECONDS.sleep(config.waitTime);
            }
        } else {
            System.out.println("UPnP is not available");
        }


    }
}
