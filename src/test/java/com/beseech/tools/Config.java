package com.beseech.tools;

import com.beseech.model.TestConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Config {

    private static String readStringFromFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public static TestConfig load() throws Exception{
        String json = Config.readStringFromFile("config.json");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);

        TestConfig testConfig = new com.beseech.model.TestConfig();
        testConfig.apiBasicUrl = jsonNode.get("apiBasicUrl").toString().replaceAll("\"", "");
        testConfig.protocol = jsonNode.get("protocol").toString().replaceAll("\"", "");
        testConfig.qaToken = jsonNode.get("qaToken").toString().replaceAll("\"", "");
        testConfig.urlPostfix = jsonNode.get("urlPostfix").toString().replaceAll("\"", "");
        testConfig.urlSubdomainPart = jsonNode.get("urlSubdomainPart").toString().replaceAll("\"", "");

        return testConfig;
    }
}
