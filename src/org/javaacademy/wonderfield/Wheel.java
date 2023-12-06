package org.javaacademy.wonderfield;

public class Wheel {
    //1. Создать барабан с перечнем значений
    // от 100 до 1200 с шагом в 100. + сектор умножения на 2,
    // + сектор пропуска хода.
    private static int[] sector;

    public static int[] getSector() {
        return sector;
    }

    public void init() {
        sector = new int[14];
        sector[0] = 100;
        sector[1] = 200;
        sector[2] = 300;
        sector[3] = 400;
        sector[4] = 500;
        sector[5] = 600;
        sector[6] = 700;
        sector[7] = 800;
        sector[8] = 900;
        sector[9] = 1000;
        sector[10] = 1100;
        sector[11] = 1200;
        //x2
        sector[12] = 2;
        //пропуск хода
        sector[13] = 0;
    }
}
