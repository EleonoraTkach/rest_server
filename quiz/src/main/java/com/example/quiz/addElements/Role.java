package com.example.quiz.addElements;

public enum Role {

        ADMIN(1, "Администратор"),
        TEACHER(2, "Преподаватель"),
        STUDENT(3, "Студент");

        private final int value;
        private final String description;

        Role(int value, String description) {
            this.value = value;
            this.description = description;
        }

    public static String getDescriptionByValue(Integer value) {
        for (Role role : Role.values()) {
            if (role.getValue() == value) {
                return role.getDescription();
            }
        }
        return "Роль не найдена";
    }

    public int getValue() {
            return value;
    }
    public String getDescription() {

            return description;
    }


}
