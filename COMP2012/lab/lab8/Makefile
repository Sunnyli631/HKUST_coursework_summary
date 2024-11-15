# use CPPFLAGS to set proper configurations in different platforms
CPPFLAGS = -std=c++11 #-fsanitize=address
SRCS = public_test.cpp
DEPS = $(SRCS:.cpp=.d)

all: public_test.exe

%_test.exe: %_test.o
	g++-12 -o $@ $(CPPFLAGS) $^

%.o: %.cpp
	g++-12 -o $@ $(CPPFLAGS) -MMD -MP -c $<

-include $(DEPS)

clean:
	rm -f *.o *.exe *.d
# (Windows) del *.o *.exe *.d
# (Linux or MacOS) rm -f *.o *.exe *.d