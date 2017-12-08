package com.stanvolcere.reversigame;



import java.util.ArrayList;

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



    int[][] boardValues = new int[][]{
            {  120, -20,  20,   5,   5,  20, -20, 120},
            {  -20, -40,  -5,  -5,  -5,  -5, -40, -20},
            {   20,  -5,  15,   3,   3,  15,  -5,  20},
            {    5,  -5,   3,   3,   3,   3,  -5,   5},
            {    5,  -5,   3,   3,   3,   3,  -5,   5},
            {   20,  -5,  15,   3,   3,  15,  -5,  20},
            {  -20, -40,  -5,  -5,  -5,  -5, -40, -20},
            {  120, -20,  20,   5,   5,  20, -20, 120}
    };

    public Board() {
        board = new int[8][8];
        //sets the middle tiles accordingly

        for (int i = 0; i < this.getBoardWidth(); i++){
            for (int j = 0; j < this.getBoardWidth(); j++){
                board[i][j] = empty;
            }
        }


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

    public Pair convertToCoordinate(int position) {
        return new Pair((position%8), (position/8));
    }

    public boolean isBoardFull() {

        if(this.getAvailableCoordinates().isEmpty()){
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

        return board[y][x];
    }

    public int getValueAt(int x,int y) {

        return boardValues[y][x];
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

    public int getValueAtGivenPosition(int i) {
        Pair coordinates = convertToCoordinate(i);
        return getValueAt(coordinates.getX(), coordinates.getY());
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

    public ArrayList<Pair> getAvailableCoordinates(){

        ArrayList<Pair> availableSpots = new ArrayList<>();
        for (int i = 0; i < this.getBoardWidth(); i++){
            for (int j = 0; j < this.getBoardWidth(); j++){
                if(board[i][j] == empty){
                    availableSpots.add(new Pair(i,j));
                }
            }
        }
        return availableSpots;
    }

    public ArrayList<Pair> getAvailablePositionsAndValue(){

        ArrayList<Pair> availablePositionsAndValue = new ArrayList<>();
        //Pair pair = new Pair(0,0);
        for (int i = 0; i < this.getBoardSize(); i++){

            if(getTileAtGivenPosition(i) == empty){
//                pair.setX(i);
//                pair.setY(getBoardValueAtPosition(i));
                availablePositionsAndValue.add(new Pair(i, getBoardValueAtPosition(i)));
            }
        }
        return availablePositionsAndValue;
    }

    public int[][] getBoardValues() {
        return boardValues;
    }

    public int getBoardValueAtPosition(int position) {

        Pair boardValueCoordinates = convertToCoordinate(position);
        int boardValue = boardValues[boardValueCoordinates.getX()][boardValueCoordinates.getY()];

        return boardValue;
    }

    public Pair evaluate() {

        int min = 0;
        int max = 0;
        Pair scoreAndMove = new Pair(0,0);
        //int score = 0;
//        if (this.isBoardFull()){

            for (int i = 0; i < this.getBoardWidth(); i++){
                for (int j = 0; j < this.getBoardWidth(); j++){
                    if(board[i][j] == white){
                        min = min + boardValues[i][j];
                    } else {
                        max = max + boardValues[i][j];
                    }
                }
            }
            scoreAndMove.setY((max - min));
            scoreAndMove.setX(0);
            return scoreAndMove;
//        }
//        return scoreAndMove;

    }

//    public ArrayList<Pair> getAvailableAndLegalCoordinates() {
//
//
//    }
}
