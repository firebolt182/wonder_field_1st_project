package org.javaacademy.wonder_field;

import org.javaacademy.wonder_field.player.Player;

public class Box {
    Player player;
    private boolean haveMoney;

    public boolean isHaveMoney() {
        return haveMoney;
    }

    public Box(boolean haveMoney) {
        this.haveMoney = haveMoney;
    }

    public void setHaveMoney(boolean haveMoney) {
        this.haveMoney = haveMoney;
    }

    public void openBox(Player player) {
        if (this.haveMoney) {
            System.out.println("Поздравляю, в этой шкатулке находятся деньги!");
            player.setHaveMoneyFromBox(true);
        } else {
            System.out.println("Увы, эта шкатулка пуста!");
        }
    }

}
