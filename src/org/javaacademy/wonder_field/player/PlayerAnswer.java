package org.javaacademy.wonder_field.player;


import java.util.Scanner;

//3.4 Создание класса 'Ответ игрока'
public class PlayerAnswer {
    Player player;
    private AnswerType answerType;
    private String answer;
    private Scanner scanner;
    public PlayerAnswer(Player player){
        this.player = player;
    }
//3.5 ход игрока
    public String move(){
        System.out.printf("Ход игрока %s, %s\n", player.getName(), player.getCity());
        System.out.println("Если хотите букву нажмите 'б' и enter, если хотите слово нажмите 'c' и enter");
        scanner = new Scanner(System.in);
        String choice;
        //проверка корректности ввода из консоли
        while (true){
             choice = scanner.nextLine();
            if(choice.equals("б") || choice.equals("с")){
                break;
            }
            System.out.println("Сделайте корректный выбор");
        }
        switch (choice){
            case "с" :
                answerType = AnswerType.WORD;
                answer = player.sayWord();
                break;
            case "б" :
                answerType = AnswerType.LETTER;
                answer = player.sayLetter();
                break;
        }
        return answer;

    }
}
