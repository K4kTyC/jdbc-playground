package org.example.model;

import org.example.enums.TestEnum;

import java.time.Instant;
import java.time.LocalDateTime;

public class TestEntity {

    private Long id;
    private Boolean columnBool;
    private String columnString;
    private TestEnum columnEnum;
    private Double columnDouble;
    private Instant columnTimestamp;
    private LocalDateTime columnDatetime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getColumnBool() {
        return columnBool;
    }

    public void setColumnBool(Boolean columnBool) {
        this.columnBool = columnBool;
    }

    public String getColumnString() {
        return columnString;
    }

    public void setColumnString(String columnString) {
        this.columnString = columnString;
    }

    public TestEnum getColumnEnum() {
        return columnEnum;
    }

    public void setColumnEnum(TestEnum columnEnum) {
        this.columnEnum = columnEnum;
    }

    public Double getColumnDouble() {
        return columnDouble;
    }

    public void setColumnDouble(Double columnDouble) {
        this.columnDouble = columnDouble;
    }

    public Instant getColumnTimestamp() {
        return columnTimestamp;
    }

    public void setColumnTimestamp(Instant columnTimestamp) {
        this.columnTimestamp = columnTimestamp;
    }

    public LocalDateTime getColumnDatetime() {
        return columnDatetime;
    }

    public void setColumnDatetime(LocalDateTime columnDatetime) {
        this.columnDatetime = columnDatetime;
    }
}
