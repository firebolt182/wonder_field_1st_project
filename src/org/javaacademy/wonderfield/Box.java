package org.javaacademy.wonderfield;

import org.javaacademy.wonderfield.player.Player;

public class Box {
    private boolean haveMoney;

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
