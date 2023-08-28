package org.example;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.util.Scanner;

public class RequestSender {

    private static final Scanner scanner = new Scanner(System.in);
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static SenderState state = SenderState.RETURN_TO_MENU;

    private static final String POOL_TEST_URI = "http://localhost:7070/test/pool";
    private static final String ISOLATION_TEST_INIT_URI = "http://localhost:7070/test/isolation/init";
    private static final String ISOLATION_TEST_T1_URI = "http://localhost:7070/test/isolation/transaction1";
    private static final String ISOLATION_TEST_T2_URI = "http://localhost:7070/test/isolation/transaction2";


    public static void main(String[] args) throws URISyntaxException {
        while (!SenderState.EXIT.equals(state)) {
            if (SenderState.RETURN_TO_MENU.equals(state)) {
                printMenu();
                state = SenderState.MENU;
            }

            switch (scanner.nextLine()) {
                case "1" -> {
                    state = SenderState.POOL_TEST;
                    System.out.println("=== Connection Pool Test ===");
                    System.out.println("App will simply send request every time you hit 'Enter'. Every request will be processing for 10 seconds. Type 'q' to exit");
                    startPoolTest();
                    System.out.println("============================");
                }
                case "2" -> {
                    state = SenderState.ISOLATION_TEST;
                    System.out.println("=== Transaction Isolation Test ===");
                    System.out.println("By default, isolation level is set to Repeatable Read");
                    System.out.println("Type 'rc' to change isolation level to Read Committed.");
                    System.out.println("Type 'rr' to change isolation level to Repeatable Read.");
                    System.out.println("Type 'se' to change isolation level to Serializable.");
                    System.out.println();
                    System.out.println("Type 'init' to clear the table and create test entities.");
                    System.out.println("Type 't1' to start the first transaction. It will show the state of the entities before and after the changes made by the second transaction with a 10 sec delay.");
                    System.out.println("Type 't2' to start the second transaction. It will update the existing entities and create a new one.");
                    System.out.println("Type 'q' to exit.");
                    startIsolationTest();
                    System.out.println("==================================");
                }
                case "q" -> state = SenderState.EXIT;
            }
        }
    }

    private static void printMenu() {
        System.out.println("Select test to run:");
        System.out.println("1. Connection Pool test");
        System.out.println("2. Isolation behaviour test");
        System.out.println("'q' to Exit");
    }

    private static void startPoolTest() throws URISyntaxException {
        while (SenderState.POOL_TEST.equals(state)) {
            switch (scanner.nextLine()) {
                case "" -> sendPoolTestRequest();
                case "q" -> state = SenderState.RETURN_TO_MENU;
            }
        }
    }

    private static void startIsolationTest() throws URISyntaxException {
        int isolationLevel = Connection.TRANSACTION_REPEATABLE_READ;
        while (SenderState.ISOLATION_TEST.equals(state)) {
            switch (scanner.nextLine()) {
                case "rc" -> {
                    isolationLevel = Connection.TRANSACTION_READ_COMMITTED;
                    System.out.println("Isolation level changed to Read Committed.");
                }
                case "rr" -> {
                    isolationLevel = Connection.TRANSACTION_REPEATABLE_READ;
                    System.out.println("Isolation level changed to Repeatable Read.");
                }
                case "se" -> {
                    isolationLevel = Connection.TRANSACTION_SERIALIZABLE;
                    System.out.println("Isolation level changed to Serializable.");
                }
                case "init" -> sendIsolationTestInitRequest();
                case "t1" -> sendIsolationTestT1Request(isolationLevel);
                case "t2" -> sendIsolationTestT2Request();
                case "q" -> state = SenderState.RETURN_TO_MENU;
            }
        }
    }

    private static void sendPoolTestRequest() throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(POOL_TEST_URI))
                .GET()
                .build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }


    private static void sendIsolationTestInitRequest() throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ISOLATION_TEST_INIT_URI))
                .GET()
                .build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void sendIsolationTestT1Request(int isolationLevel) throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ISOLATION_TEST_T1_URI + "?isolation=" + isolationLevel))
                .GET()
                .build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void sendIsolationTestT2Request() throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ISOLATION_TEST_T2_URI))
                .GET()
                .build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

}