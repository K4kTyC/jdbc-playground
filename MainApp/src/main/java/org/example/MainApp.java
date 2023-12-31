package org.example;

import io.javalin.Javalin;
import org.example.controller.Controller;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.sql.SQLException;

import static io.javalin.apibuilder.ApiBuilder.*;

public class MainApp {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mariadb://localhost:3306/jdbc?user=root&password=temp&maxPoolSize=5";
        MariaDbPoolDataSource dataSource = new MariaDbPoolDataSource(url);

        Controller controller = new Controller(dataSource);

        Javalin.create()
                .routes(() -> {
                    path("/test", () -> {
                        get("/pool", controller::poolTest);
                        get("/isolation/init", controller::isolationTestInit);
                        get("/isolation/transaction1", controller::isolationTestTransaction1);
                        get("/isolation/transaction2", controller::isolationTestTransaction2);
                    });

                    path("/entities", () -> {
                        get("/entities", controller::getAll);
                        get("/entities/{id}", controller::get);
                        post("/entities", controller::save);
                        post("/entities/{eachInSingleTransaction}", controller::bulkSave);
                        put("/entities", controller::update);
                        put("/entities/{eachInSingleTransaction}", controller::bulkUpdate);
                        delete("/entities", controller::deleteAll);
                        delete("/entities/{id}", controller::delete);
                    });
                })
                .start(7070);
    }
}