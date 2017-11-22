package com.stanvolcere.reversigame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Board board;
    private Pair score = new Pair(2,2);
    private boolean blackTurn = true;
    private static  final int black = 1;
    private static  final int white = -1;
    private static  final int empty = 0;
    private static  int chipType;
    private static  boolean gameOver = false;
    private TextView whiteScore;
    private TextView blackScore;
    private TextView turn;
    private Button quitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Hi there, welcome to the Reversi Prototype" , Toast.LENGTH_SHORT).show();
        board = new Board();
        whiteScore = (TextView) findViewById(R.id.whiteChipScore);
        blackScore = (TextView) findViewById(R.id.blackChipScore);
        turn = (TextView) findViewById(R.id.turnTextView);
        quitButton = (Button) findViewById(R.id.quitButton);

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        if (!blackTurn){
            turn.setText("White Chip it's your turn!");
        } else {
            turn.setText("Black Chip it's your turn!");
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //updateGameState(gridview, parent, v);

                if(blackTurn){
                    chipType = black;

                } else {
                    chipType = white;
                }



                if(checkAvailableTile(position)) {
                    if (checkLegalMove(board, position,chipType)) {
                        ImageView tile = (ImageView) gridview.getAdapter().getView(position,v,parent);
                        if (blackTurn) {
                            tile.setImageResource(R.drawable.blackchip);
                            blackTurn = false;
                            //Toast.makeText(MainActivity.this, "A Black chip was added at - " + position, Toast.LENGTH_SHORT).show();
                            board.addToBoard(position,black);

                            //Section is to update the Board!
                            ArrayList<Integer> listOfCoordinatesToChange = new ArrayList<>();
                            if(checkLegalMove( board,position, chipType)){
                                listOfCoordinatesToChange.addAll(checkDiagonally(board, position, chipType));
                                listOfCoordinatesToChange.addAll(checkHorizontal(board, position, chipType));
                                listOfCoordinatesToChange.addAll(checkVertical(board, position, chipType));

                                ImageView imageView;
                                for(Integer changeAt : listOfCoordinatesToChange) {
                                    //tile = (ImageView) gridview.getAdapter().getView(changeAt, v, parent);
                                    //tile.setImageDrawable();
                                    imageView = (ImageView) gridview.getChildAt(changeAt);
                                    imageView.setImageResource(R.drawable.blackchip);
                                    board.addToBoard(changeAt,black);
                                }
                            }
                            score = board.getNewScores();
                            //score.setX(score.getX()+1);
                        } else {
                            //oast.makeText(MainActivity.this, "A White Chip was added at - " + position, Toast.LENGTH_SHORT).show();
                            blackTurn = true;
                            tile.setImageResource(R.drawable.whitechip);
                            board.addToBoard(position,white);

                            //Section is to update the Board!
                            ArrayList<Integer> listOfCoordinatesToChange = new ArrayList<>();
                            if(checkLegalMove( board,position, chipType)){

                                listOfCoordinatesToChange.addAll(checkDiagonally(board, position, chipType));
                                listOfCoordinatesToChange.addAll(checkHorizontal(board, position, chipType));
                                listOfCoordinatesToChange.addAll(checkVertical(board, position, chipType));

                                ImageView imageView;
                                for(Integer changeAt : listOfCoordinatesToChange) {
                                    //tile = (ImageView) gridview.getAdapter().getView(changeAt, v, parent);
                                    //tile.setImageDrawable();
                                    imageView = (ImageView) gridview.getChildAt(changeAt);
                                    imageView.setImageResource(R.drawable.whitechip);
                                    board.addToBoard(changeAt,white);
                                }
                            }
                            score = board.getNewScores();
                            //score.setY(score.getY()+1);
                        }
//

                    }else{Toast.makeText(MainActivity.this, "Move at - " + position + "is Unavailable. Try Again",  Toast.LENGTH_SHORT).show();}
                } else {
                    Toast.makeText(MainActivity.this, "Move at - " + position + "is Unavailable. Try Again",  Toast.LENGTH_SHORT).show();
                }
                whiteScore.setText(score.getY() + "");
                blackScore.setText(score.getX() + "");

                if (!blackTurn){
                    turn.setText("White Chip it's your turn!");
                } else {
                    turn.setText("Black Chip it's your turn!");
                }
            }
        });

        //setOnItemClickListener(new AdapterView.OnItemClickListener()
        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "You are about to quit the game!",  Toast.LENGTH_SHORT).show();
            }
        });


    }


