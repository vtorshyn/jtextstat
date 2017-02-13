.PHONY: all
# The reason to use Make... is the only one dependency - java 8)

sources=Main.class
MainClass=Main

TEST_DATA_BLOCKS=10000000
TEST_DATA_FILE=input.txt
TEST_TEXT=.This is a test text, a text used by a test to test!

all: build-app

build-app: $(sources)

run: build-app prepare-test run-test

run-test:
	time java -cp $$PWD:. $(MainClass) $(TEST_DATA_FILE)
run-test-sample:
	time java -cp $$PWD:. $(MainClass) $(shell *.in)
$(sources):
	$(eval Class=$(shell echo $@ | sed 's/\.class/\.java/g'))
	@echo "Compiling: $(Class)"
	@javac -cp $(CLASSPATH):. $(Class)
clean:
	@find ./ -name "*.class" | xargs rm > /dev/null 2>&1 || true

distclean: clean clean-test-data
	@find ./ -name "*~" | xargs rm > /dev/null 2>&1 || true

prepare-test: notify-gen-test gen-test-data
# Simple target to print gen. data releated information
notify-gen-test:
	@echo "generating test data $(TEST_DATA_FILE) with $(TEST_DATA_BLOCKS) blocks  ..."

gen-test-data: $(TEST_DATA_FILE)

# Do not afraid of this :)
# It's just faster way to create HUGE files if required
$(TEST_DATA_FILE):
	@perl -e \
	'open(my $$input_data, ">", "$(TEST_DATA_FILE)"); for( $$a = 0; $$a < $(TEST_DATA_BLOCKS); $$a = $$a + 1 ){ print $$input_data "$(TEST_TEXT)\n"; }; close($$input_data);'
	@echo "test data generated"

clean-test-data:
	rm -f $(TEST_DATA_FILE)