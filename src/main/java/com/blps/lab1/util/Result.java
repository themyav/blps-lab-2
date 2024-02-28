package com.blps.lab1.util;

public class Result {
        private int code;

        public Result(int code, String message) {
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