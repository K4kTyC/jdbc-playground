package org.example.service;

import org.example.model.SimpleEntity;
import org.example.repository.TestRepository;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.util.List;

public class TestService {

    private final TestRepository testRepository;

    public TestService(MariaDbPoolDataSource dataSource) {
        testRepository = new TestRepository(dataSource);
    }


    public void poolTest() {
        testRepository.poolTest();
    }

    public void isolationTestInit() {
        SimpleEntity e1 = new SimpleEntity();
        e1.setColumnString("e1text");
        SimpleEntity e2 = new SimpleEntity();
        e2.setColumnString("e2text");

        testRepository.isolationTestClearTable();
        testRepository.isolationTestCreateEntities(List.of(e1, e2));
    }

    public void isolationTestTransaction1(Integer isolationLevel) {
        testRepository.isolationTestTransaction1(isolationLevel);
    }

    public void isolationTestTransaction2() {
        SimpleEntity e1 = new SimpleEntity();
        e1.setId(1);
        e1.setColumnString("e1text_new");
        SimpleEntity e2 = new SimpleEntity();
        e2.setId(2);
        e2.setColumnString("e2text_new");
        SimpleEntity e3 = new SimpleEntity();
        e3.setColumnString("e3text");

        testRepository.isolationTestTransaction2(List.of(e1, e2), e3);
    }
}
