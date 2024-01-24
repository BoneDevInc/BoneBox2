package com.bonesnetwork.bonebox.minecraft;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WebhookManager {
    public static String get(String channelid) {
        try {
            URL url = new URL("https://discord.com/api/v9/channels/"+channelid+"/webhooks");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setRequestProperty("Authorization", "Bot "+BoneBoxPlugin.getInstance().getConfig().getString("token"));
            con.setRequestProperty("Content-Type", "application/json");
            int status = con.getResponseCode();
            InputStreamReader streamReader;

            if (status > 299) {
                BoneBoxPlugin.getInstance().getLogger().severe("Unable to get webhook with status " + status);
                return null;
            } else {
                streamReader = new InputStreamReader(con.getInputStream());
            }

            BufferedReader in = new BufferedReader(streamReader);
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            JsonArray ja = new Gson().fromJson(content.toString(), JsonArray.class);
            for (JsonElement jsonElement : ja) {
                JsonObject je = jsonElement.getAsJsonObject();
                if (je.get("name").getAsString().equals("BoneBox" + channelid)) {
                    return je.get("url").getAsString();
                }
            }

            HttpClient httpClient = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://discord.com/api/v9/channels/"+channelid+"/webhooks"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bot " + BoneBoxPlugin.getInstance().getConfig().getString("token"))
                    .POST(HttpRequest.BodyPublishers.ofString("{\"name\": \"BoneBox"+channelid+"\"}"))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusp = response.statusCode();
            String contentp = response.body();

            if (statusp > 299) {
                BoneBoxPlugin.getInstance().getLogger().severe("Unable to create webhook with status " + statusp);
                BoneBoxPlugin.getInstance().getLogger().severe(contentp);
                return null;
            }

            JsonObject jop = new Gson().fromJson(contentp, JsonObject.class);
            return jop.get("url").getAsString();
        } catch(Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
}
