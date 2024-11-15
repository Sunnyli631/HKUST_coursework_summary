#include "board.h"

Board::Board(const int score[][BOARD_SIZE]){
    curPlayer=X; id=0;
    for(int i=0;i<BOARD_SIZE;++i){
        for(int j=0;j<BOARD_SIZE;++j){
            this->score[i][j]=score[i][j];
            cells[i][j]=EMPTY;
        }
    }
    
}

bool Board::isFull() const{
    for(int i=0;i<BOARD_SIZE;++i){
        for(int j=0;j<BOARD_SIZE;++j){
            if(cells[i][j]==EMPTY){
                return false;
            }
        }
    }
    return true;
}

bool Board::isFinished() const{
    if(isFull()){
        return true;
    }

    for(int k=0;k<BOARD_SIZE;++k){
        int count=0;
        for(int j=0;j<BOARD_SIZE-1;++j){
                //Cell temp=1;
            if(cells[j][k]==EMPTY){break;}
            if(cells[j][k]!=cells[j+1][k]){
                break;
            }
            count++;
            if(count==BOARD_SIZE-1){
                return true;
            }
        }
            
    }
    int count3=0;
    for(int k=0;k<BOARD_SIZE-1;++k){
    //Cell temp=1;
        if(cells[k][BOARD_SIZE-1-k]==EMPTY){break;}
        if(cells[k][BOARD_SIZE-1-k]!=cells[k+1][BOARD_SIZE-1-k-1]){
            break;
        }
        count3++;
        if(count3==BOARD_SIZE-1){
            return true;
        }
            
    }
    
    for(int k=0;k<BOARD_SIZE;++k){
            int count=0;
            for(int j=0;j<BOARD_SIZE-1;++j){
                //Cell temp=1;
                if(cells[k][j]==EMPTY){break;}
                if(cells[k][j]!=cells[k][j+1]){
                    break;
                }
                count++;
                if(count==BOARD_SIZE-1){
                    return true;
                }
            }
            
    }
    int count_2=0;
    for(int k=0;k<BOARD_SIZE-1;++k){
                //Cell temp=1;
        if(cells[k][k]==EMPTY){break;}
        if(cells[k][k]!=cells[k+1][k+1]){
            break;
        }
        count_2++;
        if(count_2==BOARD_SIZE-1){
            return true;
        }
            
    }
    return false;
}

int Board::getBoardScore() const{
    
    // for(int j=0;j<BOARD_SIZE;++j){
    //         for(int k=0;k<BOARD_SIZE;++k){
    //             if(cells[j][k]!=X){
    //                 break;
    //             }
    //             if(k==BOARD_SIZE-1){return WIN_SCORE;}
    //         }
    //     }
    
    // for(int j=0;j<BOARD_SIZE;++j){
    //         for(int k=0;k<BOARD_SIZE;++k){
    //             if(cells[j][k]!=O){
    //                 break;
    //             }
    //             if(k==BOARD_SIZE-1){return -WIN_SCORE;}
    //         }
    //     }
    
    //     for(int j=0;j<BOARD_SIZE;++j){
    //         for(int k=0;k<BOARD_SIZE;++k){
    //             if(cells[k][j]!=X){
    //                 break;
    //             }
    //             if(k==BOARD_SIZE-1){return WIN_SCORE;}
    //         }
    //     }
    
    // for(int j=0;j<BOARD_SIZE;++j){
    //         for(int k=0;k<BOARD_SIZE;++k){
    //             if(cells[k][j]!=O){
    //                 break;
    //             }
    //             if(k==BOARD_SIZE-1){return -WIN_SCORE;}
    //         }
    //     }
    
    // for(int k=0;k<BOARD_SIZE;++k){
    //     if(cells[k][k]!=X){
    //         break;
    //     }
    //     if(k==BOARD_SIZE-1){return WIN_SCORE;}
    // }
    // for(int k=0;k<BOARD_SIZE;++k){
    //         if(cells[k][k]!=O){
    //             break;
    //         }
    //         if(k==BOARD_SIZE-1){return -WIN_SCORE;}
    //     }
    
    //     for(int k=0;k<BOARD_SIZE;++k){
    //         if(cells[k][BOARD_SIZE-1-k]!=O){
    //             break;
    //         }
    //         if(k==BOARD_SIZE-1){return -WIN_SCORE;}
    //     }
    // for(int k=0;k<BOARD_SIZE;++k){
    //         if(cells[k][BOARD_SIZE-1-k]!=X){
    //             break;
    //         }
    //         if(k==BOARD_SIZE-1){return WIN_SCORE;}
    //     } 
    for(int k=0;k<BOARD_SIZE;++k){
            int count=0;
            for(int j=0;j<BOARD_SIZE-1;++j){
                //Cell temp=1;
                if(cells[j][k]!=cells[j+1][k]){
                    break;
                }
                count++;
                if(count==BOARD_SIZE-1){
                    if(cells[j][k]==X){
                        return WIN_SCORE;
                    }else if(cells[j][k]==O){
                        return -WIN_SCORE;
                    }
                }
            }
            
        }
    int count=0;
    for(int k=0;k<BOARD_SIZE-1;++k){
            
            
                //Cell temp=1;
                if(cells[k][BOARD_SIZE-1-k]!=cells[k+1][BOARD_SIZE-1-k-1]){
                    break;
                }
                count++;
                if(count==BOARD_SIZE-1){
                    if(cells[k][BOARD_SIZE-1-k]==X){
                        return WIN_SCORE;
                    }else if(cells[k][BOARD_SIZE-1-k]==O){
                        return -WIN_SCORE;
                    }
                }
            
        }
    
    for(int k=0;k<BOARD_SIZE;++k){
            int count=0;
            for(int j=0;j<BOARD_SIZE-1;++j){
                //Cell temp=1;
                if(cells[k][j]!=cells[k][j+1]){
                    break;
                }
                count++;
                if(count==BOARD_SIZE-1){
                    if(cells[k][j]==X){
                        return WIN_SCORE;
                    }else if(cells[k][j]==O){
                        return -WIN_SCORE;
                    }
                }
            }
            
        }
    int count_2=0;
    for(int k=0;k<BOARD_SIZE-1;++k){
            
            
                //Cell temp=1;
                if(cells[k][k]!=cells[k+1][k+1]){
                    break;
                }
                count_2++;
                if(count_2==BOARD_SIZE-1){
                    if(cells[k][k]==X){
                        return WIN_SCORE;
                    }else if(cells[k][k]==O){
                        return -WIN_SCORE;
                    }
                }
            
        }
        
    int temp=0;
    for(int i=0;i<BOARD_SIZE;++i){
        for(int j=0;j<BOARD_SIZE;++j){
            if(cells[i][j]==X){
                temp+=score[i][j]*getCellWeight(cells[i][j]);
            }
            if(cells[i][j]==O){
                temp-=score[i][j]*getCellWeight(cells[i][j]);
            }
        }
    }
    return temp;
}

bool Board::play(const BoardCoordinate& coords){
    if(!(coords.isValid())||cells[coords.row][coords.col]!=EMPTY){
        return false;
    }
    cells[coords.row][coords.col]=curPlayer;
    int temp=pow(3,coords.row * BOARD_SIZE + coords.col);
    
    if(curPlayer==X){
        id+=1 * temp;
    }else{
        id+=2 * temp;
    }
    
    if(curPlayer==X){curPlayer=O;}else{curPlayer=X;}
    
    return true;
}