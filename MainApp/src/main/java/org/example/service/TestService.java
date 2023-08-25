package org.example.service;

import org.example.model.TestEntity;
import org.example.repository.TestRepository;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.util.List;

public class TestService {

    private final TestRepository repository;

    public TestService(MariaDbPoolDataSource dataSource) {
        this.repository = new TestRepository(dataSource);
    }


    public void poolTest() {
        repository.poolTest();
    }

    public List<TestEntity> getAll() {
        return repository.getAll();
    }

    public TestEntity getById(Long id) {
        return repository.getById(id);
    }

    public void save(TestEntity entity) {
        if (entity != null) {
            repository.save(entity);
        }
    }

    public void bulkSave(List<TestEntity> entities, Boolean eachInSingleTransaction) {
        if (eachInSingleTransaction) {
            repository.bulkSaveEachInSingleTransaction(entities);
        } else {
            repository.bulkSave(entities);
        }
    }

    public void update(TestEntity entity) {
        if (entity != null && entity.getId() != null) {
            repository.update(entity);
        }
    }

    public void bulkUpdate(List<TestEntity> entities, Boolean eachInSingleTransaction) {
        if (eachInSingleTransaction) {
            repository.bulkUpdateEachInSingleTransaction(entities);
        } else {
            repository.bulkUpdate(entities);
        }
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
