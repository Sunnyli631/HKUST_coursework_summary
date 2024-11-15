#include "hashtable.h"

BoardOptimalMove BoardHashTable::getHashedMove(const unsigned long long id, const int depth){
    //std::cout<<"hi";
    int index=id%TABLE_SIZE;
    /*if(table[index]==nullptr){
        return BoardOptimalMove();
    }*/
    BoardHashNode* temp=table[index];
    while(temp!=nullptr){
        //std::cout<<"hi";
        if(temp->id!=id){
            if(temp->next==nullptr){
                return BoardOptimalMove();
            }
        }else if(temp->id==id){
            //std::cout<<"hi";
            if(temp->depth<depth){
                return BoardOptimalMove();
            }
            return temp->optimalMove;
        }
        temp=temp->next;
    }
    //std::cout<<"hi2";
    return BoardOptimalMove();
}

void BoardHashTable::updateTable(const unsigned long long id, const int depth, const BoardOptimalMove& optimalMove){
    int index=id%TABLE_SIZE;
    BoardHashNode* temp=table[index];
    BoardHashNode* temp2=table[index];
    while(temp!=nullptr){
        if(temp->id==id){
            if(temp->depth<depth){
                temp->optimalMove=optimalMove;
                temp->depth=depth;
                //std::cout<<"hi";
            }
            //std::cout<<"hi";
            return;
        }
        temp2=temp;
        temp=temp->next;
    }

    if(temp2==nullptr){
        table[index]=new BoardHashNode(id,depth,optimalMove);
    }else{
        temp2->next= new BoardHashNode(id,depth,optimalMove);
    }
    //std::cout<<"Hi";
}

void BoardHashTable::clearTable(){
    for(int i=0;i<TABLE_SIZE;++i){
        while(table[i]!=nullptr){
            BoardHashNode* temp=table[i];
            table[i]=table[i]->next;
            if(temp!=nullptr){
                delete temp;
                temp=nullptr;
            }
        }
        table[i]=nullptr;
    }
}