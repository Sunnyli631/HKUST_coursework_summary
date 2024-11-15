#include <iostream>
using namespace std;
class Weird{
        private:
        short height;

        public:
        Weird(){
            height=5;
            cout<<height<<endl;
        }
        Weird(int height){
            height=height;
            cout<<height<<endl;
        }
        void Print_x()const{
            cout<<height<<endl;
        }
    };
int main(){
    int height =10;
    Weird a;
    Weird();
    Weird(9);
    a.Print_x();
    cout<<height;
    return 0;
}