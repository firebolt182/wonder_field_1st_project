package org.javaacademy.wonderfield;

import java.util.*;
import org.javaacademy.wonderfield.host.Yakubovich;
import org.javaacademy.wonderfield.player.Player;
import org.javaacademy.wonderfield.player.PlayerAnswer;

import static org.javaacademy.wonderfield.QuestionsAndAnswers.questions;
import static org.javaacademy.wonderfield.QuestionsAndAnswers.answers;

public class Game {
    public static final  int PLAYERS_NUM = 3;
    public static final int ROUNDS = 6;
    public static final int GROUP_ROUNDS = 3;
    public static final int FINAL_ROUND_INDEX = 3;
    public static final int SUPER_ROUND_INDEX = 4;
    public static boolean isFinalRound;
    //5.1 поле winners
    private ArrayList<Player> winners = new ArrayList<>();
    private Tableau tableau = new Tableau();
    private Yakubovich yakubovich = new Yakubovich();
    private Player absoluteWinner;

    private static Scanner scanner = new Scanner(System.in);
    private Wheel wheel = new Wheel();

    private Box leftBox = new Box(false);
    private Box rightBox = new Box(false);
    String[] superGifts = {"Замок в Австрии", "200 кг золота", "АВТОМОБИИИИЛЬ!"};
    Map<Integer, String> gifts = new TreeMap<>();
    ArrayList<String> winnerTakeGifts = new ArrayList<>();

    public void giftsInit() {
        gifts.put(2500, "Лапша БП");
        gifts.put(2000, "Шерстяные носки\"Год Дракона\"");
        gifts.put(3000, "Карта Москвича");
        gifts.put(1500, "Железный занавес");
        gifts.put(1000, "Кухонный комбайн");
        gifts.put(500, "Маринованные лопухи");

    }

    public static Scanner getScanner() {
        return scanner;
    }

    //1.5 создание метода init()
    public void init() {
        System.out.println("Запуск игры \"Поле Чудес\" - подготовка к игре."
                + " Вам нужно ввести вопросы и ответы для игры.\n");
        questions[0] = "tech";
        answers[0] = "tech";

        for (int i = 1; i < 6; i++) {
            System.out.printf("\"Введите вопрос #%d\"\n", i);
            String question = scanner.nextLine();
            questions[i] = question;
            System.out.printf("\"Введите ответ вопрос #%d\"\n", i);
            String answer = scanner.nextLine();
            answers[i] = answer;
        }

        System.out.println("Иницализация закончена, игра начнется через 5 секунд");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 51; i++) {
            System.out.println("");
        }
        wheel.init();
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

