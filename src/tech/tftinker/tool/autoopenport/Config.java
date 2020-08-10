package tech.tftinker.tool.autoopenport;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Config {
    public long waitTime;
    public String logFilePath;
    public String importantLogFilePath;
    public Port[] ports;

    public Config(long waitTime, Port[] ports, String logFilePath, String importantLogFilePath){
        this.waitTime = waitTime;
        this.ports = ports;
        this.logFilePath = logFilePath;
        this.importantLogFilePath = importantLogFilePath;
    }

    public static Config readConfigFile(String path){

        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try {
            //Read JSON file
            Object obj = jsonParser.parse(new FileReader(path));

            // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
            JSONObject jsonObject = (JSONObject) obj;

            long waitTime = (long) jsonObject.get("waitTime");
            String logFilePath = (String) jsonObject.get("logFile");
            String importantLogFilePath = (String) jsonObject.get("importantLogFile");

            JSONArray portsJSON = (JSONArray) jsonObject.get("ports");

            Port[] ports = new Port[portsJSON.size()];

            for (int i = 0; i < portsJSON.size(); i++) {
                JSONObject port = (JSONObject) portsJSON.get(i);
                ports[i] = new Port((long) port.get("portOut"),
                        (long) port.get("portIn"),
                        (String) port.get("ip"),
                        (String) port.get("type"),
                        (String) port.get("name"));
            }

            return new Config(waitTime, ports, logFilePath, importantLogFilePath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
