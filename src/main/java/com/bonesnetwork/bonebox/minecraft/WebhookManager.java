package com.bonesnetwork.bonebox.minecraft;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
            int status = con.getResponseCode();
            InputStreamReader streamReader;

            if (status > 299) {
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

            JsonObject jo = new Gson().fromJson(content.toString(), JsonObject.class);
            JsonArray ja = jo.getAsJsonArray();
            for (JsonElement jsonElement : ja) {
                JsonObject je = jsonElement.getAsJsonObject();
                if (je.get("name").getAsString().equals("BoneBox" + channelid)) {
                    return je.get("url").getAsString();
                }
            }

            URL urlp = new URL("https://discord.com/api/v9/channels/"+channelid+"/webhooks");
            HttpURLConnection conp = (HttpURLConnection) urlp.openConnection();
            conp.setRequestMethod("POST");
            conp.setConnectTimeout(5000);
            conp.setReadTimeout(5000);

            Map<String, String> parameters = new HashMap<>();
            parameters.put("name", "BoneBox"+channelid);

            conp.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(conp.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();

            int statusp = conp.getResponseCode();
            InputStreamReader streamReaderp;

            if (statusp > 299) {
                return null;
            } else {
                streamReaderp = new InputStreamReader(conp.getInputStream());
            }

            BufferedReader inp = new BufferedReader(streamReaderp);
            String inputLinep;
            StringBuilder contentp = new StringBuilder();
            while ((inputLinep = inp.readLine()) != null) {
                contentp.append(inputLinep);
            }
            in.close();
            con.disconnect();
            JsonObject jop = new Gson().fromJson(contentp.toString(), JsonObject.class);
            return jop.get("url").getAsString();
        } catch(Exception ignored) {
            return null;
        }
    }
}
