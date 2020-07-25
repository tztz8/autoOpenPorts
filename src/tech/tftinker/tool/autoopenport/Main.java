package tech.tftinker.tool.autoopenport;

import org.chris.portmapper.PortMapperStarter;
import org.chris.portmapper.model.PortMapping;

public class Main {

    public static void main(String[] args) {
        // load config file
        Config config = Config.readConfigFile("./config.json");
        if (config == null){
            System.out.println("Error has happen");
        } else {
            System.out.println("Wait Time (sec): " + (config.waitTime/1000));
            for (int i = 0; i < config.ports.length; i++) {
                System.out.println(config.ports[i].name);
            }
        }

        // load portmapper
        for (int i = 0; i < config.ports.length; i++) {
            System.out.print("PortMapper : ");
            for (int j = 0; j < config.ports[i].portMapperAddArgs.length; j++) {
                System.out.print(config.ports[i].portMapperAddArgs[j] + " ");
            }
            System.out.print("\n");
            PortMapperStarter.main(config.ports[i].portMapperAddArgs);
        }
    }
}
