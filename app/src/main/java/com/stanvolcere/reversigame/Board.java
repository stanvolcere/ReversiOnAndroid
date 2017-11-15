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

    Map<String, Tile> board;
    //List<Tile> boardTiles;
    boolean boardFull = false;
    int maxSize = 64;

    public Board() {
        board = new HashMap<>();
        //sets the middle tiles accordingly

        //black tiles
        board.put("44", new Tile(1));
        board.put("33", new Tile(1));
        //white tiles
        board.put("34", new Tile(-1));
        board.put("43", new Tile(-1));


    }

    public Map<String, Tile> getBoard() {
        return board;
    }

    public void setBoard(Map<String, Tile> board) {
        this.board = board;
    }

    public boolean isBoardFull() {
        return boardFull;
    }

    public void setBoardFull(boolean boardFull) {
        this.boardFull = boardFull;
    }
}
