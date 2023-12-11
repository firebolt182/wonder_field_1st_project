package org.javaacademy.wonderfield;

import java.util.*;
import org.javaacademy.wonderfield.gift.Name;
import org.javaacademy.wonderfield.gift.Price;
import org.javaacademy.wonderfield.host.Yakubovich;
import org.javaacademy.wonderfield.player.Player;
import org.javaacademy.wonderfield.player.PlayerAnswer;


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
    private Box leftBox = new Box(false);
    private Box rightBox = new Box(false);
    private String[] superGifts = {"Замок в Австрии", "200 кг золота", "АВТОМОБИИИИЛЬ!"};
    private Map<Integer, String> gifts = new TreeMap<>();
    private ArrayList<String> winnerTakeGifts = new ArrayList<>();
    private Question[] questions = new Question[6];

    public void giftsInit() {
        for (int i = 0; i < Name.values().length; i++) {
            gifts.put(Price.values()[i].getPrice(), Name.values()[i].getName());
        }

    }

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
        for (int i = 0; i < 51; i++) {
            System.out.println("");
        }
        giftsInit();
    }

    //Кладем в одну из шкатулок деньги(true)
    public void putMoneyToBox() {
        int selector = (int) (Math.random() * 10);
        if (selector % 2 == 0) {
            leftBox.setHaveMoney(true);
        } else {
            rightBox.setHaveMoney(true);
        }
    }

    //Выбор шкатулки
    public void chooseBox(Player player) {
        yakubovich.boxesToRoom();
        System.out.println("Выберите левую или правую шкатулку. Для выбора нажмите 'л' или 'п'");
        while (true) {
            String choose = SCANNER.nextLine();
            if (choose.equals("л")) {
                leftBox.openBox(player);
                break;
            } else if (choose.equals("п")) {
                rightBox.openBox(player);
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
            int points = Wheel.values()[sectorNumber].ordinal();
            player.setPoints(player.getPoints() + (points * 100));
        }
    }

    //5.5 Метод хода игрока
    public boolean move(String question, Player player) {
        PlayerAnswer playerAnswer = new PlayerAnswer(player);
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
            answer = playerAnswer.move();
            if (yakubovich.checkAnswer(answer, tableau.getTrueAnswer(), tableau)) {
                checkSector(player, sector);
                player.setThreeInaRow(player.getThreeInaRow() + 1);
                haveThreeInaRow(player);
                if (checkTableau(tableau)) {
                    return true;
                }
            } else {
                player.setThreeInaRow(0);
                return false;
            }
        }
        player.setThreeInaRow(0);
        return false;
    }

    //Если есть 3 в ряд
    public void haveThreeInaRow(Player player) {
        if (player.getThreeInaRow() == 3 && !player.isHaveMoneyFromBox()) {
            playBoxes(player);
        }
    }

    public boolean sectorZero(Player player, int sector) {
        if (sector == 0) {
            player.setThreeInaRow(0);
            yakubovich.moveSkip();
            return true;
        } else {
            return false;
        }
    }

    public void playBoxes(Player player) {
        //кладем деньги в одну из шкатулок
        putMoneyToBox();
        //выбирает шкатулку
        chooseBox(player);
        System.out.println();
        //обнуляем серию из правильных букв
        player.setThreeInaRow(0);
        //проверка - есть ли неизвестные буквы на табло
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
            isFinalRound = yakubovich.invite(players, i);
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
        isFinalRound = yakubovich.invite(winners.toArray(winner), FINAL_ROUND_INDEX + 1);
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
        int num = 1;

        System.out.println("Выберите подарки за накопленные очки:");
        for (Map.Entry<Integer, String> gift : gifts.entrySet()) {
            System.out.println(num + " " + gift.getKey() + " | " + gift.getValue());
            num++;
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

    //Выбирает подарки
    public void takeGifts() {
        String choice;
        while (absoluteWinner.getPoints() > 499) {
            printGifts();
            System.out.println("Осталось очков : " + absoluteWinner.getPoints());
            choice = SCANNER.nextLine();
            switch (choice) {
                case "1":
                    chosenGift(500);
                    break;
                case "2":
                    chosenGift(1000);
                    break;
                case "3":
                    chosenGift(1500);
                    break;
                case "4":
                    chosenGift(2000);
                    break;
                case "5":
                    chosenGift(2500);
                    break;
                case "6":
                    chosenGift(3000);
                    break;
                default:
                    System.out.println("Введите цифру согласно выбора!");
                    break;
            }
        }
        offerToPlaySuperGame();
    }

    public void chosenGift(int points) {
        if (checkPoints(points)) {
            System.out.println("Вы выбрали :" + gifts.get(points));
            absoluteWinner.setPoints(absoluteWinner.getPoints() - points);
            winnerTakeGifts.add(gifts.get(points));
        }
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
        if (player.isHaveMoneyFromBox()) {
            System.out.println("Деньги из шкатулки");
        }
        yakubovich.saySuperThing(randomGift);
    }
}
