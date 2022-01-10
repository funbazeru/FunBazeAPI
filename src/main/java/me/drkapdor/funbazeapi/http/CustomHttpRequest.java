package me.drkapdor.funbazeapi.http;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;

@Deprecated
public class CustomHttpRequest {

    private final HttpURLConnection httpConn;
    private final String boundary;
    private final OutputStream outputStream;
    private final PrintWriter writer;

    public CustomHttpRequest(String requestURL, String method, Collection<RequestProperty> properties) throws IOException {
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        boundary = Long.toHexString(System.currentTimeMillis());
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setRequestMethod(method);
        for (RequestProperty property : properties) {
            if (property.getValue().contains("multipart/form-data"))
                httpConn.setRequestProperty(property.getKey(), property.getValue() + "; boundary=" + boundary);
            else httpConn.setRequestProperty(property.getKey(), property.getValue());
        }
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8), true);
    }

    public CustomHttpRequest(String requestURL, String method) throws IOException {
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        boundary = Long.toHexString(System.currentTimeMillis());
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setRequestMethod(method);
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpConn.setRequestProperty("Content-Type", "text/html; charset=utf-8");
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8),
                true);
    }

    public void addFormField(String name, String value) {
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append("\r\n");
        writer.append("Content-Type: text/plain; charset=utf-8").append("\r\n");
        writer.append("\r\n").append(value).append("\r\n").flush();
    }

    public void addFilePart(String fieldName, File file)
            throws IOException {
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(fieldName).append("\"").append("\r\n");
        writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(file.getName())).append("\r\n");
        writer.append("\r\n").flush();
        Files.copy(file.toPath(), outputStream);
        outputStream.flush();
        writer.append("\r\n").flush();
    }

    public void addFilePart(File file)
            throws IOException {
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(file.getName()).append("\"").append("\r\n");
        writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(file.getName())).append("\r\n");
        writer.append("\r\n").flush();
        Files.copy(file.toPath(), outputStream);
        outputStream.flush();
        writer.append("\r\n").flush();
    }

    public void addImagePart(String fieldName, BufferedImage image, String format)
            throws IOException {
        File file = new File("image." + format.toLowerCase());
        ImageIO.write(image, format.toUpperCase(), file);
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"").append(fieldName).append("\"; filename=\"").append(file.getName()).append("\"").append("\r\n");
        writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(file.getName())).append("\r\n");
        writer.append("\r\n").flush();
        Files.copy(file.toPath(), outputStream);
        outputStream.flush();
        writer.append("\r\n").flush();
    }

    public void addImagePart(BufferedImage image, String format)
            throws IOException {
        File file = new File("image." + format.toLowerCase());
        ImageIO.write(image, format.toUpperCase(), file);
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"").append(file.getName()).append("\"").append("\r\n");
        writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(file.getName())).append("\r\n");
        writer.append("\r\n").flush();
        Files.copy(file.toPath(), outputStream);
        outputStream.flush();
        writer.append("\r\n").flush();
    }

    public void addJsonPayload(String json) {
        try {
            byte[] input = json.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public CustomHttpResponse finish() {
        CustomHttpResponse response = new CustomHttpResponse();
        try {
            writer.append("--").append(boundary).append("--").append("\r\n").flush();
            writer.close();
            int status = httpConn.getResponseCode();
            response.setCode(status);
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                content.append(line);
            reader.close();
            httpConn.disconnect();
            response.setContent(content.toString());
        } catch (IOException ignored) {
        }
        return response;
    }
}
