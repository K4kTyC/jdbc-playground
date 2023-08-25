package org.example.controller;

import io.javalin.http.Context;
import org.example.model.TestEntity;
import org.example.service.TestService;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.util.Arrays;
import java.util.List;

public class TestController {

    private final TestService service;

    public TestController(MariaDbPoolDataSource dataSource) {
        service = new TestService(dataSource);
    }



    public void poolTest(Context context) {
        service.poolTest();
    }

    public void getAll(Context context) {
        List<TestEntity> entities = service.getAll();
        context.json(entities);
    }

    public void get(Context context) {
        Long id = Long.valueOf(context.pathParam("id"));
        TestEntity entity = service.getById(id);
        context.json(entity);
    }

    public void save(Context context) {
        TestEntity entity = context.bodyAsClass(TestEntity.class);
        service.save(entity);
    }

    public void bulkSave(Context context) {
        List<TestEntity> entities = Arrays.asList(context.bodyAsClass(TestEntity[].class));
        Boolean eachInSingleTransaction = Boolean.parseBoolean(context.pathParam("eachInSingleTransaction"));
        service.bulkSave(entities, eachInSingleTransaction);
    }

    public void update(Context context) {
        TestEntity entity = context.bodyAsClass(TestEntity.class);
        service.update(entity);
    }

    public void bulkUpdate(Context context) {
        List<TestEntity> entities = Arrays.asList(context.bodyAsClass(TestEntity[].class));
        Boolean eachInSingleTransaction = Boolean.parseBoolean(context.pathParam("eachInSingleTransaction"));
        service.bulkUpdate(entities, eachInSingleTransaction);
    }

    public void deleteAll(Context context) {
        service.deleteAll();
    }

    public void delete(Context context) {
        Long id = Long.valueOf(context.pathParam("id"));
        service.deleteById(id);
    }
}
