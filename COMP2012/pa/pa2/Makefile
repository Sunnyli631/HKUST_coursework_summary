CPPFLAGS = -std=c++11 #-fsanitize=address -g
SRCS = main.cpp filter.cpp handler.cpp legoGPT.cpp array.cpp
OBJS = $(SRCS:.cpp=.o)
DEPS = $(OBJS:.o=.d)

all: main.exe

main.exe: $(OBJS)
	g++-12 -o $@ $(CPPFLAGS) $^

%.o: %.cpp
	g++-12 $(CPPFLAGS) -MMD -MP -c $< -o $@

-include $(DEPS)

clean:
	rm -f *.o *.exe *.d
# On Windows, use: del *.o *.exe *.d
# On Linux or MacOS, use: rm -f *.o *.exe *.d

.PRECIOUS: $(OBJS)