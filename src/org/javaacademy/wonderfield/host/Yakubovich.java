package org.javaacademy.wonderfield.host;

import org.javaacademy.wonderfield.Game;
import org.javaacademy.wonderfield.Tableau;
import org.javaacademy.wonderfield.player.Player;

//4.1 Создание класса Якубович
public class Yakubovich {

    private final String name = "Якубович";


    //4.2 Первая фраза
    public void firstPhrase() {
        System.out.println(name + ": Здравствуйте, уважаемые дамы и господа! Пятница!"
                + " В эфире капитал-шоу «Поле чудес»!");
    }

    //4.3 Прощание
    public void goodbyePhrase() {
        System.out.println(name + ": Мы прощаемся с вами ровно на одну неделю! "
                + "Здоровья вам, до встречи!");
    }

    //4.4 Приглашение
    public void invite(Player[] players, int roundNumber) {
        if (roundNumber != 4) {
            Game.isFinalRound = false;
            System.out.println(name + ": приглашаю " + roundNumber + " тройку игроков: "
                    + concat(players));
        } else {
            Game.isFinalRound = true;
            System.out.println(name + ": приглашаю победителей групповых этапов: "
                    + concat(players));
        }
    }

    //3.5 задает вопрос
    public void askQuestion(int roundNumber, String[] questions) {
        System.out.println(name + ": Внимание вопрос!\n " + questions[roundNumber]);
    }

    //3.6 Кричит в случае победы игрока
    public void askForWinner(String playerName, String city, boolean isFinalRound) {
        if (!isFinalRound) {
            System.out.println(name + ": Молодец! " + playerName + " из " + city
                    + " проходит в финал!");
        } else {
            System.out.println(name + " : И перед нами победитель Капитал шоу поле чудес! Это "
                    + playerName + " из " + city);
        }
    }

    //3.7 проверяет ответ игрока
    public boolean checkAnswer(String playerAnswer, String trueAnswer, Tableau tableau) {
        if (playerAnswer.length() > 1) {
            if ((trueAnswer).equalsIgnoreCase(playerAnswer)) {
                System.out.println(name + ": Абсолютно верно!");
                //открываем на табло правильное слово
                tableau.openWord(trueAnswer);
                tableau.setLettersOnTableau(trueAnswer.split(""));
                return true;
            } else {
                System.out.println(name + ": Неверно! Следующий игрок!");
                System.out.println("__________________________________");
                return false;
            }
        } else {
            if ((trueAnswer).toUpperCase().contains(playerAnswer.toUpperCase())) {
                for (String t : tableau.getLettersOnTableau()) {
                    if (playerAnswer.equalsIgnoreCase(t)) {
                        System.out.println("Эта буква уже есть на табло");
                        return true;
                    }
                }
                //табло открывает букву
                System.out.println(name + ": Есть такая буква, откройте ее!");
                tableau.openLetter(playerAnswer);
                return true;
            } else {
                System.out.println(name + " : Нет такой буквы! Следующий игрок, крутите барабан!");
                System.out.println("__________________________________");
                return false;
            }
        }
    }

    public void sayWord() {
        System.out.println(name + ": Назовите слово!");
    }

    //Говорит,что игрок победил в суперигре
    public void superGameWinner() {
        System.out.println(name + ": Перед нами победитель суперигры!");
    }

    // Говорит,что игрок не угадал слово в суперигре
    public void superGameLoser(Player player) {
        System.out.println(name + ": К сожалению " + player.getName() + " не угадал ответ.");
    }

    public void saySuperThing(String randomGift) {
        System.out.println(name + ": Секретной вещью является - " + randomGift);
    }

    public void refuseSuperGame(Player player) {
        System.out.println(name + ": " + player.getName() + " отказался от супер игры.");
    }

    public void boxesToRoom() {
        System.out.println(name + ": Две шкатулки в студию!!!");
    }


    //метод соединения строк в одну строку с запятыми
    public String concat(Player[] players) {
        StringBuilder builder = new StringBuilder();
        for (Player player : players) {
            builder.append(player.getName() + ", ");
        }
        return builder.substring(0, builder.length() - 1);
    }
}