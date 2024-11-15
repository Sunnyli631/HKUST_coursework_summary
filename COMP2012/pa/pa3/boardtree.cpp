#include "boardtree.h"

//#include <iostream>
BoardTree::BoardTree(const Board& board){
    root=new BoardNode(board);
    /*for(int i=0;i<BOARD_SIZE;++i){
        for(int j=0;j<BOARD_SIZE;++j){
            root->subTree[i][j].root=nullptr;
        }
    }*/
}

BoardTree::~BoardTree(){
    if(root){
        delete root;
    }
}

BoardTree* BoardTree::getSubTree(const BoardCoordinate& coords){
    if(root->subTree[coords.row][coords.col].isEmpty()){
        Board temp=root->board;
        if(temp.play(coords)){
            root->subTree[coords.row][coords.col].root= new BoardNode(temp);
            //BoardTree* temp_1=&(root->subTree[coords.row][coords.col]);
            //return &(root->subTree[coords.row][coords.col]);
        }//else{
            BoardTree* temp_1=&(root->subTree[coords.row][coords.col]);
            return temp_1;
        //}
        
    }//else{
        //BoardTree* temp_2=&(root->subTree[coords.row][coords.col]);
        //return &(root->subTree[coords.row][coords.col]);
    BoardTree* temp_1=&(root->subTree[coords.row][coords.col]);
    return temp_1;    
    //}
}

BoardOptimalMove BoardTree::getOptimalMove(const unsigned int depth){
if (isEmpty()) {
    return BoardOptimalMove(); // Returns a dummy illegal move
}

if (depth == 0 || root->board.isFinished()) {
  // If depth is 0 or if the game has already finished, we cannot search further
  // return the score of this board with any move coordinate since we will not use it
  return BoardOptimalMove(root->board.getBoardScore(), BoardCoordinate(1,0));
}
// BoardOptimalMove t=BoardHashTable::getInstance().getHashedMove(root->board.getID(), depth);
// if (t.score!=ILLEGAL) {
//   return t;
// }
// Else, we find the estimated score and optimal move of this node by calculating the score of each children node
// Player X is trying to maximize the score, so the estimated score is the maximum of children scores
// Vice versa, player O is trying to minimize the score
int estimatedScore = 100000000;
if(root->board.getCurPlayer()==X){
    estimatedScore= -100000000;
}
//int estimatedScore = -∞ if current player is X, else +∞;
BoardOptimalMove bestMove;
for(int i=0;i<BOARD_SIZE;++i){
    for (int j=0;j<BOARD_SIZE;++j) {
    if(root->subTree[i][j].isEmpty()){
        Board temp=root->board;
        if(temp.play(BoardCoordinate(i,j))){
            root->subTree[i][j].root = new BoardNode(temp);
        }else{
            continue;
        }
    }

    BoardOptimalMove childMove = root->subTree[i][j].getOptimalMove(depth - 1);

    if (childMove.score == ILLEGAL) {
        // If the move is illegal, the subtree corresponds to an invalid move/board, simply skip to the next subtree
        continue;
    }

    if (root->board.getCurPlayer()==X) {
        if(childMove.score>estimatedScore){
            estimatedScore = childMove.score;
            bestMove = BoardOptimalMove(estimatedScore, BoardCoordinate(i,j));
            }
    }else{
        if(childMove.score<estimatedScore){
            estimatedScore = childMove.score;
            bestMove = BoardOptimalMove(estimatedScore, BoardCoordinate(i,j));
            }
        }
    }
}
    //BoardHashTable::getInstance().updateTable(root->board.getID(), depth, bestMove); 
    return bestMove;

}

BoardOptimalMove BoardTree::getOptimalMoveAlphaBeta(const unsigned int depth, int alpha, int beta){
if (isEmpty()) {
    return BoardOptimalMove(); // Returns a dummy illegal move
}

if (depth == 0 || root->board.isFinished()) {
  // If depth is 0 or if the game has already finished, we cannot search further
  // return the score of this board with any move coordinate since we will not use it
  return BoardOptimalMove(root->board.getBoardScore(), BoardCoordinate(1,0));
}

BoardOptimalMove t=BoardHashTable::getInstance().getHashedMove(root->board.getID(), depth);
if (t.score!=ILLEGAL) {
  return t;
}

// Else, we find the estimated score and optimal move of this node by calculating the score of each children node
// Player X is trying to maximize the score, so the estimated score is the maximum of children scores
// Vice versa, player O is trying to minimize the score
int estimatedScore = 100000000;
Cell tem=root->board.getCurPlayer();
if(tem==X){
    estimatedScore= -100000000;
}
//int estimatedScore = -∞ if current player is X, else +∞;
BoardOptimalMove bestMove;
for(int i=0;i<BOARD_SIZE;++i){
    for (int j=0;j<BOARD_SIZE;++j) {
    if(root->subTree[i][j].isEmpty()){
        Board temp=root->board;
        if(temp.play(BoardCoordinate(i,j))){
            root->subTree[i][j].root = new BoardNode(temp);
        }else{
            continue;
        }
    }

    BoardOptimalMove childMove = root->subTree[i][j].getOptimalMoveAlphaBeta(depth - 1,alpha,beta);

    if (childMove.score == ILLEGAL) {
        // If the move is illegal, the subtree corresponds to an invalid move/board, simply skip to the next subtree
        continue;
    }

    if (root->board.getCurPlayer()==X) {
        if(childMove.score>estimatedScore){
            estimatedScore = childMove.score;
            bestMove = BoardOptimalMove(estimatedScore, BoardCoordinate(i,j));
            }
    }else{
        if(childMove.score<estimatedScore){
            estimatedScore = childMove.score;
            bestMove = BoardOptimalMove(estimatedScore, BoardCoordinate(i,j));
            }
        }
        /*root->board.getCurPlayer()*/
        // ADDED: Update alpha and beta according to newest child score found
        if(depth>2){
        if (tem== X) {
            if (estimatedScore > beta) {
            // Found a move that would be worse for O, so O will not consider this subtree
            break;
            }
            // Update the best move found by X
            if(estimatedScore>alpha){
                alpha = estimatedScore;
            }
        }
        else {
            // Same logic but for current player O
            if (estimatedScore < alpha) {
            break;
            }
            if(estimatedScore<beta){
                beta = estimatedScore;
            }
            
        }
        }
    }
}
    BoardHashTable::getInstance().updateTable(root->board.getID(), depth, bestMove); 
    return bestMove;
}