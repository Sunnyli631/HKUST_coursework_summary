#include "array.h"

DataMap::DataMap(){
    keys=nullptr;values=nullptr;
    arraySize=0;
}

DataMap::~DataMap(){
    clear();
}

void DataMap::insert(const string &key, const string &value){
    if(arraySize==0){
        arraySize=1;
        keys= new string[1];
        keys[0]=key;
        values= new string[1];
        values[0]=value;
        return;
    }
    arraySize++;
    string* tempk= new string[arraySize];
    string* tempv= new string[arraySize];
    for(int i=0;i<arraySize-1;i++){
        tempk[i]=keys[i];
        tempv[i]=values[i];
    }
    tempk[arraySize-1]=key;
    tempv[arraySize-1]=value;
    delete []keys; delete []values;
    keys=tempk; values=tempv;
    return;
}
string DataMap::find(const string &key) const{
    for(int i=0;i<arraySize;i++){
        if(key==keys[i]){
            return values[i];
        }
    }
    return "";
}
void DataMap::clear(){
    if(arraySize!=0){
        arraySize=0;
        if(keys!=nullptr){
            delete []keys;
            keys=nullptr;
        }
        if(values!=nullptr){
            delete []values;
            values=nullptr;
        }
    }
}