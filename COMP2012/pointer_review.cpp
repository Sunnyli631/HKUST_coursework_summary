#include <iostream>
using namespace std;

int main(){
    int *p1,p2;
    p2=10;
    p1=&p2;
    int a=1,*b=&a,c=3,*d=&c;
    cout<<a<<" "<<*b<<endl;
    cout<<c<<" "<<*d<<endl;
    cout<<p2<<" "<<*p1<<endl;
    //cout<<a<<" "<<*b;
    cout<<endl<<endl;
    int arr[]={};
    const char array_1[]={};
    cout<<sizeof(int)<<" "<<sizeof(arr)<<endl;
    cout<<sizeof(float)<<" "<<endl;
    cout<<sizeof(double)<<" "<<endl;
    cout<<sizeof(char)<<" "<<sizeof(array_1)<<endl;
    cout<<sizeof(long long)<<" "<<endl;

    int array[][3]={};int x=0;
    for(int i=0;i<3;i++){
        for(int j=0;j<3;j++){
            array[i][j]=x;
            x++;
    }}
    cout<<endl;
    cout<<x<<" "<<*(*(array)+2);
    return 0;
}