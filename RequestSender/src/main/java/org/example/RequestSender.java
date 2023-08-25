package org.example;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class RequestSender {

    public static void main(String[] args) throws URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:7070/test/pool"))
                .GET()
                .build();

        Scanner scanner = new Scanner(System.in);
        do {
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        } while (scanner.nextLine().isBlank());
    }
}