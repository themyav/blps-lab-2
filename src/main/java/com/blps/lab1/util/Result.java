package com.blps.lab1.util;

public enum Result {



    OK(0, "упешное выполнение операции"),
    USER_NOT_FOUND(-1, "пользователь не найден"),
    NOT_ENOUGH_BALANCE(1, "не достаточно средств на балансе"),
    NO_VACANCY_DESCRIPTION(2, "не указано описание вакансии"),
    NO_VACANCY_TITLE(3, "не указано название вакансии"),
    NO_VACANCY_AUTHOR(4, "не указан автор вакансии"),

    UNKNOWN_ERROR(5, "ошибка во время выполнения операции");
        private int code;


         Result(int code, String message) {
            this.code = code;
            this.message = message;
        }

        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

    }