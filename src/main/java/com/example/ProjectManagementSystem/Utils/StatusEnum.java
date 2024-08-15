package com.example.ProjectManagementSystem.Utils;


public enum StatusEnum {

    PRE(0),
    START(1),
    END(3);

    private final Integer statusValue;

    StatusEnum(Integer statusValue) {
        this.statusValue = statusValue;
    }

    public Integer getStatusValue() {
        return statusValue;
    }

    public static StatusEnum fromCode(int code) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getStatusValue() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
