package org.example.repository;

import org.example.enums.EnumForEntity;
import org.example.model.Entity;
import org.mariadb.jdbc.MariaDbPoolDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CrudRepository {

    private final MariaDbPoolDataSource dataSource;

    public CrudRepository(MariaDbPoolDataSource dataSource) {
        this.dataSource = dataSource;
    }


    public List<Entity> getAll() {
        List<Entity> entities = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM entity");
            while (rs.next()) {
                entities.add(mapEntityFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return entities;
    }

    public Entity getById(Long id) {
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM entity WHERE id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapEntityFromResultSet(rs);
            } else {
                throw new RuntimeException("Can't find entity with id: " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(Entity entity) {
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement("""
                        INSERT INTO entity(column_bool, column_string, column_enum, column_double, column_timestamp, column_datetime)
                        VALUES(?, ?, ?, ?, ?, ?)
            """);
            ps.setBoolean(1, entity.getColumnBool());
            ps.setString(2, entity.getColumnString());
            ps.setString(3, entity.getColumnEnum().toString());
            ps.setDouble(4, entity.getColumnDouble());
            ps.setTimestamp(5, Timestamp.from(entity.getColumnTimestamp()));
            ps.setObject(6, entity.getColumnDatetime());
            ps.executeUpdate();
            /*if (Math.random() > 0.5) {
                throw new SQLException("random error");
            } else {
                System.out.println("saved");
            }*/
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void bulkSaveEachInSingleTransaction(List<Entity> entities) {
        for (Entity entity : entities) {
            try {
                save(entity);
            } catch (RuntimeException ignored) { }
            /*catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }*/
        }
    }

    public void bulkSave(List<Entity> entities) {
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);

            for (Entity entity : entities) {
                try {
                    save(entity);
                } catch (RuntimeException e) {
                    con.rollback();
                    throw e;
                }
            }

            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Entity entity) {
        if (!existsById(entity.getId())) {
            throw new RuntimeException("Can't find entity with id: " + entity.getId());
        }

        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement("""
                    UPDATE entity
                    SET column_bool = ?, column_string = ?, column_enum = ?, column_double = ?, column_timestamp = ?, column_datetime = ?
                    WHERE id = ?
            """);
            ps.setBoolean(1, entity.getColumnBool());
            ps.setString(2, entity.getColumnString());
            ps.setString(3, entity.getColumnEnum().toString());
            ps.setDouble(4, entity.getColumnDouble());
            ps.setTimestamp(5, Timestamp.from(entity.getColumnTimestamp()));
            ps.setObject(6, entity.getColumnDatetime());
            ps.setLong(7, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void bulkUpdateEachInSingleTransaction(List<Entity> entities) {
        for (Entity entity : entities) {
            try {
                update(entity);
            } catch (RuntimeException ignored) { }
            /*catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }*/
        }
    }

    public void bulkUpdate(List<Entity> entities) {
        try (Connection con = dataSource.getConnection()) {
            con.setAutoCommit(false);

            for (Entity entity : entities) {
                try {
                    update(entity);
                } catch (RuntimeException e) {
                    con.rollback();
                    throw e;
                }
            }

            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        try (Connection con = dataSource.getConnection()) {
            Statement st = con.createStatement();
            st.executeUpdate("DELETE FROM entity");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(Long id) {
        if (!existsById(id)) {
            throw new RuntimeException("Can't find entity with id: " + id);
        }

        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM entity WHERE id = ?");
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsById(Long id) {
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT id FROM entity WHERE id = ?");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Entity mapEntityFromResultSet(ResultSet rs) throws SQLException {
        Entity entity = new Entity();
        entity.setId(rs.getLong("id"));
        entity.setColumnBool(rs.getBoolean("column_bool"));
        entity.setColumnString(rs.getString("column_string"));
        entity.setColumnEnum(EnumForEntity.valueOf(rs.getString("column_enum")));
        entity.setColumnDouble(rs.getDouble("column_double"));
        entity.setColumnTimestamp(rs.getObject("column_timestamp", Instant.class));
        entity.setColumnDatetime(rs.getObject("column_datetime", LocalDateTime.class));
        return entity;
    }
}
