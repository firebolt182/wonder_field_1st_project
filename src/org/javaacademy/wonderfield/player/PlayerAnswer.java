package org.javaacademy.wonderfield.player;

import org.javaacademy.wonderfield.Game;

//3.4 Создание класса 'Ответ игрока'
public class PlayerAnswer {
    Player player;
    private AnswerType answerType;
    private String answer;

    public PlayerAnswer(Player player) {
        this.player = player;
    }


    //3.5 ход игрока
    public String move() {
        System.out.printf("Ход игрока %s, %s\n", player.getName(), player.getCity());
        System.out.println("Если хотите букву нажмите 'б' и enter,"
                + " если хотите слово нажмите 'c' и enter");
        String choice;
        //проверка корректности ввода из консоли
        while (true) {
            choice = Game.SCANNER.nextLine();
            if ((choice.equalsIgnoreCase("б") || choice.equalsIgnoreCase("с"))
                && !choice.isEmpty()) {
                break;
            }
        }
        switch (choice) {
            case "с":
                answerType = AnswerType.WORD;
                answer = player.sayWord();
                break;
            case "б":
                answerType = AnswerType.LETTER;
                answer = player.sayLetter();
                break;
            default:
                break;
        }
        return answer;
    }
}
