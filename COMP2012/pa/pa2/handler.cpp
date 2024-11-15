#include "handler.h"

HandlerModule::~HandlerModule(){
    data.clear();
    languageFilter = nullptr;
    toneFilter = nullptr;
    //languageFilter->~FilterModule(); toneFilter->~FilterModule();
}
void HandlerModule::print() const{
    std::cout << "Module name = " << m_name << ", this is a Handler, can handle topic = " << topic << std::endl;
}

void HandlerModule::trainData(const string &question, const string &answer){
    data.insert(question,answer);
}
string HandlerModule::inneranswer(const string &question) const{
    string temp="Sorry, I don't know";
    string temp_data=data.find(question);
    if(temp_data!=""){
        temp=temp_data;
    }
    return temp;
}
string HandlerModule::answer(const string &question) const{
    string temp=question;
    if(languageFilter!=nullptr){
        string temp_1=languageFilter->translatePreData(question);
        if(temp_1!=question){
            temp=temp_1;
        }
    }
    if(toneFilter!=nullptr){
        string temp_2=toneFilter->translatePreData(temp);
        if(temp_2!=question){
            temp=temp_2;
        }
    }
    string ans=inneranswer(temp);
    //string ans=data.find(temp);
    if(toneFilter!=nullptr){
        string temp_4=toneFilter->translatePostData(inneranswer(temp));
        if(temp_4!="Sorry, I don't know"){
            ans=temp_4;
        }
    }
    if(languageFilter!=nullptr){
        string temp_3=languageFilter->translatePostData(ans);
        if(temp_3!="Sorry, I don't know"){
            ans=temp_3;
        }
    }
    return ans;
}

void HandlerMap::insert(string key, HandlerModule *value){
    if (0 == arraySize)
    {
        arraySize = 1;
        names = new string[1]; names[0] = key;
        handlerModules = new HandlerModule*[1]; handlerModules[0]=value;
        return;
    }
    arraySize++;
    string* temps= new string[arraySize];
    HandlerModule** temph= new HandlerModule*[arraySize];
    for(int i=0;i<arraySize-1;i++){
        temps[i]=names[i];
        temph[i]=handlerModules[i];
    }
    temps[arraySize-1]=key; temph[arraySize-1]=value;
    delete[]names; delete[]handlerModules;
    names=temps;
    handlerModules=temph;
    return;
}
HandlerModule* HandlerMap::find(const string &key) const{
    HandlerModule* temp=nullptr; 
    for(int i=0;i<arraySize;i++){
        if(key==names[i]){
            temp=handlerModules[i];
            break;
        }
    }
    return temp;
}
string HandlerMap::getNames(int index) const{
    return names[index];
}
void HandlerMap::clear(){
    if(names!=nullptr){
        delete[]names;
        names=nullptr;
    }
    if(handlerModules!=nullptr){
        delete[]handlerModules;
        handlerModules=nullptr;
    }
}
HandlerMap::HandlerMap(){
    arraySize=0;
    names=nullptr; handlerModules=nullptr;
}
HandlerMap::~HandlerMap(){
    clear();
}