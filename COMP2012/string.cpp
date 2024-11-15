#include <iostream>
using namespace std;
void compares(const string& s1,const string& s2){
    if(s1!=s2){
        cout<<s1<<" is not equal to "<< s2<<endl;
    }
    if(s1>s2){
        cout<<s1<<" is greater than "<<s2<<endl;
    }else{
        cout<<s2<<" is greater than "<<s1<<endl;
    }
}
int main(){

    string a("A");
    string b="B";
    compares(a,b);
    return 0;
}