package me.ardryck.paladins.homes.enums;

public enum Type {

    PRIVATE("Privada"),
    PUBLIC("Pública");

    private final String type;

    private Type(String type) {
        this.type = type;
    }

    public String getName() {
        return this.type;
    }
}
