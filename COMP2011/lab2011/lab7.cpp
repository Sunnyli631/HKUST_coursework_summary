#include <iostream>
#include <fstream>
#include "lab7.h"

using namespace std;

// TODO1: Reorder the linked list so that VIP customers come first
// Your would need to reorder the entries in the given array, with VIP customers comes first
//      Both the VIP customers and the normal customers would be then ordered by their ID repectively
//      Example: ID0_VIP, ID1_Non_VIP, ID2_VIP => ID0_VIP, ID2_VIP, ID1_Non_VIP
void reorder_customer(Customer* queue [], int num_customer) {
    
    for(int j=0;j<num_customer-1;j++){
        for(int i=0;i<num_customer-1-j;i++){
            
            if((*queue[i]).vip==false&&(*queue[i+1]).vip==true){
                Customer temp=*queue[i];
                *queue[i]=*queue[i+1];
                *queue[i+1]=temp;
            }
        }
    }
    return;
}

// TODO2: Let customers check out from the store, and get the money
// Customers comes in the ordered of the array
// For each of the customers' needs, if the store can satisfy it, sell the corresponding amount and get the paid money, otherwise skip this need (sell nothing and earn nothing)
//      Example: If a customer need 5 Apples and 5 Bananas and the store have 10 Apples and 2 Bananas left, then sell 5 Apples (and 0 Bananas) to the customer
// You don't need to delete the pointers to customers in this TODO
int check_out(Fruit fruits [], const Customer* const queue [], int num_fruit, int num_customer) {
    int count=0;int count_1=0;
    for(int i=0;i<num_fruit;i++){
        count_1=fruits[i].total_count;
        for(int j=0;j<num_customer;j++){
            if(count_1>=(*queue[j]).need[i]){
                count_1-=(*queue[j]).need[i];
                fruits[i].sold_count+=(*queue[j]).need[i];
                count+=(*queue[j]).need[i]*fruits[i].unit_price;
            }
        }
    }
    return count;
}