#include <iostream>
using namespace std;

int main(){

    int* ptr1=new int[10];
    delete ptr1;
    delete ptr1;
    return 0;
}
