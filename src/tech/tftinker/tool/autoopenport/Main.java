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
    public static boolean printStartMessage = false;
    public static boolean debugFlag = true;

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

                if (!printStartMessage){
                    print("First Start of auto open ports \n");
                    printStartMessage = true;
                }

                print("");

                // Load modified WaifUPnP
                for(Port port: config.ports){
                    print("Opening " + port);
                    Map<String, String> outPut = null;
                    if(port.type.equalsIgnoreCase("tcp")){
                        outPut = UPnP.isMappedTCPReturnOutPut(port.portOut);
                    }else if (port.type.equalsIgnoreCase("udp")){
                        outPut = UPnP.isMappedUDPReturnOutPut(port.portOut);
                    }else {
                        print("error with port setup", ConsoleColors.RED);
                    }
                    
                    if (outPut != null) { //is the port already mapped?
                    //if (false) {
                        print("UPnP port forwarding not enabled: port is already mapped", ConsoleColors.RED);
                        if (outPut.get("NewInternalPort").equalsIgnoreCase("" + port.portIn) &&
                                outPut.get("NewInternalClient").equalsIgnoreCase(port.ip)){
                            print("It is the same set port");
                        } else {
                            print(outPut.get("NewPortMappingDescription") + " is use the port at : " + outPut.get("NewInternalClient"), ConsoleColors.RED_BOLD);
                        }

                        if (debugFlag) {
                            print("The output is: ", ConsoleColors.PURPLE);
                            for (String key : outPut.keySet()) {
                                print("-> \"" + key + "\" : \"" + outPut.get(key).replaceAll("\\n", "\\\\n") + "\"", ConsoleColors.PURPLE);
                            }
                        }
                    } else if (port.type.equalsIgnoreCase("tcp")) { //try to map port
                        if (UPnP.openPortTCP(port.portOut, port.portIn, port.ip, port.name)){
                            print("UPnP port forwarding enabled", ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLUE);
                        }else {
                            print("UPnP port forwarding failed", ConsoleColors.RED_BOLD);
                        }
                    } else if (port.type.equalsIgnoreCase("udp")){
                        if (UPnP.openPortUDP(port.portOut, port.portIn, port.ip, port.name)){
                            print("UPnP port forwarding enabled", ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLUE);
                        }else {
                            print("UPnP port forwarding failed", ConsoleColors.RED_BOLD);
                        }
                    } else {
                        print("UPnP port forwarding failed", ConsoleColors.RED_BOLD);
                    }
                    print("");
                }

                if ((((double) config.waitTime)/60/60) >= 1){
                    print("Waiting for " + (((double) config.waitTime)/60/60) + " hr's");
                }else if ((((double) config.waitTime)/60) >= 1){
                    print("Waiting for " + (((double) config.waitTime)/60) + " min's");
                }else {
                    print("Waiting for " + config.waitTime + " sec's");
                }

                TimeUnit.SECONDS.sleep(config.waitTime);
                print("Restart Now");
                print("");
                print("");
            }
        } else {
            print("UPnP is not available", ConsoleColors.RED_BOLD);
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
                if (color == ConsoleColors.RED) {
                    bw.write(time + " : ERROR \"" + message + "\" ERROR\n");
                }else if (color == ConsoleColors.RED_BOLD) {
                    bw.write(time + " : !ERROR! \"" + message + "\" !ERROR!\n");
                }else if (color == ConsoleColors.PURPLE) {
                    bw.write(time + " : DEBUG " + message + " DEBUG\n");
                }else if (color == (ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLUE)) {
                    bw.write(time + " : !GOOD! \"" + message + "\" !GOOD!\n");
                }else {
                    bw.write(time + " : " + message + "\n");
                }
                bw.close();

                File f2 = new File(config.importantLogFilePath);
                if (!f2.exists()) {
                    f2.createNewFile();
                }

                FileWriter fileWritter2 = new FileWriter(f2.getName(), true);
                BufferedWriter bw2 = new BufferedWriter(fileWritter2);
                if (color == ConsoleColors.RED_BOLD) {
                    bw2.write(time + " : !ERROR! \"" + message + "\" !ERROR!\n");
                }else if (color == (ConsoleColors.GREEN_BACKGROUND + ConsoleColors.BLUE)) {
                    bw2.write(time + " : !GOOD! \"" + message + "\" !GOOD!\n");
                }
                bw2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
