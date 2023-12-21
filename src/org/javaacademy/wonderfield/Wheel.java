package org.javaacademy.wonderfield;

public enum Wheel {
    MOVE_SKIP(0),
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    THREE_HUNDRED(300),
    FOUR_HUNDRED(400),
    FIVE_HUNDRED(500),
    SIX_HUNDRED(600),
    SEVEN_HUNDRED(700),
    EIGHT_HUNDRED(800),
    NINE_HUNDRED(900),
    ONE_THOUSAND(1000),
    ONE_THOUSAND_ONE_HUNDRED(1100),
    ONE_THOUSAND_TWO_HUNDRED(1200),
    MULTIPLICATION(0);

    private int points;
    Wheel(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

}
