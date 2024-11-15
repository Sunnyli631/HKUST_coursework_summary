#include <iostream>
#include "swiss.h"
#include "match.h"

Swiss::Swiss(const int numRounds, const PlayerList& list){
    this->numRounds=numRounds;
    curRound=0;
    for(int i=0;i<list.getNumPlayers();i++){
        this->list.addPlayer(list.getPlayer(i));
    }
    
    this->list.sort();
}


Swiss::~Swiss(){ }

void Swiss::play(){
    for(int i=0;i<numRounds;i++){
        curRound=curRound+1;
        PlayerList* temp=new PlayerList[2*curRound-1];
        int count=0;
        for(int j=2*curRound-2;j>=0;j--){
            for(int k=0;k<list.getNumPlayers();k++){
                if(list.getPlayer(k)->getScore()==j){
                    temp[count].addPlayer(list.getPlayer(k));
                }
            }
            count++;
        }
        for(int z=0;z<2*curRound-1;z++){
            if(temp[z].getNumPlayers()>0){
                temp[z].sort();
                int a=temp[z].getNumPlayers()/2;
                for(int i=0;i<a;i++){
                    Match match{temp[z].getPlayer(i),temp[z].getPlayer(i+a)};
                    match.play();
                }
                if(temp[z].getNumPlayers()%2==1){
                    if(z==2*curRound-2){
                        temp[z].getPlayer(temp[z].getNumPlayers()-1)->addScore(2);
                        break;
                    }
                    temp[z+1].addPlayer(temp[z].getPlayer(temp[z].getNumPlayers()-1));
                }
            }
            
        }
        list.sort();
        printLeaderboard();
        
        delete []temp;
        temp=nullptr;
        
    }
    
}