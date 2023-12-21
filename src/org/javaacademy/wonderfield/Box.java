package org.javaacademy.wonderfield;

public class Box {
    private boolean haveMoney;

    public Box(boolean haveMoney) {
        this.haveMoney = haveMoney;
    }

    public void setHaveMoney(boolean haveMoney) {
        this.haveMoney = haveMoney;
    }

    public int openBox() {
        if (this.haveMoney) {
            System.out.println("Поздравляю, в этой шкатулке находятся деньги!");
            return 10000;
        } else {
            System.out.println("Увы, эта шкатулка пуста!");
            return 0;
        }
    }
}
