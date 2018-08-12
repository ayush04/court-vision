package com.cv.utils;

import com.cv.events.EventsData;
import com.cv.events.EventsService;
import com.cv.exception.InvalidGameException;
import com.cv.game.GameService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author ayush
 */
public class DataCreator {
    private static int playerInPossessionOfBall = 1;
    private static int ballX = 0;
    private static int ballY = 0;
    private static int counter = 0;
    private static int LEFT = 0;
    private static int RIGHT = 1;
    private static final int leftBasketX = 0;
    private static final int leftBasketY = 225;
    private static final int rightBasketX = 840;
    private static final int rightBasketY = 225;
    private static final List<Position> player1Positions = new ArrayList<>();
    private static final List<Position> player2Positions = new ArrayList<>();
    private static final List<Position> player3Positions = new ArrayList<>();
    private static final List<Position> player4Positions = new ArrayList<>();
    private static final List<Position> player5Positions = new ArrayList<>();
    private static final List<Position> player6Positions = new ArrayList<>();
    private static final List<Position> player7Positions = new ArrayList<>();
    private static final List<Position> player8Positions = new ArrayList<>();
    private static final List<Position> player9Positions = new ArrayList<>();
    private static final List<Position> player10Positions = new ArrayList<>();
    private static final List<List<Position>> playerPositions = new ArrayList<>();
    
    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }
    
    private static void getPlayerPosition(int playerId) {
        if(playerId < 6) {
            if(playerInPossessionOfBall < 6) {
                //move right
            }
            else {
                //move left
            }
        }
        else {
            if (playerInPossessionOfBall < 6) {
                //move right
            } else {
                //move left
            }
        }
    };
    
    private static int getPlayerNearestToBasket(int p) {
        double smallestDistance = 0;
        int playerId = 0;
        if(p == LEFT) {
            for(int i=1; i<=5; i++) {
                Position position = playerPositions.get(i).get(counter);
                double d = distance(leftBasketX, leftBasketY, position.getX(), position.getY());
                if(d < smallestDistance) {
                    smallestDistance = d;
                    playerId = i;
                }
            }
        }
        else {
            for (int i = 6; i <= 10; i++) {
                Position position = playerPositions.get(i).get(counter);
                double d = distance(rightBasketX, rightBasketY, position.getX(), position.getY());
                if (d < smallestDistance) {
                    smallestDistance = d;
                    playerId = i;
                }
            }
        }
        return playerId;
    }
    
    private static int nextPlayer() {
        if(playerInPossessionOfBall < 6) {
            if(Math.random() < 0.7) {
                return ThreadLocalRandom.current().nextInt(1, 6);
            }
            else {
                return ThreadLocalRandom.current().nextInt(6, 11);
            }
        }
        else {
            if (Math.random() < 0.7) {
                return ThreadLocalRandom.current().nextInt(6, 11);
            } else {
                return ThreadLocalRandom.current().nextInt(1, 6);
            }
        }
    }
    
    public static void main(String args[]) throws InvalidGameException {
//        playerPositions.add(player1Positions);
//        playerPositions.add(player2Positions);
//        playerPositions.add(player3Positions);
//        playerPositions.add(player4Positions);
//        playerPositions.add(player5Positions);
//        playerPositions.add(player6Positions);
//        playerPositions.add(player7Positions);
//        playerPositions.add(player8Positions);
//        playerPositions.add(player9Positions);
//        playerPositions.add(player10Positions);
//        
//        System.out.println(playerInPossessionOfBall);
//        for(int i=0; i<20; i++) {
//            playerInPossessionOfBall = nextPlayer();
//            System.out.println(playerInPossessionOfBall);
//        }

        long time = 1532038907385L;
        long quarter = 120000L;
        for (int i=1; i<5; i++) {
            EventsData event = new EventsData();
            event.setEventTime(time + (quarter) * i);
            event.setEventType(Constants.QUARTER_END_EVENT_TYPE);
            event.setGameId(7);
            EventsService.createEvent(event);
        }
    }
}
