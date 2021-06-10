package com.uzm.common.plugin.updater;

import com.uzm.common.plugin.abstracts.UzmPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author JotaMPê
 */
public class Updater {

    private UzmPlugin plugin;
    @Getter(AccessLevel.PUBLIC)
    private String lastestName;


    public Updater(UzmPlugin plugin) {
        this.plugin = plugin;
    }

    public void run() {

        Bukkit.getConsoleSender().sendMessage("§6[uzm.commons | Updater] §7Looking for new updates...");

        JSONObject json = getVersion();
        String latest = json.get("version").toString();
        String current = this.plugin.getDescription().getVersion();
        if (latest == null) {
            Bukkit.getConsoleSender().sendMessage("§6[uzm.commons | Updater] §cThe connection with GoogleDrive api can't be estabilished.");
        } else {

            if (latest.equals(current)) {
                Bukkit.getConsoleSender().sendMessage("§6[uzm.commons | Updater] §7No updates found!");

            } else {
                Bukkit.getConsoleSender().sendMessage("§6[uzm.commons | Updater] §aNew version found:");
                Bukkit.getConsoleSender().sendMessage("§6[uzm.commons | Updater] §c" + current + " to §a" + latest);
                Bukkit.getConsoleSender().sendMessage("§6[uzm.commons | Updater] §7Prepare to update and download file...");
                downloadUpdate(json.get("download").toString(), latest);


            }
        }
    }

    public void downloadUpdate(String url, String latest) {
        try {
            String versionExtractor = latest.replace(" build (", "-").replace(")", "");
            File file = new File("plugins/" + this.plugin.getName() + "/update", this.plugin.getName() + ".jar");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            this.lastestName = this.plugin.getName() + "-" + versionExtractor + ".jar";

            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            int max = connection.getContentLength();
            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);

            int oneChar;
            Bukkit.getConsoleSender().sendMessage("§6[uzm.commons | Updater] §7File size: §b" + (max / 1024) + "kb");

            while ((oneChar = in.read()) != -1) {
                bout.write(oneChar);
            }
            Bukkit.getConsoleSender().sendMessage("§6[uzm.commons | Updater] §fDownload finished, when the server restarts, the plugin will be update automatically.");
            in.close();
            bout.close();
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage("§6[uzm.commons | Updater] §cDownload failed -" + ex.getMessage());
        }
    }

    private static JSONObject getVersion() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://drive.google.com/uc?id=1FE_2t3r9yg4WwTJ5uoqDIAcTnVCQAyjq&export=download").openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            JSONObject object = (JSONObject) new JSONParser().parse(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            return object;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}