//    private void updateBoard(int position, int chipType, GridView grid) {
//
//        ArrayList<Integer> listOfCoordinatesToChange = new ArrayList<>();
//        ImageView newChip;
//
//        if(checkLegalMove( board,position, chipType)){
//            //listOfCoordinatesToChange.addAll(checkHorizontal(board, position, chipType));
//            listOfCoordinatesToChange.addAll(checkVertical(board, position, chipType));}
//            //listOfCoordinatesToChange.addAll(checkDiagonally(board, position, chipType));
//
//        for(Integer changeAt : listOfCoordinatesToChange){
//            ImageView tile = (ImageView) grid.getAdapter().getView(changeAt,v,parent);
//        }
//
//    }

    private boolean checkLegalMove(Board board, int position, int chiptype) {

        if (checkVertical(board,position,chiptype).isEmpty()
                && checkHorizontal(board,position,chiptype).isEmpty()
                    && checkDiagonally(board,position,chiptype).isEmpty()){
            return false;
        }
        return true;
    }


//    private void updateGameState( GridView gridView, AdapterView<?> parent, View v) {
//        ImageView tile;
//
//        for(int i = 0; i <= board.getBoardSize()-1;i++){
//            tile = (ImageView) gridView.getAdapter().getView(i,v,parent);
//            if(tile.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.blackchip).getConstantState())){
//                board.addToBoard(i,black);
//            } else if(tile.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.whitechip).getConstantState())){
//                board.addToBoard(i,white);
//            } else {
//                board.addToBoard(i,empty);
//            }
//
//        }
//    }

    //update the board with int[]
//    private void updateGameState(){
//        for(int i = 0; i <= board.getBoardSize()-1;i++){
//
//        }
//    }

    private boolean checkAvailableTile(int position) {
        Pair pair = convertPositionToCoordinate(position);
        int help = board.getTileAt(pair.getX(),pair.getY());

        if(help == empty) {
            //Toast.makeText(MainActivity.this, "legal " + position, Toast.LENGTH_SHORT).show();
            return true;
        }
        //Toast.makeText(MainActivity.this, "Not a Legal move " + position, Toast.LENGTH_SHORT).show();
        return false;

    }

