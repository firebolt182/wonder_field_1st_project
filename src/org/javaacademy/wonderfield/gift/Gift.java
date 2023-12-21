package org.javaacademy.wonderfield.gift;

public enum Gift {
    NOTHING("", 0),
    COOKING_LEAVES("Маринованные лопухи", 500),
    KITCHEN_COMBINE("Кухонный комбайн", 1000),

    METAL_CURTAIN("Железный занавес", 1500),

    SOX("Шерстяные носки\"Год Дракона\"", 2000),

    NOODLES("Лапша БП", 2500),

    CARD("Карта Москвича", 3000);

    private String name;
    private int price;

    Gift(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
