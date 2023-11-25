package org.javaacademy.wonder_field.player;

import java.util.Scanner;

//3.1 Создание класса Игрок
public class Player {
    private String name;
    private String city;
    private Scanner scanner;

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

//3.2 Кричит букву
    public String sayLetter(){
        scanner = new Scanner(System.in);
        // Проверяем букву,чтобы была одна и кириллицей
            String letter = null;
            while (true){
                letter = scanner.nextLine();
                if (letter.length()==1 && (Character.UnicodeBlock.of(letter.charAt(0))
                        .equals(Character.UnicodeBlock.CYRILLIC))) {
                    System.out.printf("Игрок %s : %s\n",this.getName(),letter);
                    break;
                } else{
                    System.out.println("Ошибка! это не русская буква, введите русскую букву");
                }
            }
        return letter;
    }
//3.3 Говорит слово
    public String sayWord(){
        scanner = new Scanner(System.in);
        String word = scanner.nextLine();
        System.out.printf("Игрок %s : %s\n",this.getName(),word);
        return word;
    }

}
