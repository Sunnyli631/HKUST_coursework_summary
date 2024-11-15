/*BoardOptimalMove BoardTree::getOptimalMove(const unsigned int depth){
    if (isEmpty()) {
        return BoardOptimalMove(); // Returns a dummy illegal move
    }
    //std::cout<<"hi";
    if (depth == 0 || root->board.isFinished()) {
  // If depth is 0 or if the game has already finished, we cannot search further
  // return the score of this board with any move coordinate since we will not use it
        return BoardOptimalMove(root->board.getBoardScore(), BoardCoordinate(0,0));
    }
    // Else, we find the estimated score and optimal move of this node by calculating the score of each children node
// Player X is trying to maximize the score, so the estimated score is the maximum of children scores
// Vice versa, player O is trying to minimize the score
    int estimatedScore = 100000000;
    if(root->board.getCurPlayer()==X){
        int estimatedScore = -100000000;
    }
    BoardOptimalMove bestMove;
    for (int i=0;i<BOARD_SIZE;++i) {
        for(int j=0;j<BOARD_SIZE;++j){
            //root->subTree[i][j].getSubTree(BoardCoordinate(i,j));
             if(root->subTree[i][j].isEmpty()){
                Board temp=root->board;
                BoardCoordinate tem=BoardCoordinate(i,j);
                if(temp.play(tem)){
                    root->subTree[i][j].root= new BoardNode(temp);
                }else{
                    root->subTree[i][j].root=nullptr;
                    continue;
                }
                //BoardTree* temp_1=&(root->subTree[i][j]);
                //root->subTree[i][j];
            }
            
            BoardOptimalMove childMove = root->subTree[i][j].getOptimalMove(depth - 1);

            if (childMove.score == ILLEGAL) {
            // If the move is illegal, the subtree corresponds to an invalid move/board, simply skip to the next subtree
                continue;
            }
            /* int z=0;int x=0;
            if((j+1)%BOARD_SIZE==0){z=0;x=i+1;}else{z=j+1;x=i;} 
            if(root->board.getCurPlayer()==X){
                if ((childMove.score > estimatedScore)&&(childMove.score!=ILLEGAL)) {
                    estimatedScore = childMove.score;
                    bestMove = BoardOptimalMove(estimatedScore, BoardCoordinate(childMove.coords.row,childMove.coords.col));
                    //std::cout<<"tick";
                }
            }else{
                if ((childMove.score < estimatedScore )&&(childMove.score!=ILLEGAL)) {
                    estimatedScore = childMove.score;
                    bestMove = BoardOptimalMove(estimatedScore, BoardCoordinate(childMove.coords.row,childMove.coords.col));
                    //std::cout<<"tick";
                }
            }
        }
    }
    return bestMove;

}*/