//    private static ArrayList<Pair> checkVertical(Board board, int position){
//
//        ArrayList<Pair> listOfCoordinatesToChange = new ArrayList<Pair>();
//
//        if(position < board.getBoardSize()){
//            for (int i = (pair.getY())+1; i < board.length; i++){
//                if(board[i][pair.getX()].equals(EMPTY_TILE)){
//                    if(listOfCoordinatesToChange.isEmpty()){
//                        break;
//                    } else {
//                        listOfCoordinatesToChange.clear();
//                        break;
//                    }
//                } else if (board[i][pair.getX()].equals(tile)){
//                    if(listOfCoordinatesToChange.isEmpty()){
//                        break;
//                    } else {
//                        return listOfCoordinatesToChange;
//                    }
//                } else {
//                    listOfCoordinatesToChange.add(new Pair(pair.getX(), i));
//                }
//            }
//        }
//
//        if((pair.getY()-1) >= 0){
//            for (int j = (pair.getY()-1); j >= 0; j--){
//                if(board[j][pair.getX()].equals(EMPTY_TILE)){
//                    if(listOfCoordinatesToChange.isEmpty()){
//                        break;
//                    } else {
//                        listOfCoordinatesToChange.clear();
//                        break;
//                    }
//                } else if (board[j][pair.getX()].equals(tile)){
//                    if(listOfCoordinatesToChange.isEmpty()){
//                        break;
//                    } else {
//                        return listOfCoordinatesToChange;
//                    }
//                } else {
//                    listOfCoordinatesToChange.add(new Pair(pair.getX(), j));
//                }
//            }
//        }
//
//
//        return listOfCoordinatesToChange;
//
//    }

    private static ArrayList<Integer> checkHorizontal(Board board, int position, int chipType) {

        ArrayList<Integer> listOfPositionsToChange = new ArrayList<Integer>();
        Pair pair = convertPositionToCoordinate(position);
        int initialYPos = pair.getY();
        int initialXPos = pair.getX();

        int helper = position;

        if (position < board.getBoardSize()) {
            for (int i = initialXPos + 1; i < board.getBoardWidth(); i++) {
                if (board.checkIsLegalOnBoard(i, initialYPos)) {
//                if (!board.checkBoardEdge(initialXPos,i)) {
                    if (board.getTileAt(i, initialYPos) == empty) {
                        if (listOfPositionsToChange.isEmpty()) {
                            break;
                        } else {
                            listOfPositionsToChange.clear();
                            break;
                        }
                    } else if (board.getTileAt(i, initialYPos) == chipType) {
                        if (listOfPositionsToChange.isEmpty()) {
                            break;
                        } else {
                            return listOfPositionsToChange;
                        }
                    } else {
                        helper ++;
                        if (helper < board.getBoardSize()) {
                            listOfPositionsToChange.add(helper);
                        }

                    }
                }

            }

            //initialXPos = (position - 1)/board.getBoardWidth();

            helper = position;
            if (position < board.getBoardSize()) {
                for (int j = initialXPos - 1; j >= 0; j--) {
                    if (board.checkIsLegalOnBoard(j, initialYPos)) {
                        if (board.getTileAt(j, initialYPos) == empty) {
                            if (listOfPositionsToChange.isEmpty()) {
                                break;
                            } else {
                                listOfPositionsToChange.clear();
                                break;
                            }
                        } else if (board.getTileAt(j, initialYPos) == chipType) {
                            if (listOfPositionsToChange.isEmpty()) {
                                break;
                            } else {
                                return listOfPositionsToChange;
                            }
                        } else {
                            helper -= 1;
                            if (helper > 0) {
                                listOfPositionsToChange.add(helper);
                            }

                        }
                    }
                }
            }
        }
        return listOfPositionsToChange;
    }

    private static ArrayList<Integer> checkVertical(Board board, int position, int chipType){
        ArrayList<Integer> listOfPositionsToChange = new ArrayList<Integer>();

        Pair pair = convertPositionToCoordinate(position);
        int initialYPos = pair.getY();
        int initialXPos = pair.getX();

        int helper = position;

        if (position < board.getBoardSize()) {
            for (int i = initialYPos + 1; i < board.getBoardWidth(); i++) {
                if (board.checkIsLegalOnBoard(initialXPos, i)) {
//                if (!board.checkBoardEdge(initialXPos,i)) {
                    if (board.getTileAt(initialXPos, i) == empty) {

                        if (listOfPositionsToChange.isEmpty()) {
                            break;
                        } else {
                            listOfPositionsToChange.clear();
                            break;
                        }
                    } else if (board.getTileAt(initialXPos, i) == chipType) {
                        if (listOfPositionsToChange.isEmpty()) {
                            break;
                        } else {
                            return listOfPositionsToChange;
                        }
                    } else {
                        helper += 8;
                        if (helper < board.getBoardSize()) {
                            listOfPositionsToChange.add(helper);
                        }

                    }
                }

            }

            //initialXPos = (position - 1)/board.getBoardWidth();

            helper = position;
            if (position < board.getBoardSize()) {
                for (int j = initialYPos - 1; j >= 0; j--) {
                    if (board.checkIsLegalOnBoard(initialXPos, j)) {
                        if (board.getTileAt(initialXPos, j) == empty) {
                            if (listOfPositionsToChange.isEmpty()) {
                                break;
                            } else {
                                listOfPositionsToChange.clear();
                                break;
                            }
                        } else if (board.getTileAt(initialXPos, j) == chipType) {
                            if (listOfPositionsToChange.isEmpty()) {
                                break;
                            } else {
                                return listOfPositionsToChange;
                            }
                        } else {
                            helper -= 8;
                            if (helper > 0) {
                                listOfPositionsToChange.add(helper);
                            }

                        }
                    }
                }
            }
        }
        return listOfPositionsToChange;
    }

    //returns an arraylist<Integer> of all the coordinates that require change due to a move
    private static ArrayList<Integer> checkDiagonally(Board board, int position, int chipType){

        ArrayList<Integer> listOfCoordinatesToChange=new ArrayList<>();

        listOfCoordinatesToChange.addAll(checkUpLeft(board, position, chipType));
        listOfCoordinatesToChange.addAll(checkUpRight(board, position, chipType));
        listOfCoordinatesToChange.addAll(checkDownLeft(board,position, chipType));
        listOfCoordinatesToChange.addAll(checkDownRight(board, position, chipType));

        //
        return listOfCoordinatesToChange;
    }

    private static ArrayList<Integer> checkUpLeft(Board board, int position, int chipType) {

        Pair pair = convertPositionToCoordinate(position);
        int initialX = pair.getX()-1;
        int initialY = pair.getY()-1;
        ArrayList<Integer> listOfCoordinatesToChange = new ArrayList<Integer>();
        int helper = position;

        if (initialX > 0 && initialY > 0){
            do {
                if(board.getTileAt(initialX, initialY) == empty){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        listOfCoordinatesToChange.clear();
                        break;
                    }
                    //(board.getTileAt(initialX, initialY) == empty)
                } else if (board.getTileAt(initialX, initialY) == chipType){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        return listOfCoordinatesToChange;
                    }
                } else {
                    helper -= 9;
                    if (helper > 0) {
                        listOfCoordinatesToChange.add(helper);
                    }
                }
                initialY--;
                initialX--;

            } while (initialX > 0 && initialY > 0);
        } //else {
        return listOfCoordinatesToChange;
    }

    private static ArrayList<Integer> checkDownLeft(Board board, int position, int chipType) {

        Pair pair = convertPositionToCoordinate(position);
        int initialX = pair.getX()-1;
        int initialY = pair.getY()+1;
        ArrayList<Integer> listOfCoordinatesToChange = new ArrayList<Integer>();
        int helper = position;

        if (initialX > 0 && initialY < board.getBoardWidth()){
            do {
                if(board.getTileAt(initialX, initialY) == empty){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        listOfCoordinatesToChange.clear();
                        break;
                    }
                    //(board.getTileAt(initialX, initialY) == empty)
                } else if (board.getTileAt(initialX, initialY) == chipType){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        return listOfCoordinatesToChange;
                    }
                } else {
                    helper += 7;
                    if (helper > 0) {
                        listOfCoordinatesToChange.add(helper);
                    }
                }
                initialY++;
                initialX--;

            } while (initialX > 0 && initialY < board.getBoardWidth());
        } //else {
        return listOfCoordinatesToChange;

    }

    private static ArrayList<Integer> checkDownRight(Board board, int position, int chipType) {

        Pair pair = convertPositionToCoordinate(position);
        int initialX = pair.getX()+1;
        int initialY = pair.getY()+1;
        ArrayList<Integer> listOfCoordinatesToChange = new ArrayList<Integer>();
        int helper = position;

        if (initialX < board.getBoardWidth() && initialY < board.getBoardWidth()){
            do {
                if(board.getTileAt(initialX, initialY) == empty){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        listOfCoordinatesToChange.clear();
                        break;
                    }
                    //(board.getTileAt(initialX, initialY) == empty)
                } else if (board.getTileAt(initialX, initialY) == chipType){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        return listOfCoordinatesToChange;
                    }
                } else {
                    helper += 9;
                    if (helper > 0) {
                        listOfCoordinatesToChange.add(helper);
                    }
                }
                initialY++;
                initialX++;

            } while (initialX < board.getBoardWidth() && initialY < board.getBoardWidth());
        } //else {
        return listOfCoordinatesToChange;
    }

    private static ArrayList<Integer> checkUpRight(Board board, int position, int chipType){

        Pair pair = convertPositionToCoordinate(position);
        int initialX = pair.getX()+1;
        int initialY = pair.getY()-1;
        ArrayList<Integer> listOfCoordinatesToChange = new ArrayList<Integer>();
        int helper = position;

        if (initialX < board.getBoardWidth() && initialY > 0){
            do {
                if(board.getTileAt(initialX, initialY) == empty){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        listOfCoordinatesToChange.clear();
                        break;
                    }
                    //(board.getTileAt(initialX, initialY) == empty)
                } else if (board.getTileAt(initialX, initialY) == chipType){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        return listOfCoordinatesToChange;
                    }
                } else {
                    helper -= 7;
                    if (helper > 0) {
                        listOfCoordinatesToChange.add(helper);
                    }
                }
                initialY--;
                initialX++;

            } while (initialX < board.getBoardWidth() && initialY > 0);
        } //else {
        return listOfCoordinatesToChange;
    }
//    private static int checkSameYLevel(int position) {
//        Pair pair = convertPositionToCoordinate(position);
//        return pair.getY();
//    }

    private static Pair convertPositionToCoordinate(int position) {
        return  new Pair((position%8), (position/8));
    }


}