    //
    public void chooseBox(Player player) {
        yakubovich.boxesToRoom();
        System.out.println("Выберите левую или правую шкатулку. Для выбора нажмите 'л' или 'п'");
        while (true) {
            String choose = scanner.nextLine();
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

    public void checkSector(Player player, int sectorNumber) {
        if (sectorNumber == 12) {
            player.setPoints(player.getPoints() * 2);
        } else {
            int points = wheel.getSector()[sectorNumber];
            player.setPoints(player.getPoints() + points);
        }
    }

    //5.5 Метод хода игрока
    public boolean move(String question, Player player) {

        PlayerAnswer playerAnswer = new PlayerAnswer(player);
        String answer = "";
        boolean exit = false;

        while (!exit) {
            int sector = player.runTheWheel();
            //блок на проверку не пустого ответа игрока
            try {
                if (sector == 13) {
                    player.setThreeInaRow(0);
                    return false;
                }
                answer = playerAnswer.move();

            } catch (StringIndexOutOfBoundsException e) {
                while (true) {
                    if (answer.length() == 0) {
                        answer = playerAnswer.move();
                    } else {
                        player.setThreeInaRow(0);
                        return false;
                    }
                }
            }
            //ищем совпадение
            if (yakubovich.checkAnswer(answer, tableau.getTrueAnswer(), tableau)) {
                checkSector(player, sector);
                player.setThreeInaRow(player.getThreeInaRow() + 1);
                if (player.getThreeInaRow() == 3 && !player.isHaveMoneyFromBox()) {
                    putMoneyToBox();
                    chooseBox(player);
                    System.out.println();
                    player.setThreeInaRow(0);
                }
                //если все буквы отгаданы - то игрок победил
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

    //5.6 метод сыграть раунд
    public Player playRound(Player[] players) {
        boolean isWin = false;
        while (!isWin) {
            for (int i = 0; i < PLAYERS_NUM; i++) {
                isWin = move(questions[i], players[i]);
                if (isWin) {
                    if (!isFinalRound) {
                        yakubovich.askForWinner(players[i].getName(),
                                players[i].getCity(), false);
                        winners.add(players[i]);
                    } else {
                        isFinalRound = true;
                        yakubovich.askForWinner(players[i].getName(), players[i].getCity(), true);
                        return players[i];
                    }
                    break;
                }
            }
        }
        return null;
    }

    //5.7 создаем метод сыграть все групповые раунды
    public void playAllGroupRounds() {
        for (int i = 1; i < GROUP_ROUNDS + 1; i++) {
            Player[] players = createPlayers();
            tableau.setTrueAnswer(answers[i]);
            //обнуляю табло
            tableau.init();
            yakubovich.invite(players, i);
            yakubovich.askQuestion(i, questions);
            System.out.println(" " + String.join(" ", tableau.getLettersOnTableau()) + " ");
            playRound(players);

        }
    }

    //5.8 создаем метод сыграть финальный раунд
    public void playFinalRound() {
        tableau.setTrueAnswer(answers[FINAL_ROUND_INDEX + 1]);
        tableau.init();
        Player[] winner = new Player[3];
        yakubovich.invite(winners.toArray(winner), FINAL_ROUND_INDEX + 1);
        yakubovich.askQuestion(FINAL_ROUND_INDEX + 1, questions);
        System.out.println(" " + String.join(" ", tableau.getLettersOnTableau()) + " ");
        Player gameWinner = playRound(winners.toArray(winner));
        absoluteWinner = gameWinner;
        System.out.println("Победитель " + gameWinner.getName()
                + " набрал " + gameWinner.getPoints() + " очков");

    }

    //Играем Супер Игру
    public boolean playSuperGame() {
        String letter;
        tableau.setTrueAnswer(answers[SUPER_ROUND_INDEX]);
        tableau.init();
        //Якубович говорит про финальный раунд
        yakubovich.askQuestion(SUPER_ROUND_INDEX, questions);
        tableau.show();
        System.out.println("Введите 3 буквы из ответа");
        for (int i = 0; i < 3; i++) {
            letter = scanner.nextLine();
            if (tableau.getTrueAnswer().contains(letter.toUpperCase())) {
                tableau.openLetter(letter);
            }
        }
        yakubovich.sayWord();
        if (scanner.nextLine().equalsIgnoreCase(tableau.getTrueAnswer())) {
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
        int i = 1;
        System.out.println("Выберите подарки за накопленные очки:");
        for (Map.Entry gift : gifts.entrySet()) {
            System.out.println(i + ". " + gift.getKey() + " очков." + "  |  " + gift.getValue());
            i++;
        }
    }

    //Выбирает вещи за очки
    public boolean checkPoints(int points) {
        if (absoluteWinner.getPoints() < points) {
            System.out.println("Не хватает баллов");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
            choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    if (checkPoints(500)) {
                        System.out.println("Вы выбрали :" + gifts.get(500));
                        absoluteWinner.setPoints(absoluteWinner.getPoints() - 500);
                        winnerTakeGifts.add(gifts.get(500));
                    }
                    break;
                case "2":
                    if (checkPoints(1000)) {
                        System.out.println("Вы выбрали :" + gifts.get(1000));
                        absoluteWinner.setPoints(absoluteWinner.getPoints() - 1000);
                        winnerTakeGifts.add(gifts.get(1000));
                    }
                    break;
                case "3":
                    if (checkPoints(1500)) {
                        System.out.println("Вы выбрали :" + gifts.get(1500));
                        absoluteWinner.setPoints(absoluteWinner.getPoints() - 1500);
                        winnerTakeGifts.add(gifts.get(1500));
                    }
                    break;
                case "4":
                    if (checkPoints(2000)) {
                        System.out.println("Вы выбрали :" + gifts.get(2000));
                        absoluteWinner.setPoints(absoluteWinner.getPoints() - 2000);
                        winnerTakeGifts.add(gifts.get(2000));
                    }
                    break;
                case "5":
                    if (checkPoints(2500)) {
                        System.out.println("Вы выбрали :" + gifts.get(2500));
                        absoluteWinner.setPoints(absoluteWinner.getPoints() - 2500);
                        winnerTakeGifts.add(gifts.get(2500));
                    }
                    break;
                case "6":
                    if (checkPoints(3000)) {
                        System.out.println("Вы выбрали :" + gifts.get(3000));
                        absoluteWinner.setPoints(absoluteWinner.getPoints() - 3000);
                        winnerTakeGifts.add(gifts.get(3000));
                    }
                    break;
                default:
                    System.out.println("Введите цифру согласно выбора!");
                    break;
            }
        }
        offerToPlaySuperGame();
    }

    // Предложение сыграть в суперигру и тут же присвоение суперподарка
    public void offerToPlaySuperGame() {
        System.out.println("Хотите ли вы играть в суперигру? Введите 'д'(да)'н'(нет)");
        int superGiftsIndex = (int) (Math.random() * superGifts.length);
        String randomGift = superGifts[superGiftsIndex];
        String answer = scanner.nextLine();
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
