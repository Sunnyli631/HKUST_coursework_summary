#include <iostream>
using namespace std;
#include "stock_management.h"

void stock_checking(const Fruit fruits[], int max_fruit_num)
{   
    //todo: print (name, total_count) pairs of each kind of fruit.
    //For example the last printed line should be watermelon: 10
    for(int i=0;i<MAX_FRUIT_NUM;i++){
        cout<<fruits[i].name<<": "<<fruits[i].total_count<<endl;
    }
    return;
}

// Update stock info and print (name, total_count) pairs of each kind of fruit.
void stock_updating(Fruit fruits[], int fruit_id_to_sell, int number_to_sell){
    //todo: update total_count of the sold fruit
    fruits[fruit_id_to_sell].total_count=fruits[fruit_id_to_sell].total_count-number_to_sell;
    fruits[fruit_id_to_sell].sold_count=fruits[fruit_id_to_sell].sold_count+number_to_sell;
    return;
}

// Calculate selling amount
float selling_amount_calculating(const Fruit fruits[], int max_fruit_num){
    float selling_amount = 0;
    // todo: calculate selling amount of all fruits; for each fruit this number should be the multiplication of unit price and sold count
    for(int i=0;i<MAX_FRUIT_NUM;i++){
        selling_amount=fruits[i].unit_price*fruits[i].sold_count+selling_amount;
    }
    return selling_amount;
}