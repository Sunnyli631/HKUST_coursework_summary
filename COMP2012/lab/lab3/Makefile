CPPFLAGS = -std=c++11

all: lab3.exe

lab3.exe: Dog.o StrayDog.o TherapyDog.o Animal.o Boar.o main.cpp
	g++-12 -o $@ $(CPPFLAGS) Dog.o StrayDog.o TherapyDog.o Animal.o Boar.o main.cpp

%.o: %.cpp
	g++-12 $(CPPFLAGS) -c $< -o $@ 

clean:
	rm -f *.o *.exe
# On Windows, use: del *.o *.exe
# On Linux or MacOS, use: rm -f *.o *.exe
