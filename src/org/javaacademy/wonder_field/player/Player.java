package org.javaacademy.wonder_field.player;

import org.javaacademy.wonder_field.Game;


//3.1 Создание класса Игрок
public class Player {
    private String name;
    private String city;

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
        // Проверяем букву,чтобы была одна и кириллицей
            String letter = null;
            while (true){
                letter = Game.getScanner().nextLine();
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
        String word = Game.getScanner().nextLine();
        System.out.printf("Игрок %s : %s\n",this.getName(),word);
        return word;
    }
}