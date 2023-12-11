package org.javaacademy.wonderfield.gift;

public enum Name {
    COOKING_LEAVES("Маринованные лопухи"),
    KITCHEN_COMBINE("Кухонный комбайн"),

    METAL_CURTAIN("Железный занавес"),

    SOX("Шерстяные носки\"Год Дракона\""),

    NOODLES("Лапша БП"),

    CARD("Карта Москвича");

    private String name;

    Name(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
