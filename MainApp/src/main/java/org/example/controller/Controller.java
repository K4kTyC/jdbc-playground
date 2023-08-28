package org.example.controller;

import io.javalin.http.Context;
import org.example.model.Entity;
import org.example.service.CrudService;
import org.example.service.TestService;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Controller {

    private final CrudService crudService;
    private final TestService testService;

    public Controller(MariaDbPoolDataSource dataSource) {
        crudService = new CrudService(dataSource);
        testService = new TestService(dataSource);
    }


    public void poolTest(Context context) {
        testService.poolTest();
    }

    public void isolationTestInit(Context context) {
        testService.isolationTestInit();
    }

    public void isolationTestTransaction1(Context context) {
        Integer isolationLevel = Integer.valueOf(Objects.requireNonNull(context.queryParam("isolation")));
        testService.isolationTestTransaction1(isolationLevel);
    }

    public void isolationTestTransaction2(Context context) {
        testService.isolationTestTransaction2();
    }


    public void getAll(Context context) {
        List<Entity> entities = crudService.getAll();
        context.json(entities);
    }

    public void get(Context context) {
        Long id = Long.valueOf(context.pathParam("id"));
        Entity entity = crudService.getById(id);
        context.json(entity);
    }

    public void save(Context context) {
        Entity entity = context.bodyAsClass(Entity.class);
        crudService.save(entity);
    }

    public void bulkSave(Context context) {
        List<Entity> entities = Arrays.asList(context.bodyAsClass(Entity[].class));
        Boolean eachInSingleTransaction = Boolean.parseBoolean(context.pathParam("eachInSingleTransaction"));
        crudService.bulkSave(entities, eachInSingleTransaction);
    }

    public void update(Context context) {
        Entity entity = context.bodyAsClass(Entity.class);
        crudService.update(entity);
    }

    public void bulkUpdate(Context context) {
        List<Entity> entities = Arrays.asList(context.bodyAsClass(Entity[].class));
        Boolean eachInSingleTransaction = Boolean.parseBoolean(context.pathParam("eachInSingleTransaction"));
        crudService.bulkUpdate(entities, eachInSingleTransaction);
    }

    public void deleteAll(Context context) {
        crudService.deleteAll();
    }

    public void delete(Context context) {
        Long id = Long.valueOf(context.pathParam("id"));
        crudService.deleteById(id);
    }
}
