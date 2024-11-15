#ifndef THERAPYDOG_H
#define THERAPYDOG_H

#include "Dog.h"

//  TODO: Complete the class declaration of TherapyDog
class TherapyDog:public Dog{
public:
    TherapyDog(const std::string &name, const std::string &location, const std::string &employment);
    std::string get_employment() const;
    void smile() const;
private:
    std::string employment;
};

#endif