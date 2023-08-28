package org.example.model;

public class SimpleEntity {

    private Integer id;
    private String columnString;


    @Override
    public String toString() {
        return "SimpleEntity{" +
                "id=" + id +
                ", columnString='" + columnString + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getColumnString() {
        return columnString;
    }

    public void setColumnString(String columnString) {
        this.columnString = columnString;
    }
}
