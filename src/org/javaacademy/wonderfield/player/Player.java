package org.javaacademy.wonderfield.player;

import org.javaacademy.wonderfield.Game;
import org.javaacademy.wonderfield.Wheel;


//3.1 Создание класса Игрок
public class Player {
    private String name;
    private String city;

    private PlayerAnswer playerAnswer = new PlayerAnswer(this);
    private int points = 0;
    private int threeInRow = 0;
    private int moneyFromBox = 0;

    public Player(String name, String city) {
        this.name = name;
        this.city = city;
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

    public int getThreeInRow() {
        return threeInRow;
    }

    public void setThreeInRow(int threeInRow) {
        this.threeInRow = threeInRow;
    }

    public int getMoneyFromBox() {
        return moneyFromBox;
    }

    public void setMoneyFromBox(int moneyFromBox) {
        this.moneyFromBox = moneyFromBox;
    }

    //3.2 Кричит букву
    public String sayLetter() {
        // Проверяем букву,чтобы была одна и кириллицей
        String letter = "";
        while (true) {
            letter = Game.SCANNER.nextLine();
            char letterCharacter = 0;
            if (!letter.isEmpty()) {
                letterCharacter = letter.charAt(0);
            }
            if (letterCheck(letter, letterCharacter)) {
                System.out.printf("Игрок %s : %s\n", this.getName(), letter);
                break;
            } else {
                System.out.println("Ошибка! это не русская буква, введите русскую букву");
            }
        }
        return letter;
    }

    //отдельный метод проверки
    public boolean letterCheck(String letter, Character letterCharacter) {
        return (letter.length() == 1
                && ((letterCharacter >= 'А' && letterCharacter <= 'Я')
                || (letterCharacter >= 'а' && letterCharacter <= 'я')));
    }

    //3.3 Говорит слово
    public String sayWord() {
        String word = Game.SCANNER.nextLine();
        System.out.printf("Игрок %s : %s\n", this.getName(), word);
        return word;
    }

    //3.5 ход игрока
    public String move() {
        System.out.printf("Ход игрока %s, %s\n", this.getName(), this.getCity());
        System.out.println("Если хотите букву нажмите 'б' и enter,"
                + " если хотите слово нажмите 'c' и enter");
        String choice;
        //проверка корректности ввода из консоли
        while (true) {
            choice = Game.SCANNER.nextLine();
            if ((choice.equalsIgnoreCase("б") || choice.equalsIgnoreCase("с"))
                    && !choice.isEmpty()) {
                break;
            } else {
                System.out.println("Некорректное значение, введите 'б' или 'с'");
            }
        }
        makeDecision(choice);
        return playerAnswer.getAnswer();
    }

    //выбор - буква/слово
    public void makeDecision(String choice) {
        switch (choice) {
            case "с":
                playerAnswer.setAnswerType(AnswerType.WORD);
                playerAnswer.setAnswer(this.sayWord());
                break;
            case "б":
                playerAnswer.setAnswerType(AnswerType.LETTER);
                playerAnswer.setAnswer(this.sayLetter());
                break;
            default:
                break;
        }
    }

    public int runTheWheel() {
        int max = Wheel.values().length;
        return (int) (Math.random() * max);
    }
}