#include <iostream>
using namespace std;

int main(){

    int *op = new int;
    int *op2 = op;
    delete op;
    delete op2; // What happens here?

    return 0;
}