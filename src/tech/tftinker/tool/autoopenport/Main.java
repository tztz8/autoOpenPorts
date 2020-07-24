package tech.tftinker.tool.autoopenport;

public class Main {

    public static void main(String[] args) {
        Config config = Config.readConfigFile("./config.json");
        if (config == null){
            System.out.println("Error has happen");
        } else {
            System.out.println("Wait Time (sec): " + (config.waitTime/1000));
            for (int i = 0; i < config.ports.length; i++) {
                System.out.println(config.ports[i].name);
            }
        }

    }
}
