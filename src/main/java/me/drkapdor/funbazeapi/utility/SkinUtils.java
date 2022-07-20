package me.drkapdor.funbazeapi.utility;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.drkapdor.funbazeapi.FunBazeApiPlugin;
import me.drkapdor.funbazeapi.api.user.records.UserSkin;
import me.drkapdor.funbazeapi.utility.mojang.MojangUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;

/**
 * Утилита для работы с кастомными скинами
 * @author DrKapdor
 */

public class SkinUtils {

    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    private static final JsonParser JSON_PARSER = new JsonParser();

    /**
     * Получить изображение развёртки скина премиум-аккаунта
     *
     * @param account UUID
     * @return Изображение развёртки скина (64x64 или 64x32)
     */

    public static BufferedImage getSkinImage(String account) {
        try {
            InputStreamReader reader = new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + account + "?unsigned=false").openStream());
            JsonObject response = FunBazeApiPlugin.getJsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            JsonObject decoded = FunBazeApiPlugin.getJsonParser().parse(new String(Base64.getDecoder().decode(response.get("value").getAsString()))).getAsJsonObject();
            String skinUrl = decoded.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
            return ImageIO.read(new URL(skinUrl));
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * Получить изображение лица скина
     *
     * @param value Закодированное в формате Base64 значение скина
     * @return Изображение лица скина (8x8)
     */

    public static BufferedImage getSkinFace(String value) {
        byte[] imageByte = Base64.getDecoder().decode(value);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
        try {
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonElement element = new JsonParser().parse(reader);
        String url = element.getAsJsonObject()
                .getAsJsonObject("textures")
                .getAsJsonObject("SKIN")
                .getAsJsonPrimitive("url")
                .getAsString();
        try {
            return ImageIO.read(new URL(url)).getSubimage(8, 8, 8, 8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Получить изображение лица скина
     *
     * @param image Изображение развёртки скина
     * @return Изображение лица скина (8x8)
     */

    public static BufferedImage getSkinFace(File image) {
        try {
            return ImageIO.read(image).getSubimage(8, 8, 8, 8);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Получить изображение лица скина
     *
     * @param image Изображение развёртки скина
     * @return Изображение лица скина (8x8)
     */

    public static BufferedImage getSkinFace(BufferedImage image) {
        return image.getSubimage(8, 8, 8, 8);
    }

    /**
     * Наложить изображение одного скина на другой
     *
     * @param main Скин, на который осуществляется наложение
     * @param additional Скин, накладываемый на основной
     * @return Изображение комбинации основного и накладываемого скина (64x64 или 64x32)
     */

    public static BufferedImage combineSkins(BufferedImage main, BufferedImage additional) {
        BufferedImage canvas = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics canvasGraphics = canvas.getGraphics();
        BufferedImage head = new BufferedImage(32, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics headGraphics = head.getGraphics();
        headGraphics.drawImage(main, 0, 0, null);
        BufferedImage body = new BufferedImage(56, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics bodyGraphics = body.getGraphics();
        bodyGraphics.drawImage(main, 0, -16, null);
        canvasGraphics.drawImage(head, 0, 0, null);
        if (main.getHeight() != 64) {
            BufferedImage arm = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics armsGraphics = arm.getGraphics();
            armsGraphics.drawImage(main, -40, -16, null);
            canvasGraphics.drawImage(arm, 32, 48, null);
        } else {
            BufferedImage extra = new BufferedImage(32, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics subBodyGraphics = extra.getGraphics();
            subBodyGraphics.drawImage(main, -16, -48, null);
            canvasGraphics.drawImage(extra, 16, 48, null);
        }
        canvasGraphics.drawImage(body, 0, 16, null);
        canvasGraphics.drawImage(additional, 0, 0, null);
        return canvas;
    }

    /**
     * Получить URL изображения скина из закодированного Base64 значения
     *
     * @param base64String Закодированное значение
     * @return URL изображения развёртки скина (64x64 или 64x32)
     */

    public static String getSkinURL(String base64String) {
        String encoded = new String(BASE64_DECODER.decode(base64String));
        JsonObject json = JSON_PARSER.parse(encoded).getAsJsonObject();
        JsonObject textures = json.get("textures").getAsJsonObject();
        if (textures.entrySet().size() != 0)
            return textures.get("SKIN").getAsJsonObject().get("url").getAsString();
        return null;
    }

    /**
     * Получить изображение развёртки скина зарезервированного аккаунта FunBaze
     *
     * @return Изображения развёртки скина (64x64 или 64x32)
     */

    public static UserSkin getCustomSkin() {
        String value = "";
        String signature = "";
        try {
            InputStreamReader reader = new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + MojangUtils.currentAccount().getUUID() + "?unsigned=false").openStream());
            JsonObject response = FunBazeApiPlugin.getJsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            value = response.get("value").getAsString();
            signature = response.get("signature").getAsString();
        } catch (Exception ignored) {
        }
        return new UserSkin(value, signature);
    }
}
