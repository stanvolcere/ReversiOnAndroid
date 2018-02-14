package com.stanvolcere.reversigame.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stanvolcere.reversigame.GameLogic.Board;
import com.stanvolcere.reversigame.ImageAdapter;
import com.stanvolcere.reversigame.Pair;
import com.stanvolcere.reversigame.R;

import java.util.ArrayList;
import java.util.Random;

public class ReversiActivity extends AppCompatActivity {

    private Board mBoard;
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
    private Button passButton;
    private Handler mHandler;
    private ArrayList<Pair> listOfMovesMade = new ArrayList<>(

    );
    private int callsToMinimax = 0;
    private int callsToAlphaBeta = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reversi);

        mHandler = new Handler();

        listOfMovesMade.add(new Pair(27,black));
        listOfMovesMade.add(new Pair(36,black));
        listOfMovesMade.add(new Pair(28,white));
        listOfMovesMade.add(new Pair(35,white));


        Toast.makeText(this, "Hi there, welcome to the Reversi Prototype" , Toast.LENGTH_SHORT).show();
        mBoard = new Board();
        whiteScore = (TextView) findViewById(R.id.whiteChipScore);
        blackScore = (TextView) findViewById(R.id.blackChipScore);
        turn = (TextView) findViewById(R.id.turnTextView);
        quitButton = (Button) findViewById(R.id.quitButton);
        passButton = (Button) findViewById(R.id.passButton);

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        if (!blackTurn){
            turn.setText("White Chip it's your turn!");
        } else {
            turn.setText("Black Chip it's your turn!");
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){


                if(blackTurn){
                    chipType = black;

                } else {
                    chipType = white;
                }


                for (Pair moves: listOfMovesMade){
                    //re-adds the moves from the original state of the board
                    mBoard.addToBoard(moves.getX(), moves.getY());
                }

                if(checkAvailableTile(mBoard,position)) {
                    if (checkLegalMove(mBoard, position,chipType)) {
                        ImageView tile = (ImageView) gridview.getAdapter().getView(position,v,parent);
                        if (blackTurn) {
                            do {
                                makeMove(blackTurn,tile, mBoard,position,gridview);
                            } while (!opponentHasMove(mBoard, white));

                            Toast.makeText(ReversiActivity.this, "Game is Thinking!",  Toast.LENGTH_SHORT).show();
                            blackTurn = true;

                        } else {
//                            makeMove(blackTurn,tile, mBoard,position,gridview);
//                            blackTurn = true;
//                            mHandler.postDelayed(new Runnable() {
//                                public void run() {
//
//                                    Board helperBoard = new Board();
//                                    helperBoard = mBoard;
//                                    computerMove(false,helperBoard,gridview);
//                                    //randomMove(false,helperBoard,gridview);
//                                }
//                            }, 1500);

                        }

                        for (Pair moves: listOfMovesMade){
                            //re-adds the moves from the original state of the board
                            mBoard.addToBoard(moves.getX(), moves.getY());
                        }

                        whiteScore.setText(score.getY() + "");
                        blackScore.setText(score.getX() + "");

                        do {
                            mHandler.postDelayed(new Runnable() {
                                public void run() {

                                    Board helperBoard = new Board();
                                    helperBoard = mBoard;
                                    computerMove(false,helperBoard,gridview);
                                    //randomMove(false,helperBoard,gridview);
                                }
                            }, 500);
                        } while (!opponentHasMove(mBoard, black));

                    }else{Toast.makeText(ReversiActivity.this, "Move at - " + position + "is Unavailable. Try Again",  Toast.LENGTH_SHORT).show();}
                } else {
                    Toast.makeText(ReversiActivity.this, "Move at - " + position + "is Unavailable. Try Again",  Toast.LENGTH_SHORT).show();
                }




                whiteScore.setText(score.getY() + "");
                blackScore.setText(score.getX() + "");

                if (!blackTurn){
                    turn.setText("White Chip it's your turn!");
                } else {
                    turn.setText("Black Chip it's your turn!");
                }

                if (mBoard.isBoardFull()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReversiActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("The game is Over!");
                    if (score.getX() > score.getY()){
                        builder.setMessage("Black Chip Player you win!");
                    } else if (score.getX() < score.getY()) {
                        builder.setMessage("White Chip Player you win!");
                    } else {
                        builder.setMessage("The game ended in a draw. Play Again?");
                    }

                    builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ReversiActivity.super.recreate();
                        }
                    });



                }

            }
        });


        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReversiActivity.this, "You are about to quit the game!",  Toast.LENGTH_SHORT).show();
            }
        });

        passButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!opponentHasMove(mBoard, black)){
                    computerMove(false,mBoard, gridview);
                } else {
                    Toast.makeText(ReversiActivity.this, "You have moves available!",  Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean opponentHasMove(Board mBoard, int player) {
        if(filterIllegalMoves(mBoard, player).isEmpty()){

            //createAlert();
            Toast.makeText(ReversiActivity.this, "You have m=no available moves! Tit you opponents turn!",  Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }

    private void createAlert() {

        AlertDialog alertDialog = new AlertDialog.Builder(ReversiActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Sorry, you have no available move. You'll have to pass!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }




    //checking the move is legal =============================================================================
    private boolean checkLegalMove(Board board, int position, int chiptype) {

        if (checkVertical(board,position,chiptype).isEmpty()
                && checkHorizontal(board,position,chiptype).isEmpty()
                    && checkDiagonally(board,position,chiptype).isEmpty()){
            return false;
        }
        return true;
    }

    private boolean checkAvailableTile(Board board,int position) {
        //Pair pair = convertPositionToCoordinate(position);
        Pair pair = board.convertToCoordinate(position);
        int help = board.getTileAt(pair.getX(),pair.getY());

        if(help == empty) {
            return true;
        }
        return false;

    }

    private void makeMove(boolean blackTurn, ImageView tile, Board board, int position, GridView gridview){
        //blackTurn = true;

        if (!blackTurn){

            tile.setImageResource(R.drawable.whitechip);
            board.addToBoard(position,white);
            listOfMovesMade.add(new Pair(position,white) );

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
                    listOfMovesMade.add(new Pair(changeAt,white));
                }
            }
            score = board.getNewScores();
        } else {
            tile.setImageResource(R.drawable.blackchip);
            board.addToBoard(position,black);
            listOfMovesMade.add(new Pair(position,black));


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
                    listOfMovesMade.add(new Pair(changeAt,black));
                }
            }
            score = board.getNewScores();

        }
    }



    //checking move is available vertically and horizontally on mBoard=========================================
    private static ArrayList<Integer> checkHorizontal(Board board, int position, int chipType) {

        ArrayList<Integer> listOfPositionsToChange = new ArrayList<Integer>();
        Pair pair = convertPositionToCoordinate(position);
        int initialYPos = pair.getY();
        int initialXPos = pair.getX();

        int helper = position;


            if (position < board.getBoardSize()) {
                for (int i = initialXPos + 1; i < board.getBoardWidth(); i++) {

                    if(i <= board.getBoardWidth()){
                        if (board.checkIsLegalOnBoard(i, initialYPos)) {
                            //if (!mBoard.checkBoardEdge(initialXPos,i)) {
                            if (board.getTileAt(i, initialYPos) == empty) {
                                if (listOfPositionsToChange.isEmpty()) {
                                    break;
                                } else {
                                    listOfPositionsToChange.clear();
                                    break;
                                }                                                    //new code to check that the next tile is actually legal (solves edge cases issue)
                            } else if (board.getTileAt(i, initialYPos) == chipType) {
                                if (listOfPositionsToChange.isEmpty()) {
                                    break;
                                } else {
                                    return listOfPositionsToChange;
                                }
                            } else {
                                helper++;
                                if (helper < board.getBoardSize()) {
                                    listOfPositionsToChange.add(helper);
                                }

                            }

                            if (!board.checkIsLegalOnBoard((i+1), (initialYPos))){
                                if (listOfPositionsToChange.isEmpty()) {
                                    break;
                                } else {
                                    listOfPositionsToChange.clear();
                                    break;
                                }
                            }
                        }
                    } else {
                        listOfPositionsToChange.clear();
                        break;
                    }


                }



            helper = position;
            if (position < board.getBoardSize()) {
                for (int j = initialXPos - 1; j >= 0; j--) {
                    if(j > 0) {
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

                            if (!board.checkIsLegalOnBoard((j-1), (initialYPos))){
                                if (listOfPositionsToChange.isEmpty()) {
                                    break;
                                } else {
                                    listOfPositionsToChange.clear();
                                    break;
                                }
                            }

                        }
                    } else {
                        listOfPositionsToChange.clear();
                        break;
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

                    if (!board.checkIsLegalOnBoard((initialXPos), (i+1))){
                        if (listOfPositionsToChange.isEmpty()) {
                            break;
                        } else {
                            listOfPositionsToChange.clear();
                            break;
                        }
                    }
                }

            }

           

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

                        if (!board.checkIsLegalOnBoard((initialXPos), (j-1))){
                            if (listOfPositionsToChange.isEmpty()) {
                                break;
                            } else {
                                listOfPositionsToChange.clear();
                                break;
                            }
                        }

                    }
                }
            }
        }
        return listOfPositionsToChange;
    }




    //returns an array list<Integer> of all the coordinates that require change due to a move
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

        if (initialX >= 0 && initialY >= 0){
            do {
                if(board.getTileAt(initialX, initialY) == empty){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        listOfCoordinatesToChange.clear();
                        break;
                    }

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

                if(!board.checkIsLegalOnBoard((initialX), (initialY))){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        listOfCoordinatesToChange.clear();
                        break;
                    }
                }

            } while (initialX >= 0 && initialY >= 0);
        } //else {
        return listOfCoordinatesToChange;
    }

    private static ArrayList<Integer> checkDownLeft(Board board, int position, int chipType) {

        Pair pair = convertPositionToCoordinate(position);
        int initialX = pair.getX()-1;
        int initialY = pair.getY()+1;
        ArrayList<Integer> listOfCoordinatesToChange = new ArrayList<Integer>();
        int helper = position;

        if (initialX >= 0 && initialY < board.getBoardWidth()){
            do {
                if(board.getTileAt(initialX, initialY) == empty){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        listOfCoordinatesToChange.clear();
                        break;
                    }
                    //(mBoard.getTileAt(initialX, initialY) == empty)
                } else if (board.getTileAt(initialX, initialY) == chipType){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        return listOfCoordinatesToChange;
                    }
                } else {
                    helper += 7;
                    if (helper < board.getBoardSize()) {
                        listOfCoordinatesToChange.add(helper);
                    }
                }
                initialY++;
                initialX--;

                if(!board.checkIsLegalOnBoard((initialX), (initialY))){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        listOfCoordinatesToChange.clear();
                        break;
                    }
                }

            } while (initialX >= 0 && initialY < board.getBoardWidth());
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

                } else if (board.getTileAt(initialX, initialY) == chipType){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        return listOfCoordinatesToChange;
                    }
                } else {
                    helper += 9;
                    if (helper < board.getBoardSize()) {
                        listOfCoordinatesToChange.add(helper);
                    }
                }
                initialY++;
                initialX++;

                if(!board.checkIsLegalOnBoard((initialX), (initialY))){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        listOfCoordinatesToChange.clear();
                        break;
                    }
                }

            } while (initialX < board.getBoardWidth() && initialY < board.getBoardWidth());
        }
        return listOfCoordinatesToChange;
    }

    private static ArrayList<Integer> checkUpRight(Board board, int position, int chipType){

        Pair pair = convertPositionToCoordinate(position);
        int initialX = pair.getX()+1;
        int initialY = pair.getY()-1;
        ArrayList<Integer> listOfCoordinatesToChange = new ArrayList<Integer>();
        int helper = position;

        if (initialX < board.getBoardWidth() && initialY >= 0){
            do {
                if(board.getTileAt(initialX, initialY) == empty){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        listOfCoordinatesToChange.clear();
                        break;
                    }

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

                if(!board.checkIsLegalOnBoard((initialX), (initialY))){
                    if(listOfCoordinatesToChange.isEmpty()){
                        break;
                    } else {
                        listOfCoordinatesToChange.clear();
                        break;
                    }
                }

            } while (initialX < board.getBoardWidth() && initialY >= 0);
        } //else {
        return listOfCoordinatesToChange;
    }

    private static Pair convertPositionToCoordinate(int position) {
        return  new Pair((position%8), (position/8));
    }




    //performs a random move =======================================================================
    private int generateRandomPosition(Board board, int chip){
        Random rand = new Random();
        ArrayList<Integer> randomPostions = new ArrayList<>();
        int randomPos;

        for (int i = 0; i < board.getBoardSize(); i++){
            randomPos = rand.nextInt(63) + 1;
            if(checkLegalMove(board, randomPos, chip) && checkAvailableTile(board, randomPos)){
                randomPostions.add(randomPos);
            }
        }

        return randomPostions.get(0);
    }

    private void randomMove(boolean blackTurn, Board board, GridView gridview){


        int chosenMove = generateRandomPosition(board, white);
        ImageView newTile = (ImageView) gridview.getChildAt(chosenMove);

        if (!blackTurn){
            newTile.setImageResource(R.drawable.whitechip);
            board.addToBoard(chosenMove,white);

            //Section is to update the Board!
            ArrayList<Integer> listOfCoordinatesToChange = new ArrayList<>();
            if(checkLegalMove( board,chosenMove, white)){

                listOfCoordinatesToChange.addAll(checkDiagonally(board, chosenMove, white));
                listOfCoordinatesToChange.addAll(checkHorizontal(board, chosenMove, white));
                listOfCoordinatesToChange.addAll(checkVertical(board, chosenMove, white));

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
        }
    }



    //==============================================================================================
    //minimax section

    private void computerMove(boolean blackTurn, Board passThisBoardToMinimax, GridView gridview){

         //callsToMinimax++;
         Board updatedBoard = new Board();

         //Using Minimax Algorithm
          //Pair chosenMoveAndValue = minimax(passThisBoardToMinimax, !blackTurn, 3);

         //Using Alpha-Beta Pruning                                                  alpha            beta
         Pair chosenMoveAndValue = alphaBeta(passThisBoardToMinimax, !blackTurn, -100000, 100000, 3);

         for (Pair moves: listOfMovesMade){
             //re-adds the moves from the original state of the board
             updatedBoard.addToBoard(moves.getX(), moves.getY());
         }

         mBoard = updatedBoard;

         int chosenMove = chosenMoveAndValue.getX();


        if (!blackTurn){
            ImageView newTile = (ImageView) gridview.getChildAt(chosenMove);

            newTile.setImageResource(R.drawable.whitechip);
             mBoard.addToBoard(chosenMove,white);
            listOfMovesMade.add(new Pair(chosenMove,white));

             //Section is to update the Board!
             ArrayList<Integer> listOfCoordinatesToChange = new ArrayList<>();
             if(checkLegalMove(mBoard,chosenMove, white)){

                 listOfCoordinatesToChange.addAll(checkDiagonally(mBoard, chosenMove, white));
                 listOfCoordinatesToChange.addAll(checkHorizontal(mBoard, chosenMove, white));
                 listOfCoordinatesToChange.addAll(checkVertical(mBoard, chosenMove, white));

                 ImageView imageView;
                 for(Integer changeAt : listOfCoordinatesToChange) {
                     //tile = (ImageView) gridview.getAdapter().getView(changeAt, v, parent);
                     //tile.setImageDrawable();
                     imageView = (ImageView) gridview.getChildAt(changeAt);
                     imageView.setImageResource(R.drawable.whitechip);
                     mBoard.addToBoard(changeAt,white);
                     listOfMovesMade.add(new Pair(changeAt,white));
                 }
             }
             score = mBoard.getNewScores();
         }
         callsToAlphaBeta = 0;
     }

    private Pair minimax(Board board, boolean maxPlayer, int depth ){

         //Pair bestCombo = new Pair(0,0);
        callsToMinimax++;
        int bestValue;
        int bestMove;

         if (board.isBoardFull()){
             //return board.evaluate();
             return board.terminalEvaluation();

         }

         if (depth == 0){
             return board.evaluate();
         }

         if (maxPlayer){ // the ai player
             bestValue = -100000;
             bestMove = 0;

             //ArrayList<Pair> availableSpotsAndValue =mBoard.getAvailablePositionsAndValue();
             ArrayList<Integer> newlist = filterIllegalMoves(board, white);

             Board newBoard = board;

             if (!newlist.isEmpty()){
                 for (int scoredMove: newlist){

                     newBoard.addToBoard(scoredMove, white);
                     newBoard = makeChangesToBoard(newBoard,scoredMove, white);

                     Pair value = minimax(newBoard, false, depth-1);
                     if (value.getY() > bestValue){
                         bestValue = value.getY();
                         bestMove = scoredMove;
                     }
                 }
             } else {

                 Pair value = minimax(newBoard, true , depth-1);
                 if (value.getY() < bestValue){
                     bestValue = value.getY();
                     bestMove = value.getX();
                 }
             }

             return new Pair(bestMove,bestValue);

         } else { // the human player

             bestValue = 100000;
             bestMove = 0;

             ArrayList<Integer> newlist = filterIllegalMoves(board, black);

             Board newBoard = board;

             if (!newlist.isEmpty()){
                 for (int scoredMove: newlist){

                     newBoard.addToBoard(scoredMove, black);
                     newBoard = makeChangesToBoard(newBoard,scoredMove, black);

                     Pair value = minimax(newBoard, true , depth-1);
                     if (value.getY() < bestValue){
                         bestValue = value.getY();
                         bestMove = scoredMove;
                     }
                 }
             } else {

                     Pair value = minimax(newBoard, false , depth-1);
                     if (value.getY() < bestValue){
                         bestValue = value.getY();
                         bestMove = value.getX();
                     }
             }

             return new Pair(bestMove,bestValue);

         }

     }

    private ArrayList<Integer> filterIllegalMoves(Board board,int player)  {
         ArrayList<Integer> newList = new ArrayList<>();

         for (int i = 0; i < board.getBoardSize(); i++) {
             if(checkLegalMove(board, i, player) && checkAvailableTile(board, i)){
                 newList.add(i);
             }
         }
         return newList;

    }

    private Board makeChangesToBoard(Board mBoard, int move, int chip){
        //Section is to update the Board!
        ArrayList<Integer> listOfCoordinatesToChange = new ArrayList<>();
        if(checkLegalMove( mBoard,move, chip)){

            listOfCoordinatesToChange.addAll(checkDiagonally(mBoard, move, chip));
            listOfCoordinatesToChange.addAll(checkHorizontal(mBoard, move, chip));
            listOfCoordinatesToChange.addAll(checkVertical(mBoard, move, chip));

            for(Integer changeAt : listOfCoordinatesToChange) {
                mBoard.addToBoard(changeAt,chip);
            }
        }

        return mBoard;
    }




    //Alpha-Beta Pruning section ==================================================================
    private Pair alphaBeta(Board board, boolean maxPlayer, int alpha, int beta, int depth){


        callsToAlphaBeta++;
        int bestValue;
        int bestMove;

        ArrayList<Pair> movesPlayed = new ArrayList<>();

        for (int i = 0; i < board.getBoardSize(); i++ ){
            if (board.getTileAtGivenPosition(i) == white){
                movesPlayed.add(new Pair(i, white));
            } else if (board.getTileAtGivenPosition(i) == black){
                movesPlayed.add(new Pair(i, black));
            }
        }


        if (depth == 0){
            return board.evaluate();
        }


        if (board.isBoardFull()){
            return board.terminalEvaluation();

        }

//
//        if(board.noMoreChildren()){
//        }



        if (maxPlayer){ // the ai player
            bestValue = -100000;
            bestMove = 0;


            ArrayList<Integer> newlist = filterIllegalMoves(board, white);
            Board newBoard = board;

            if (!newlist.isEmpty()) {

                for (int scoredMove: newlist){
//                Board newBoard = new Board();
//
//                for (Pair pastMove : movesPlayed){
//                    newBoard.addToBoard(pastMove.getX(), pastMove.getY());
//

                    newBoard.addToBoard(scoredMove, white);
                    //movesPlayed.add(new Pair(scoredMove, white));

                    newBoard = makeChangesToBoard(newBoard,scoredMove, white);

                    Pair value = alphaBeta(newBoard, false, alpha, beta , depth-1);

                    if (value.getY() > bestValue){
                        bestMove = scoredMove;
                    }
                    bestValue = returnMax(bestValue, value.getY());
                    alpha = returnMax(bestValue, alpha);

                    if (beta <= alpha){
                        break;
                    }

                }
            } else {

                //forces to pass to min player
                Pair value = alphaBeta(newBoard, true, alpha, beta , depth-1);

                if (value.getY() < bestValue){
                    bestMove = value.getX();
                }
                bestValue = returnMax(bestValue, value.getY());

            }

            return new Pair(bestMove,bestValue);

        } else { // the human player/minimizing player

            bestValue = 100000;
            bestMove = 0;

            ArrayList<Integer> newlist = filterIllegalMoves(board, black);
            Board newBoard = board;

            if (!newlist.isEmpty()){
                for (int scoredMove: newlist){

                    newBoard.addToBoard(scoredMove, black);
                    newBoard = makeChangesToBoard(newBoard,scoredMove, black);

                    Pair value = alphaBeta(newBoard, true, alpha, beta, depth-1);

                    if (value.getY() < bestValue){
                        bestMove = scoredMove;
                    }

                    bestValue = returnMin(value.getY(), bestValue);
                    beta = returnMin(bestValue, beta);
                    //bestMove =

                    if (beta <= alpha){
                        break;
                    }
                }
            } else {

                //forced to pass
                Pair value = alphaBeta(newBoard, false, alpha, beta, depth-1);

                if (value.getY() < bestValue){
                    bestMove = value.getX();
                }
                bestValue = returnMin(value.getY(), bestValue);

            }
            return new Pair(bestMove,bestValue);

        }

    }

    private int returnMin(int num1, int num2) {
        if (num1 < num2){
            return num1;
        } else {
            return num2;
        }
    }

    private int returnMax(int num1, int num2) {
        if (num1 > num2){
            return num1;
        } else {
            return num2;
        }
    }


}
