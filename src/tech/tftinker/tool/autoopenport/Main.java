package tech.tftinker.tool.autoopenport;

import com.dosse.upnp.UPnP;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Main {

    public static Config config;

    public static void main(String[] args) throws InterruptedException {
        if (UPnP.isUPnPAvailable()) { //is UPnP available?
            while (true) {
                print("loading config file");
                // load config file
                config = Config.readConfigFile("./config.json");
                if (config == null){
                    print("Error has happen", ConsoleColors.RED);
                    System.exit(1);
                } else {
                    print("Done Loading config file");
                }

                print("");

                // Load modified WaifUPnP
                for(Port port: config.ports){
                    print("Opening " + port);
                    Map<String, String> outPut = UPnP.isMappedTCPReturnOutPut(port.portOut);
                    if (outPut.get("NewInternalPort") != null) { //is the port already mapped?
                        print("UPnP port forwarding not enabled: port is already mapped", ConsoleColors.RED);
                        if (outPut.get("NewInternalPort").equalsIgnoreCase("" + port.portIn) &&
                                outPut.get("NewInternalClient").equalsIgnoreCase(port.ip)){
                            print("It is the same set port");
                        } else {
                            print(outPut.get("NewPortMappingDescription") + " is use the port at : " + outPut.get("NewInternalClient"), ConsoleColors.RED);
                        }
                    } else if (UPnP.openPortTCP(port.portOut, port.portIn, port.ip, port.name)) { //try to map port
                        print("UPnP port forwarding enabled");
                    } else {
                        print("UPnP port forwarding failed", ConsoleColors.RED);
                    }
                    print("");
                }

                print("Waiting for " + (((double) config.waitTime)/60/60) + "hr");
                TimeUnit.SECONDS.sleep(config.waitTime);
                print("Restart Now");
                print("");
                print("");
            }
        } else {
            print("UPnP is not available", ConsoleColors.RED);
        }
    }

    public static void print(String message){
        String color = ConsoleColors.BLUE;
        print(message, color);
    }

    public static void print(String message, String color){
        System.out.print(ConsoleColors.GREEN);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String time = dtf.format(now);
        System.out.print(time);
        System.out.print(ConsoleColors.RESET + " : " + color);
        System.out.print(message);
        System.out.println(ConsoleColors.RESET);
        if (config != null) {
            try {
                File f1 = new File(config.logFilePath);
                if (!f1.exists()) {
                    f1.createNewFile();
                }

                FileWriter fileWritter = new FileWriter(f1.getName(), true);
                BufferedWriter bw = new BufferedWriter(fileWritter);
                bw.write(time + " : " + message + "\n");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
