package org.example.service;

import org.example.model.Entity;
import org.example.repository.CrudRepository;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.util.List;

public class CrudService {

    private final CrudRepository crudRepository;

    public CrudService(MariaDbPoolDataSource dataSource) {
        crudRepository = new CrudRepository(dataSource);
    }


    public List<Entity> getAll() {
        return crudRepository.getAll();
    }

    public Entity getById(Long id) {
        return crudRepository.getById(id);
    }

    public void save(Entity entity) {
        if (entity != null) {
            crudRepository.save(entity);
        }
    }

    public void bulkSave(List<Entity> entities, Boolean eachInSingleTransaction) {
        if (eachInSingleTransaction) {
            crudRepository.bulkSaveEachInSingleTransaction(entities);
        } else {
            crudRepository.bulkSave(entities);
        }
    }

    public void update(Entity entity) {
        if (entity != null && entity.getId() != null) {
            crudRepository.update(entity);
        }
    }

    public void bulkUpdate(List<Entity> entities, Boolean eachInSingleTransaction) {
        if (eachInSingleTransaction) {
            crudRepository.bulkUpdateEachInSingleTransaction(entities);
        } else {
            crudRepository.bulkUpdate(entities);
        }
    }

    public void deleteAll() {
        crudRepository.deleteAll();
    }

    public void deleteById(Long id) {
        crudRepository.deleteById(id);
    }
}
