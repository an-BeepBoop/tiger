SRC := $(wildcard *.java)
OUT := out

# Default target: compile
all: $(OUT)/.compiled

$(OUT)/.compiled: $(SRC)
	mkdir -p $(OUT)
	javac -d $(OUT) $(SRC)
	@touch $@

# Run the program (assuming main class is interp)
run: all
	java -classpath $(OUT) Interp

# Clean build artifacts
clean:
	rm -rf $(OUT)

.PHONY: all run clean
