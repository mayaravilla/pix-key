package com.example.pix.enums;

public enum PixKeyType {

    EMAIL {
        @Override
        public boolean isValid(String valor) {
            return valor != null && valor.matches("^[^@]+@[^@]+\\.[^@]+$");
        }
    },
    CPF {
        @Override
        public boolean isValid(String valor) {
            return valor != null && valor.matches("\\d{11}");
        }
    },
    CNPJ {
        @Override
        public boolean isValid(String valor) {
            return valor != null && valor.matches("\\d{14}");
        }
    };

    public abstract boolean isValid(String valor);
}
