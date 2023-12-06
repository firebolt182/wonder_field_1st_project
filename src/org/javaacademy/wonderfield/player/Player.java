package org.javaacademy.wonderfield.player;

import org.javaacademy.wonderfield.Game;
import org.javaacademy.wonderfield.Wheel;


//3.1 Создание класса Игрок
public class Player {
    private String name;
    private String city;
    private int points;
    private int threeInaRow;

    private boolean haveMoneyFromBox;

    public Player(String name, String city) {
        this.name = name;
        this.city = city;
        this.points = 0;
        threeInaRow = 0;
        this.setHaveMoneyFromBox(false);
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getThreeInaRow() {
        return threeInaRow;
    }

    public void setThreeInaRow(int threeInaRow) {
        this.threeInaRow = threeInaRow;
    }

    public boolean isHaveMoneyFromBox() {
        return haveMoneyFromBox;
    }

    public void setHaveMoneyFromBox(boolean haveMoneyFromBox) {
        this.haveMoneyFromBox = haveMoneyFromBox;
    }

    //3.2 Кричит букву
    public String sayLetter() {
        // Проверяем букву,чтобы была одна и кириллицей
        String letter = null;
        while (true) {
            letter = Game.getScanner().nextLine();
            char letterCheck = letter.charAt(0);
            if (letter.length() == 1
                    && ((letterCheck >= 'А' && letterCheck <= 'Я')
                    || (letterCheck >= 'а' && letterCheck <= 'я'))) {
                System.out.printf("Игрок %s : %s\n", this.getName(), letter);
                break;
            } else {
                System.out.println("Ошибка! это не русская буква, введите русскую букву");
            }
        }
        return letter;
    }

    //3.3 Говорит слово
    public String sayWord() {
        String word = Game.getScanner().nextLine();
        System.out.printf("Игрок %s : %s\n", this.getName(), word);
        return word;
    }

    public int runTheWheel() {
        int max = Wheel.getSector().length - 1;
        return (int) (Math.random() * max);
    }
}