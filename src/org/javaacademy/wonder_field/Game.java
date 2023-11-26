package org.javaacademy.wonder_field;

import org.javaacademy.wonder_field.host.Yakubovich;
import org.javaacademy.wonder_field.player.Player;
import org.javaacademy.wonder_field.player.PlayerAnswer;

import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private static final int PLAYERS = 3;
    private static final int ROUNDS = 4;
    private static final int GROUP_ROUNDS = 3;
    private static final int FINAL_ROUND_INDEX = 3;
    private String[] questions = new String[ROUNDS];
    private String[] answers = new String[ROUNDS];
//5.1 поле winners
    private ArrayList<Player> winners = new ArrayList<>();
    private Tableau tableau;
    private Yakubovich yakubovich;

    private static Scanner scanner;

    public static Scanner getScanner() {
        return scanner;
    }

    //1.5 создание метода init()
    public void init() {
        System.out.println("Запуск игры \"Поле Чудес\" - подготовка к игре." +
                " Вам нужно ввести вопросы и ответы для игры.\n");
        scanner = new Scanner(System.in);

        for (int i = 1; i < 5; i++) {
            System.out.printf("\"Введите вопрос #%d\"\n", i);
            String question = scanner.nextLine();
            questions[i-1] = question;
            System.out.printf("\"Введите ответ вопрос #%d\"\n", i);
            String answer = scanner.nextLine();
            answers[i-1] = answer;
        }
        //инициализирую табло
        tableau = new Tableau();
        System.out.println("Иницализация закончена, игра начнется через 5 секунд");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        for (int i = 0; i < 51; i++) {
            System.out.println("");
        }
        yakubovich = new Yakubovich();
    }

//5.2 метод создания игроков
    public Player[] createPlayers(){
        Player[] players = new Player[3];
        for (int i = 0; i < PLAYERS; i++) {
            System.out.println("Игрок №" + (i+1) + " представьтесь: имя,город. Например: Иван,Москва");
            while (true) {
                String data = scanner.nextLine();
                if (data.contains(",")) {
                    String[] dataArray = data.split(",");
                    players[i] = new Player(dataArray[0], dataArray[1].trim());
                    break;
                } else {
                    System.out.println("Введите имя и город через запятую");
                }
            }
        }

        return players;
    }

//5.3 вытаскиваем имена игроков в отдельный массив
    public String[] namesOfPlayers(Player[] players){
        String[] names = new String[3];
        for (int i = 0; i < players.length; i++) {
            names[i] = players[i].getName();
        }
        return names;
    }

//5.4 проверка табло
    public boolean checkTableau(Tableau tableau){
        return tableau.containsUnknownWords();
    }

    //5.5 Метод хода игрока
    public boolean move(String question, Player player){

        PlayerAnswer playerAnswer = new PlayerAnswer(player);
        String answer = "";
        boolean exit = false;

        while (!exit) {
            //блок на проверку не пустого ответа игрока
            try{
                answer = playerAnswer.move();
            } catch (StringIndexOutOfBoundsException e){
                while (true){
                    if (answer.length()==0){
                        answer = playerAnswer.move();
                    } else {
                        return false;
                    }
                }
            }
            //ищем совпадение
            if(yakubovich.checkAnswer(answer,tableau.getTrueAnswer(),tableau)){
                //если все буквы отгаданы - то игрок победил
                if (checkTableau(tableau)) {
                    return true;
                }
            } else {
                return false;
            }

        }
        return false;
    }
//5.6 метод сыграть раунд
    public void playRound(Player[] players){
        boolean isWin = false;
        while (!isWin){
            for (int i = 0; i < PLAYERS; i++) {
                isWin = move(questions[i], players[i]);
                if (isWin) {
                    if(!Yakubovich.isIsFinalRound()) {
                        yakubovich.askForWinner(players[i].getName(),
                              players[i].getCity(), false);
                        winners.add(players[i]);
                    } else {
                        Yakubovich.setIsFinalRound(true);
                        yakubovich.askForWinner(players[i].getName(), players[i].getCity(), true);
                  }
                  break;
              }
            }
        }
    }
//5.7 создаем метод сыграть все групповые раунды
    public void playAllGroupRounds(){
        for (int i = 0; i < GROUP_ROUNDS; i++) {
            Player[] players = createPlayers();
            tableau.setTrueAnswer(answers[i]);
            //обнуляю табло
            tableau.init();
            yakubovich.invite(players, i+1);
            yakubovich.askingQuestion(i,questions);
            System.out.println(" " + String.join(" ", tableau.getLettersOnTableau()) + " ");
            playRound(players);

        }
    }

//5.8 создаем метод сыграть финальный раунд
    public void playFinalRound(){
        tableau.setTrueAnswer(answers[FINAL_ROUND_INDEX]);
        tableau.init();
        Player[]win = new Player[3];
        yakubovich.invite(winners.toArray(win),FINAL_ROUND_INDEX+1);
        yakubovich.askingQuestion(FINAL_ROUND_INDEX,questions);
        System.out.println(" " + String.join(" ", tableau.getLettersOnTableau()) + " ");
        playRound(winners.toArray(win));

    }
    //5.9 Метод Старт
    public void start(){
        yakubovich.firstPhrase();
        playAllGroupRounds();
        playFinalRound();
        yakubovich.goodbyePhrase();
    }
}
