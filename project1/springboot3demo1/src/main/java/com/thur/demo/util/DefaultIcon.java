package com.thur.demo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class DefaultIcon {
    @Autowired
    private ResourceLoader resourceLoader;

    public String defaultIcon() {
        try {
            int i = (int) (Math.random() * 2) + 1;

            String imagePath = "classpath:img/defaultIcon/" + i + ".png";
            Resource resource = resourceLoader.getResource(imagePath);
            byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            return "data:image/png;base64," + base64String;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String defaultProductIcon() {
        try {
            String imagePath = "classpath:img/defaultIcon/question.png";
            Resource resource = resourceLoader.getResource(imagePath);
            byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            return "data:image/png;base64," + base64String;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String defaultShopIcon() {
        try {
            String imagePath = "classpath:img/defaultIcon/question.png";
            Resource resource = resourceLoader.getResource(imagePath);
            byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            return "data:image/png;base64," + base64String;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> picRandomStringList(String resourcePath) throws IOException {
        String folderPath = "classpath:" + resourcePath;
        Resource resource = resourceLoader.getResource(folderPath);
        Path path = Paths.get(resource.getURI());
        List<String> pics = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path, Files::isReadable)) {
            for (Path entry : stream) {
                pics.add(entry.getFileName().toString());
            }
        }

        return pics;

    }

    public String picRandom(String resourcePath, List<String> picNames) throws IOException {
        int num = (int) (Math.random() * picNames.size()) + 1;
        String imagePath = "classpath:" + resourcePath + picNames.get(num);
        System.out.println(imagePath);
        String imgFormat = imagePath.split("\\.")[1];

        try {
            Resource resource = resourceLoader.getResource(imagePath);
            byte[] imageBytes = Files.readAllBytes(resource.getFile().toPath());
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            return "data:image/" + imgFormat + ";base64," + base64String;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> nameRandomStringList(String resourcePath) throws IOException {
        String folderPath = "classpath:file/" + resourcePath + ".txt";
        Resource resource = resourceLoader.getResource(folderPath);
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        // lines.forEach(System.out::println);
        return lines;
    }
}
