package com.alerthub.sms.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class SMSApiClient {

    private final String baseUrl;
    private final String apiKey;
    private final String apiToken;

    public SMSApiClient(
            @Value("${sms.api.base-url}") String baseUrl,
            @Value("${sms.api.key}") String apiKey,
            @Value("${sms.api.token}") String apiToken) {

        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.apiKey = apiKey;
        this.apiToken = apiToken;
    }

    public String sendSMS(String to, String message) throws IOException {
        StringBuilder params = new StringBuilder();
        params.append("api_key=").append(encode(apiKey));
        params.append("&api_token=").append(encode(apiToken));
        params.append("&to=").append(encode(to));
        params.append("&message=").append(encode(message));

        return doPost("/api/sms/send", params.toString());
    }

    private String doPost(String endpoint, String params) throws IOException {
        URL url = new URL(baseUrl + endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes(StandardCharsets.UTF_8));
        }

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 300)
                ? conn.getInputStream()
                : conn.getErrorStream();

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        log.info("SMS API response status={}, body={}", status, response);
        return response.toString();
    }

    private String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }
}
