import logger.Logger;
import logger.factory.LoggerFactory;
import logger.impl.SimpleLogger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;



class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            log.error("Укажи файл в аргументах");
            return;
        }
        var inputPath = Path.of(args[0]);
        log.debug(String.format("Получен файл по пути %s", inputPath.toAbsolutePath()));

        var fileName = inputPath.getFileName();

        byte[] pureData;
        try {
            pureData = Files.readAllBytes(inputPath);
        } catch (IOException e) {
            log.error("Ошибка чтения файла", e);
            return;
        }
        log.debug(String.format("Получена дата файла размера %d", pureData.length));

        var data = Base64.getEncoder().encodeToString(pureData);

        var jsonPayload = String.format(
                "{\n  \"name\": \"%s\",\n  \"data\": \"%s\"\n}",
                fileName, data
        );

        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/obfuscator"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            log.error("Ошибка запроса", e);
            return;
        }

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            log.info("Файл успешно отправлен. Status=" + response.statusCode());
        } else {
            log.error("Ошибка сервера. Status=" + response.statusCode());
            log.debug("Ответ сервера: " + response.body());
        }
    }
}