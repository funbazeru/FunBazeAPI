package me.drkapdor.funbazeapi.utility.mojang;

import com.google.gson.JsonObject;
import me.drkapdor.funbazeapi.ApiPlugin;
import me.drkapdor.funbazeapi.http.CustomHttpRequest;
import me.drkapdor.funbazeapi.http.CustomHttpResponse;
import me.drkapdor.funbazeapi.http.RequestProperty;

import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MojangUtils {

    private static final LinkedList<MojangAccount> accounts;
    private static int nextAccountId = 0;

    static {
        accounts = new LinkedList<>();
        accounts.add(new MojangAccount("eduard150771@gmail.com", "I2*rB+7i5&3bUS({=dg9F=])ub$N2yf", "8e0ba772406240c4bc416538f9173a6c"));
    }

    public static MojangAccount currentAccount() {
        return accounts.get(nextAccountId);
    }

    public static AccessToken nextToken() {
        String uuid = null;
        String accessToken = null;
        try {
            List<RequestProperty> properties = Arrays.asList(
                    new RequestProperty("Content-Type", "application/json"),
                    new RequestProperty("User-Agent", "Mozilla/5.0")
            );
            CustomHttpRequest request = new CustomHttpRequest("https://authserver.mojang.com/authenticate", "POST", properties);
            JsonObject payload = new JsonObject();
            JsonObject agent = new JsonObject();
            agent.addProperty("name", "Minecraft");
            agent.addProperty("version", 1);
            payload.add("agent", agent);
            if (nextAccountId < accounts.size() - 1)
                nextAccountId++;
            else nextAccountId = 0;
            MojangAccount account = currentAccount();
            payload.addProperty("username", account.getUserName());
            payload.addProperty("password", account.getPassword());
            payload.addProperty("clientToken", UUID.randomUUID().toString());
            payload.addProperty("requestUser", true);
            request.addJsonPayload(payload.toString());
            CustomHttpResponse response = request.finish();
            if (response.getCode() == HttpURLConnection.HTTP_OK) {
                JsonObject jsonResponse = ApiPlugin.getJsonParser().parse(response.getContent()).getAsJsonObject();
                uuid = jsonResponse.get("clientToken").getAsString();
                accessToken = jsonResponse.get("accessToken").getAsString();
            }
        } catch (Exception ignored) {
        }
        return new AccessToken(uuid, accessToken);
    }

    @Deprecated
    public static boolean uploadSkin(AccessToken accessToken, String variant, String url) {
        try {
            List<RequestProperty> properties = Arrays.asList(
                    new RequestProperty("Content-Type", "application/json"),
                    new RequestProperty("Authorization", ("Bearer " + accessToken))
            );
            CustomHttpRequest request = new CustomHttpRequest("https://api.minecraftservices.com/minecraft/profile/skins", "POST", properties);
            JsonObject payload = new JsonObject();
            payload.addProperty("variant", variant);
            payload.addProperty("url", url);
            request.addJsonPayload(payload.toString());
            return request.finish().getCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean uploadSkin(AccessToken accessToken, String variant, BufferedImage image) {
        try {
            List<RequestProperty> properties = Arrays.asList(
                    new RequestProperty("Authorization", ("Bearer " + accessToken)),
                    new RequestProperty("Content-Type", "multipart/form-data")
            );
            CustomHttpRequest request = new CustomHttpRequest("https://api.minecraftservices.com/minecraft/profile/skins", "POST", properties);
            request.addFormField("variant", variant);
            request.addImagePart("file", image, "PNG");
            return request.finish().getCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception exception) {
            return false;
        }
    }
}
