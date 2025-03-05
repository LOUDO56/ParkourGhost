package fr.loudo.parkourGhost.utils;


import fr.loudo.parkourGhost.ParkourGhost;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CheckVersion {

    private static String latestVersion;
    private static boolean newVersionAvailable = false;

    public static void verify() throws IOException, ParseException {

        ParkourGhost.getPlugin().getLogger().info("Checking for new update...");

        URL url = new URL("https://api.github.com/repos/LOUDO56/ParkourGhost/releases/latest");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int status = conn.getResponseCode();

        if(status != 200) {
            throw new RuntimeException("Github API down, can't check new update.");
        }

        StringBuilder inline = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());

        while(scanner.hasNext()) {
            inline.append(scanner.nextLine());
        }

        JSONParser parse = new JSONParser();
        JSONObject dataObj = (JSONObject) parse.parse(inline.toString());
        latestVersion = ((String) dataObj.get("tag_name")).replace("v", "");

        System.out.println(latestVersion);
        System.out.println(ParkourGhost.getPlugin().getDescription().getVersion());

        if(!ParkourGhost.getPlugin().getDescription().getVersion().equals(latestVersion)) {
            newVersionAvailable = true;
            notifyConsole();
        }

    }

    public static void notifyConsole() {
        if(newVersionAvailable) ParkourGhost.getPlugin().getLogger().info("New update available: " + latestVersion);
    }

    public static void notifyPlayer(Player player) {
        if(newVersionAvailable) player.sendMessage("[ParkourGhost] §aParkourGhost has a new update available: §e" + latestVersion + "§a. Download it here: §ehttps://github.com/LOUDO56/ParkourGhost/releases");
    }

}