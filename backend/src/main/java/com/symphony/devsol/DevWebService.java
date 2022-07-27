package com.symphony.devsol;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// @Profile("dev")
// @RestController
// @ResponseBody
public class DevWebService {
    private final String root = "http://localhost:3000/";
    private final RestTemplate restTemplate;
    HttpClient client = HttpClient.newHttpClient();

    public DevWebService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping(value = "/", produces = "text/html; charset=utf-8")
    public String devIndex() {
        return this.restTemplate.getForObject(root, String.class);
    }

    @GetMapping(value = "/static/js/bundle.js", produces = "application/javascript; charset=utf-8")
    public String devBundle() {
        return this.restTemplate.getForObject(root + "static/js/bundle.js", String.class);
    }

    @GetMapping(value = "/static/media/{file}.woff2", produces = "font/woff2")
    public byte[] devFontsWoff(@PathVariable String file) {
        HttpRequest request = HttpRequest.newBuilder().GET()
            .uri(URI.create(root + "static/media/" + file + ".woff2"))
            .build();
        try {
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // return this.restTemplate.getForObject(root + "static/media/" + file + ".woff", Object.class);
    }
    /*
    @GetMapping(value = "/static/media/{file}.woff2", produces = "font/woff2")
    public Object devFontsWoff2(@PathVariable String file) {
        return this.restTemplate.getForObject(root + "static/media/" + file + ".woff2", Object.class);
    }

    @GetMapping(value = "/static/media/{file}.ttf", produces = "font/ttf")
    public Object devFontsTtf(@PathVariable String file) {
        return this.restTemplate.getForObject(root + "static/media/" + file + ".ttf", Object.class);
    }*/
}
