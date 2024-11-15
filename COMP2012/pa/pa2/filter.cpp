#include "filter.h"

FilterModule::~FilterModule(){
    preData.clear();
    postData.clear();
}

void FilterModule::print() const{
    std::cout << "Module name = " << m_name << ", this is a filter, filter type = " << getFilterType(type) << std::endl;
}

void FilterModule::trainPreData(const string &source, const string &target){
    preData.insert(source,target);
}
void FilterModule::trainPostData(const string &source, const string &target){
    postData.insert(source,target);
}

string FilterModule::translatePreData(string question) const{
    string temp=preData.find(question);
    if(temp==""){
        return question;
    }
    return temp;
}

string FilterModule::translatePostData(string question) const{
    string temp=postData.find(question);
    if(temp==""){
        return "Sorry, I don't know";
    }
    else {return temp;}
}

void FilterArray::insert(FilterModule *input){
    if (0 == arraySize)
    {
        arraySize = 1;
        array= new FilterModule*[1];
        array[0]=input;
        return;
    }
    arraySize++;
    FilterModule** tempf= new FilterModule*[arraySize];
    for(int i=0;i<arraySize-1;i++){
        tempf[i]=array[i];
    }
    tempf[arraySize-1]=input;
    delete[]array; array=tempf;
    return;
}
FilterModule *FilterArray::get(int index) const{
    return array[index];
}