package com.teams.entity.models;

import lombok.Data;

import java.util.UUID;

@Data
public class DinningTableModel {
    private Long tableId;
    private UUID subUserId;
    private String tableName;
    private String status;

    public enum TableEnum{
        AVAILABLE,OCCUPIED
    }
}
