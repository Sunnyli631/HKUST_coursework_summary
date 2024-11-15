#include <iostream>
#include "player.h"
#include <cstring>

Player::Player(const char* const name, const int elo){
    score=0;this->elo=elo;
    this->name= new char[strlen(name)+1];
    strcpy(this->name,name);
}

Player::~Player(){
    if(name!=nullptr){
        delete []name;
    }
    name =nullptr;
}

PlayerList::PlayerList(){
    players=nullptr;numPlayers=0;
}

PlayerList::PlayerList(const PlayerList& list){
    this->numPlayers=list.numPlayers;
    this->players=new Player*[list.numPlayers];
    for(int i=0;i<list.getNumPlayers();i++){
        this->players[i]=list.players[i];
    }
}

PlayerList::~PlayerList(){
    if(players!=nullptr){
        delete []players;
    }
    players=nullptr;
}

void PlayerList::addPlayer(Player* const player){
    
    Player** temp=new Player*[numPlayers+1];
    for(int i=0;i<numPlayers;i++){
        temp[i]=this->players[i];
    }
    temp[numPlayers]=player;
    numPlayers++;
    delete []players;
    players=temp;temp=nullptr;
}

void PlayerList::sort(){
    int i,j;
    
    for(i=0;i<numPlayers-1;i++){
        for(j=0;j<numPlayers-1-i;j++){
            if(players[j]->getScore()<players[j+1]->getScore()){
                Player* temp=nullptr;
                temp=players[j];
                players[j]=players[j+1];
                players[j+1]=temp;
                temp=nullptr;
            }
            if(players[j]->getScore()==players[j+1]->getScore()){
                if(players[j]->getELO()<players[j+1]->getELO()){
                    Player* temp=nullptr;
                    temp=players[j];
                    players[j]=players[j+1];
                    players[j+1]=temp;
                    temp=nullptr;
                }
            }
        }
    }

}

PlayerList* PlayerList::splice(const int startIndex, const int endIndex) const{
    PlayerList* temp=new PlayerList();
    if(startIndex<0||endIndex>numPlayers){
        return temp;
    }
    if(startIndex>=endIndex){return temp;}
    //int j=0;
    for(int i=startIndex;i<endIndex;i++){
        temp->addPlayer(players[i]);
        //j++;
    }
    return temp;
}