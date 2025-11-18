
#!/bin/bash

help_text="$0 [...specific test cases; all if none listed] [options]
    --clean              Remove test output files
    --clean-only         Do not also run the tests after cleaning
    -c  --color          Enable coloured output
    -m  --make           Run make before tests
    -M  --make-clean     Run make clean and make before tests
    +e  --no-err         Ignore the output to stderr
    -h  --help           Print this help message
    -p  --parallel       Run all the tests in parallel
    -E  --show-err       Print the error output
    -O  --show-out-diff  Print the file diff between the program output and the 
                         expected output
    -s  --strict-errors  Check exact match of stderr output
"

# Modified from a script by Peter Oslington <peter.oslington@anu.edu.au>
shopt -s extglob 
trap 'printf "\n"; exit 1' INT

#######################################
# Argument Parsing                    #
#######################################

# Bash array of tests
TESTS=()

CLEAR="\33[2K"

# Parse arguments
for arg in "$@"; do

    ### Bunch of build options

    # Make from scratch source files
    if [[ $arg = "-M" ]] || [[ $arg = "--make-clean" ]]; then
      make clean && make all

      if [[ "$?" != 0 ]]; then
        echo "0 / 0 passed. (make failed)"
        exit 1
      fi

    # Run make before tests
  elif [[ $arg = "-m" ]] ||  [[ $arg = "--make" ]]; then
    make build

    if [[ "$?" != 0 ]]; then
      echo "0 / 0 passed. (make failed)"
      exit 1
    fi

    # Clean up all tests and exit
    # Will remove all .s files in the top level directory
  elif [[ $arg = "--clean-only" ]]; then
    rm -r output 2> /dev/null
    exit 0

    # Clean up all tests before running
    # Will remove all .s files in the top level directory
  elif [[ $arg = "--clean" ]]; then
    rm -r output 2> /dev/null

    ### Output formatting

    # Add colour output
  elif [[ $arg = "--color" ]] || [[ $arg == "-c" ]] ; then
    RED='[0;31m'
    GREEN='[0;32m'
    YELLOW='[0;33m'
    NORM='[0;00m'

    ### Printing behaviour

    # Dump output diff after test
  elif [[ $arg = "--show-out-diff" ]] || [[ $arg = "-O" ]]; then
    PRINT_OUTPUT_DIFF=1

    # Dump err after test
  elif [[ $arg = "--show-err" ]] || [[ $arg = "-E" ]]; then
    PRINT_ERROR=1

  elif [[ $arg = "--no-err" ]] || [[ $arg = "+e" ]]; then
    RUN_ERROR_TEST=1

  elif [[ $arg = "--strict-errors" ]] || [[ $arg = "-s" ]]; then
    RUN_ERROR_TEST=1

  elif [[ $arg = "--parallel" ]] || [[ $arg = "-p" ]] ; then
    PARALLEL=1

  elif [[ $arg = "--help" ]] || [[ $arg = "-h" ]]; then
    printf "$help_text"
    exit 0


    # Else assume is test name
  else
    if [[ ${arg:0:1} == "-" ]]; then
      echo "Unrecognized option: '$arg'"
      exit 1
    else
      TESTS+=("$arg")
    fi
    fi
  done

#######################################
# Define a bunch of testing functions #
#######################################

# Actually run the test, outputting to the normal folders
function run_test() {
  # Test name is first argument
  test_name=$1

  # Output files
  out=output/${test_name##tests/}.out # output file (no slashes)
  err=output/${test_name##tests/}.err # output file (no slashes)

  $TIMEOUT java -ea -cp bin/ ParserTokenManager < $t 2> $err > $out

}

#######################################
# Variable setup                      #
#######################################


# Setup for tests
mkdir -p output/

# Set default tests to all
if [ ${#TESTS[@]} == 0 ]; then
  TESTS=(tests/*.mj)
fi

# Use timeout if present
TIMEOUT=$(which timeout)
if [ "$?" = "0" ]; then
  TIMEOUT="timeout 10"
else
  TIMEOUT=""
fi

# During test print behaviour
PRINT_OUTPUT_DIFF=${PRINT_OUTPUT_DIFF:=0}
PRINT_ERROR_DIFF=${PRINT_ERROR_DIFF:=0}
PARALLEL=${PARALLEL:=0}

# Tests to run
RUN_OUTPUT_TEST=${RUN_OUTPUT_TEST:=1}
RUN_ERROR_TEST=${RUN_ERROR_TEST:=1}
RUN_STRICT_ERROR_TEST=${RUN_STRICT_ERROR_TEST:=0}

# Init counts
passed=0
failed=0
bonus=0
total=${#TESTS[@]}

#######################################
# Main test loop                      #
#######################################

if [ $PARALLEL == 1 ]; then
  for t in ${TESTS[@]}; do
    # Print running message
    printf "${YELLOW}run${NORM}   ${t}\n"
    # Generate output files
    run_test $t &
  done

  wait
fi

for t in ${TESTS[@]}; do

  if [ $PARALLEL == 0 ]; then
    # Print running message
    printf "${YELLOW}run${NORM}   $t\r"
    # Generate output files
    run_test $t
  fi
done

  # Expected output files
  expout=$t.out             # expected stdout
  experr=$t.err             # expected stderr
exit $?
