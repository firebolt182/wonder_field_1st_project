package org.javaacademy.wonderfield.player;

import org.javaacademy.wonderfield.Game;

//3.4 Создание класса 'Ответ игрока'
public class PlayerAnswer {
    private Player player;
    private AnswerType answerType;
    private String answer;

    public PlayerAnswer(Player player) {
        this.player = player;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
