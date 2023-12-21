package org.javaacademy.wonderfield;

import java.util.*;
import org.javaacademy.wonderfield.gift.Gift;
import org.javaacademy.wonderfield.host.Yakubovich;
import org.javaacademy.wonderfield.player.Player;

public class Game {
    public static final  int PLAYERS_NUM = 3;
    public static final int ROUNDS = 6;
    public static final int GROUP_ROUNDS = 3;
    public static final int FINAL_ROUND_INDEX = 3;
    public static final int SUPER_ROUND_INDEX = 5;
    public static final Scanner SCANNER = new Scanner(System.in);
    public boolean isFinalRound;
    //5.1 поле winners
    private ArrayList<Player> winners = new ArrayList<>();
    private Tableau tableau = new Tableau();
    private Yakubovich yakubovich = new Yakubovich();
    private Player absoluteWinner;
    private String[] superGifts = {"Замок в Австрии", "200 кг золота", "АВТОМОБИИИИЛЬ!"};
    private ArrayList<String> winnerTakeGifts = new ArrayList<>();
    private Question[] questions = new Question[6];

    //1.5 создание метода init()
    public void init() {
        System.out.println("Запуск игры \"Поле Чудес\" - подготовка к игре."
                + " Вам нужно ввести вопросы и ответы для игры.\n");
        questions[0] = new Question();
        questions[0].setQuestion("tech");
        questions[0].setAnswer("tech");

        for (int i = 1; i < 6; i++) {
            questions[i] = new Question();
            System.out.printf("\"Введите вопрос #%d\"\n", i);
            questions[i].setQuestion(SCANNER.nextLine());
            System.out.printf("\"Введите ответ вопрос #%d\"\n", i);
            questions[i].setAnswer(SCANNER.nextLine());
        }
        System.out.println("Иницализация закончена, игра начнется через 5 секунд");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(("\n").repeat(50));
    }

    //Кладем в одну из шкатулок деньги(true)
    public void putMoneyToBox(Box leftBox, Box rightBox) {
        int selector = (int) (Math.random() * 10);
        if (selector % 2 == 0) {
            leftBox.setHaveMoney(true);
        } else {
            rightBox.setHaveMoney(true);
        }
    }

    //Выбор шкатулки
    public void chooseBox(Player player, Box leftBox, Box rightBox) {
        yakubovich.boxesToRoom();
        System.out.println("Выберите левую или правую шкатулку. Для выбора нажмите 'л' или 'п'");
        while (true) {
            String choose = SCANNER.nextLine();
            if (choose.equals("л")) {
                player.setMoneyFromBox(leftBox.openBox());
                break;
            } else if (choose.equals("п")) {
                player.setMoneyFromBox(rightBox.openBox());
                break;
            } else {
                System.out.println("введите 'л' или 'п'");
            }
        }
    }

