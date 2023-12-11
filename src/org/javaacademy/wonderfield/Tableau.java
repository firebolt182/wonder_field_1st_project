package org.javaacademy.wonderfield;

import java.util.Arrays;
//2.1 Создание класса Табло с полями

public class Tableau {
    private String trueAnswer;
    private String[] letters;

    public String getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(String trueAnswer) {
        this.trueAnswer = trueAnswer.toUpperCase();
    }

    public String[] getLetters() {
        return letters;
    }

    public void setLetters(String[] letters) {
        this.letters = letters;
    }

    //2.2 Инициализация табло
    public void init() {
        letters = new String[trueAnswer.length()];
        Arrays.fill(letters, "_");
    }

    //2.3 отображает все буквы
    public void show() {
        if (attributesNotEmpty()) {
            for (String letters : letters) {
                System.out.print(" " + String.join(" ", letters) + " ");
            }
            System.out.println();
        }
    }

    // 2.4 открывает букву
    public void openLetter(String word) {
        if (attributesNotEmpty()) {
            if (word.length() == 1) {
                for (int i = 0; i < trueAnswer.length(); i++) {
                    if (trueAnswer.split("")[i].equals(word.toUpperCase())) {
                        letters[i] = word.toUpperCase();
                    }
                }
            }
        }
        // Отображение будет с найденной буквой
        this.show();
    }

    //2.5 открывает слово целиком
    public void openWord(String word) {
        System.out.println(String.join(" ", word.toUpperCase()));
    }

    //2.6 содержит ли неизвестные буквы
    public boolean containsUnknownWords() {
        return !Arrays.toString(this.getLetters()).contains("_");
    }

    //2.7 проверяет, что атрибуты не пустые
    public boolean attributesNotEmpty() {
        try {
            return !trueAnswer.isEmpty() || letters == null;
        } catch (Exception e) {
            return false;
        }
    }
}
