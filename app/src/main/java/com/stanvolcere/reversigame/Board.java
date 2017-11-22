package com.stanvolcere.reversigame;



import java.util.HashMap;

import java.util.Map;

/**
 * Created by Stan Wayne Volcere on 11/14/2017.
 */

public class Board {

//    Map m1 = new HashMap();
//      m1.put("Zara", "8");
//      m1.put("Mahnaz", "31");
//      m1.put("Ayan", "12");
//      m1.put("Daisy", "14");

    private int[][] board;

    private boolean boardFull = false;
    private int maxSize = 64;

    //Remember the numbers for black empty and white are
    // -1 0 1 repectively
    private int black = 1;
    private int empty = 0;
    private int white = -1;
    private boolean gameOver;

    public Board() {
        board = new int[8][8];
        //sets the middle tiles accordingly

        //black tiles
        board[3][3] = black;
        board[4][4] = black;

        //white tiles
        board[3][4] = white;
        board[4][3] = white;







    }

    public int[][] getBoard() {
        return board;
    }



    public void addToBoard(int position, int chipColour) {
        Pair pair = convertToCoordinate(position);
        board[pair.getY()][pair.getX()] = chipColour;
    }

    private Pair convertToCoordinate(int position) {
        return  new Pair((position%8), (position/8));
    }

    public boolean isBoardFull() {
        if(board.length >= maxSize){
            return true;
        }
        return false;
    }

    public void setBoardFull(boolean boardFull) {
        this.boardFull = boardFull;
    }

    public int getBoardSize(){
        return maxSize;
    }

    public int getTileAt(int x,int y) {
//        return tileRetriever[pair.getX()][pair.getY()];
        return board[y][x];
    }

    public boolean checkIsLegalOnBoard(int x, int y) {
        Pair pair = new Pair(x,y);
        if (pair.getX() > 7
                || pair.getX() < 0
                || pair.getY() > 7
                || pair.getY() < 0){
            return false;
        }
        return true;
    }

    public int getBoardWidth() {
        return 8;
    }

    public int getBoardHeight() {
        return 8;
    }

    public int getTileAtGivenPosition(int i) {
        Pair coordinates = convertToCoordinate(i);
        return getTileAt(coordinates.getX(), coordinates.getY());
    }

    public Pair getNewScores() {
        Pair scorePair = new Pair(0,0);
        int whiteCounter = 0;
        int blackCounter = 0;

        for (int i = 0; i < this.getBoardSize(); i++){
            if(this.getTileAtGivenPosition(i) == white){
                //score.setY(score.getY()+1);
                whiteCounter++;
            }
            else if (this.getTileAtGivenPosition(i) == black){
//                score.setX(score.getX()+1);
                blackCounter++;
            }
            else{
                continue;
            }
        }

        if (scorePair.getX() + scorePair.getY() == this.getBoardSize()){
            gameOver = true;
        }

        scorePair.setX(blackCounter);
        scorePair.setY(whiteCounter);
        //return new Pair()
        return scorePair;
    }

}