    //5.2 метод создания игроков
    public Player[] createPlayers() {
        Player[] players = new Player[3];

        for (int i = 0; i < PLAYERS_NUM; i++) {
            System.out.println("Игрок №" + (i + 1)
                    + " представьтесь: имя,город. Например: Иван,Москва");
            while (true) {
                String data = SCANNER.nextLine();
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
    public String[] namesOfPlayers(Player[] players) {
        String[] names = new String[3];
        for (int i = 0; i < players.length; i++) {
            names[i] = players[i].getName();
        }
        return names;
    }

    //5.4 проверка табло
    public boolean checkTableau(Tableau tableau) {
        return tableau.containsUnknownWords();
    }

    //Начисление очков игроку
    public void checkSector(Player player, int sectorNumber) {
        if (sectorNumber == 13) {
            player.setPoints(player.getPoints() * 2);
        } else {
            int points = Wheel.values()[sectorNumber].getPoints();
            player.setPoints(player.getPoints() + points);
        }
    }

    //5.5 Метод хода игрока
    public boolean move(String question, Player player) {
        String answer = "";
        boolean exit = false;
        while (!exit) {
            int sector = player.runTheWheel();
            Wheel wheel = Wheel.values()[sector];
            sector = wheel.ordinal();
            boolean sectorZero = sectorZero(player, sector);
            if (sectorZero) {
                return false;
            }
            answer = player.move();
            if (yakubovich.checkAnswer(answer, tableau.getTrueAnswer(), tableau)) {
                checkSector(player, sector);
                player.setThreeInRow(player.getThreeInRow() + 1);
                haveThreeInRow(player);
                if (checkTableau(tableau)) {
                    return true;
                }
            } else {
                player.setThreeInRow(0);
                return false;
            }
        }
        player.setThreeInRow(0);
        return false;
    }

    //Если есть 3 в ряд
    public void haveThreeInRow(Player player) {
        if (player.getThreeInRow() == 3 && player.getMoneyFromBox() == 0) {
            Box leftBox = new Box(false);
            Box rightBox = new Box(false);
            playBoxes(player, leftBox, rightBox);
        }
    }

    public boolean sectorZero(Player player, int sector) {
        if (sector == 0) {
            player.setThreeInRow(0);
            yakubovich.moveSkip();
            return true;
        } else {
            return false;
        }
    }

    public void playBoxes(Player player, Box leftBox, Box rightBox) {
        //кладем деньги в одну из шкатулок
        putMoneyToBox(leftBox, rightBox);
        //выбирает шкатулку
        chooseBox(player, leftBox, rightBox);
        System.out.println();
        //обнуляем серию из правильных букв
        player.setThreeInRow(0);
    }

    //5.6 метод сыграть раунд
    public Player playRound(Player[] players, boolean isFinalRound) {
        boolean isWin = false;
        while (!isWin) {
            for (int i = 0; i < PLAYERS_NUM; i++) {
                isWin = move(questions[i].getQuestion(), players[i]);
                if (!isWin) {
                    continue;
                } else if (isWin && !isFinalRound) {
                    yakubovich.askForWinner(players[i].getName(),
                            players[i].getCity(), false);
                    winners.add(players[i]);
                } else {
                    yakubovich.askForWinner(players[i].getName(),
                            players[i].getCity(), true);
                    return players[i];
                }
                break;
            }
        }
        return null;
    }

    //5.7 создаем метод сыграть все групповые раунды
    public void playAllGroupRounds() {
        for (int i = 1; i < GROUP_ROUNDS + 1; i++) {
            Player[] players = createPlayers();
            tableau.setTrueAnswer(questions[i].getAnswer());
            //обнуляю табло
            tableau.init();
            isFinalRound = false;
            yakubovich.invite(players, i);
            yakubovich.askQuestion(i, questions);
            System.out.println(" " + String.join(" ", tableau.getLetters()) + " ");
            playRound(players, isFinalRound);
        }
    }

    //5.8 создаем метод сыграть финальный раунд
    public void playFinalRound() {
        tableau.setTrueAnswer(questions[FINAL_ROUND_INDEX + 1].getAnswer());
        tableau.init();
        Player[] winner = new Player[3];
        isFinalRound = true;
        yakubovich.invite(winners.toArray(winner), FINAL_ROUND_INDEX + 1);
        yakubovich.askQuestion(FINAL_ROUND_INDEX + 1, questions);
        System.out.println(" " + String.join(" ", tableau.getLetters()) + " ");
        Player gameWinner = playRound(winners.toArray(winner), isFinalRound);
        absoluteWinner = gameWinner;
        System.out.println("Победитель " + gameWinner.getName()
                + " набрал " + gameWinner.getPoints() + " очков");
    }

    //Играем Супер Игру
    public boolean playSuperGame() {
        String letter;
        tableau.setTrueAnswer(questions[SUPER_ROUND_INDEX].getAnswer());
        tableau.init();
        //Якубович говорит про финальный раунд
        yakubovich.askQuestion(SUPER_ROUND_INDEX, questions);
        tableau.show();
        System.out.println("Введите 3 буквы из ответа");
        for (int i = 0; i < 3; i++) {
            letter = SCANNER.nextLine();
            if (tableau.getTrueAnswer().contains(letter.toUpperCase())) {
                tableau.openLetter(letter);
            }
        }
        yakubovich.sayWord();
        if (SCANNER.nextLine().equalsIgnoreCase(tableau.getTrueAnswer())) {
            return true;
        } else {
            return false;
        }
    }

    //5.9 Метод Старт
    public void start() {
        yakubovich.firstPhrase();
        playAllGroupRounds();
        playFinalRound();
        takeGifts();
        yakubovich.goodbyePhrase();
    }

    //Печатаем подарки
    public void printGifts() {
        System.out.println("Выберите подарки за накопленные очки:");
        for (int i = 1; i < Gift.values().length; i++) {
            System.out.println(i + " : " + Gift.values()[i].getName()
                    + " | " + Gift.values()[i].getPrice());
        }
    }

    //Выбирает подарки
    public void takeGifts() {
        int choice;
        while (absoluteWinner.getPoints() > 499) {
            printGifts();
            System.out.println("Осталось очков : " + absoluteWinner.getPoints());
            choice = Integer.parseInt(SCANNER.nextLine());
            switch (choice) {
                case 1:
                    chosenGift(500, choice);
                    break;
                case 2:
                    chosenGift(1000, choice);
                    break;
                case 3:
                    chosenGift(1500, choice);
                    break;
                case 4:
                    chosenGift(2000, choice);
                    break;
                case 5:
                    chosenGift(2500, choice);
                    break;
                case 6:
                    chosenGift(3000, choice);
                    break;
                default:
                    System.out.println("Введите цифру согласно выбора!");
                    break;
            }
        }
        offerToPlaySuperGame();
    }

    public void chosenGift(int points, int choice) {
        if (checkPoints(points)) {
            System.out.println("Вы выбрали :" + Gift.values()[choice].getName());
            absoluteWinner.setPoints(absoluteWinner.getPoints() - points);
            winnerTakeGifts.add(Gift.values()[choice].getName());
        }
    }

    //Выбирает вещи за очки
    public boolean checkPoints(int points) {
        if (absoluteWinner.getPoints() < points) {
            System.out.println("Не хватает баллов");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return false;
        }
        return true;
    }

    // Предложение сыграть в суперигру и тут же присвоение суперподарка
    public void offerToPlaySuperGame() {
        System.out.println("Хотите ли вы играть в суперигру? Введите 'д'(да)'н'(нет)");
        int superGiftsIndex = (int) (Math.random() * superGifts.length);
        String randomGift = superGifts[superGiftsIndex];
        String answer = SCANNER.nextLine();
        if (answer.equalsIgnoreCase("д")) {
            boolean win = playSuperGame();
            if (win) {
                yakubovich.superGameWinner();
                prizes(absoluteWinner, winnerTakeGifts, randomGift);
            } else {
                yakubovich.superGameLoser(absoluteWinner);
            }
        } else {
            yakubovich.refuseSuperGame(absoluteWinner);
            prizes(absoluteWinner, winnerTakeGifts, randomGift);
        }
    }

    //Перечисление выигранного в конце
    public void prizes(Player player, List<String> gifts, String randomGift) {
        System.out.println("Игрок " + player.getName() + " выиграл : ");
        for (String gift : gifts) {
            System.out.println(gift);
        }
        if (player.getMoneyFromBox() > 0) {
            System.out.println("Деньги из шкатулки : " + player.getMoneyFromBox());
        }
        yakubovich.saySuperThing(randomGift);
    }
}
