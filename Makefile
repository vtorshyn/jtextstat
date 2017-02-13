.PHONY: all
# The reason to use Make... is the only one Java dependency - java 8)

PACKAGE=com.vtorshyn
MAINCLASS=$(PACKAGE).Main
BUILD_DIR=build/
PACKAGE_TO_DIR=`echo $(PACKAGE) | sed 's/\./\//g'`

# Path to class files in $(BUILD_DIR)/$(PACKAGE_TO_DIR)
CLASSES=exceptions/CommandLineException.class \
	utils/ApplicationOptions.class \
	utils/Logger.class \
	utils/CharUtils.class \
	Main.class


TEST_DATA_LINES=10000000
TEST_DATA_FILE=$(BUILD_DIR)/input.txt
TEST_TEXT=.This is a test text, a text used by a test to test!
TASK_SAMPLE=task-text.in

# Default target
all: build-app

# An alias for compiling java sources
build-app: $(CLASSES)

# Grouping steps required to execute app with provided sample in email
run: build-app run-test-sample

# Another test
# It generates file with amount of lines defined in TEST_DATA_LINES
run-test: build-app clean-test-data prepare-test
	cd $(BUILD_DIR)/ && time java -cp $(BUILD_DIR)/$(PACKAGE_TO_DIR):. $(MAINCLASS) -input $(TEST_DATA_FILE)

# An target to execute application with provided sample data file
run-test-sample: clean-test-data
	ln -s ../$(TASK_SAMPLE) $(BUILD_DIR)/input.txt 
	cd $(BUILD_DIR)/ && time java -cp $(PACKAGE_TO_DIR):. $(MAINCLASS) -input input.txt

# Main target used to compile sources.
# Please note usage .class ext in sources variable.
$(CLASSES): make-dst-dirs
	$(eval Class=$(shell echo $@ | sed 's/\.class/\.java/g'))
	@echo "Compiling: $(Class)"
	@javac -cp $(CLASSPATH):$(BUILD_DIR) -d $(BUILD_DIR) ./src/main/java/$(PACKAGE_TO_DIR)/$(Class)

#
make-dst-dirs:
	mkdir -p $(BUILD_DIR)/$(PACKAGE_TO_DIR)
# Cleaning build artefacts
clean:
	rm -rf $(BUILD_DIR)
# Distribution clean up steps.
distclean: clean clean-test-data
	find ./ -name "*~" | xargs rm > /dev/null 2>&1 || true
	[ -d target ] && rm -rf target

# Grouping required steps to be executed before test run
prepare-test: notify-gen-test gen-test-data

# Simple target to print gen. data releated information
notify-gen-test:
	@echo "generating test data $(TEST_DATA_FILE) with $(TEST_DATA_LINES) blocks  ..."

# An alias to $(TEST_DATA_FILE)
gen-test-data: $(TEST_DATA_FILE)

# Do not afraid of this :)
# It's just faster way to create HUGE files if required
$(TEST_DATA_FILE):
	@perl -e \
	'open(my $$input_data, ">", "$(TEST_DATA_FILE)"); for( $$a = 0; $$a < $(TEST_DATA_LINES); $$a = $$a + 1 ){ print $$input_data "$(TEST_TEXT)\n"; }; close($$input_data);'
	@echo "test data generated"

# Removing generated input file
clean-test-data:
	rm -f $(TEST_DATA_FILE)