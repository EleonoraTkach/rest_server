package com.example.quiz.addElements;

public enum TypeQuestion {
    ONEASWER(1, "Выбор одного правильного ответа"),
    MANYANSWER(2, "Выбор нескольких правильных ответов"),
    TEXTANSWER(3, "Вписать ответ");

    private final int value;
    private final String description;

    TypeQuestion(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static String getDescriptionByValue(Integer value) {
        for (TypeQuestion typeQuestion : TypeQuestion.values()) {
            if (typeQuestion.getValue() == value) {
                return typeQuestion.getDescription();
            }
        }
        return "Тип вопроса не найден";
    }

    public static Integer getValueByDescription(String description) {
        for (TypeQuestion typeQuestion : TypeQuestion.values()) {
            if (typeQuestion.getDescription().equals(description)) {
                return typeQuestion.getValue();
            }
        }
        return 0;
    }
    public int getValue() {
        return value;
    }
    public String getDescription() {
        return description;
    }
}
