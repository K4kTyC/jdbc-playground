package org.example.repository;

import org.example.model.SimpleEntity;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.example.util.PoolStatistics.*;

public class TestRepository {

    private final MariaDbPoolDataSource dataSource;

    public TestRepository(MariaDbPoolDataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void poolTest() {
        String threadName = Thread.currentThread().getName();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

        System.out.print(LocalTime.now().format(formatter) + " [" + threadName + "] Before getConnection() - ");
        printStats();

        try (Connection con = dataSource.getConnection()) {
            System.out.print(LocalTime.now().format(formatter) + " [" + threadName + "] After getConnection() - ");
            printStats();

            Thread.sleep(10000);
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.print(LocalTime.now().format(formatter) + " [" + threadName + "] Completed - ");
        printStats();
    }

    public void isolationTestClearTable() {
        try (Connection con = dataSource.getConnection()) {
            Statement st = con.createStatement();
            st.executeUpdate("DELETE FROM simple_entity");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void isolationTestCreateEntities(List<SimpleEntity> entities) {
        int id = 1;
        for (SimpleEntity entity : entities) {
            try (Connection con = dataSource.getConnection()) {
                PreparedStatement ps = con.prepareStatement("""
                        INSERT INTO simple_entity(id, column_string)
                        VALUES(?, ?)
            """);
                ps.setInt(1, id++);
                ps.setString(2, entity.getColumnString());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void isolationTestTransaction1(Integer isolationLevel) {
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            con.setTransactionIsolation(isolationLevel);
            System.out.print("Isolation level: ");
            switch (isolationLevel) {
                case Connection.TRANSACTION_READ_COMMITTED -> System.out.println("Read Committed");
                case Connection.TRANSACTION_REPEATABLE_READ -> System.out.println("Repeatable Read");
                case Connection.TRANSACTION_SERIALIZABLE -> System.out.println("Serializable");
            }

            List<SimpleEntity> entities = new ArrayList<>();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM simple_entity");
            while (rs.next()) {
                entities.add(mapSimpleEntityFromResultSet(rs));
            }
            System.out.println("T1 before update: ");
            entities.forEach(System.out::println);
            Thread.sleep(10000);

            entities.clear();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM simple_entity");
            while (rs.next()) {
                entities.add(mapSimpleEntityFromResultSet(rs));
            }
            System.out.println("T1 after update: ");
            entities.forEach(System.out::println);

        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void isolationTestTransaction2(List<SimpleEntity> updatedEntities, SimpleEntity newEntity) {
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);
            for (SimpleEntity entity : updatedEntities) {
                PreparedStatement ps = con.prepareStatement("""
                       UPDATE simple_entity
                       SET column_string = ?
                       WHERE id = ?
                """);
                ps.setString(1, entity.getColumnString());
                ps.setLong(2, entity.getId());
                ps.executeUpdate();
            }
            PreparedStatement ps = con.prepareStatement("""
                        INSERT INTO simple_entity(id, column_string)
                        VALUES(?, ?)
            """);
            ps.setInt(1, 3);
            ps.setString(2, newEntity.getColumnString());
            ps.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private SimpleEntity mapSimpleEntityFromResultSet(ResultSet rs) throws SQLException {
        SimpleEntity entity = new SimpleEntity();
        entity.setId(rs.getInt("id"));
        entity.setColumnString(rs.getString("column_string"));
        return entity;
    }
}
