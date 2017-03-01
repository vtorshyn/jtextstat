java_sources_dir=$(shell pwd)/src/main/java
java_sources=$(shell find $(java_sources_dir) -type f -name '*.java')
java_build_dir=build

xperement_opts=-Xss384k -XX:NewSize=128m -XX:MaxNewSize=384m -Xms512m -Xmx1024m
java_runtime_options=-server $(xperement_opts)
java_runtime_debug_options=-Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=y -server
java_runtime_profile_options=-Xprof

generated_test_file_lines_count=10000000
generated_test_file_path=$(java_build_dir)/input.txt
generated_test_file_text=.This is a test text, a text used by a test to test!

java_application_read_buffer ?= 16384
java_application_thread_count ?= 1

java_application_options=-blockSize $(java_application_read_buffer) -queue $(java_application_thread_count) -threads $(java_application_thread_count)
java_application_main_class ?= com.vtorshyn.Main

test_sanity_data_file=data/test.task.text
test_sanity_expect = \
    merry=16 \
    christmas=8 \
    and=6

test_long_line_file=data/test.long.line.text
test_long_line_expect = \
    V2VkIE1hciAgMSAxNjowMTo1NiBVVEMgMjAxNwo=924 \
    V2VkIE1hciAgMSAxNjowMTo1NyBVVEMgMjAxNwo=132
compile: java_build

# Simple and very efficient way to compile sources without tracking dependecies manually
java_build: $(java_sources) $(java_build_dir)/create
	javac -sourcepath $(java_sources_dir) \
		-d $(java_build_dir) \
		$(java_sources)

# An wrapper for mkdir which creates output directory for class files
$(java_build_dir)/create:
	mkdir -p $(java_build_dir)
	touch $(java_build_dir)/create

# Default target will compile application by default
all: compile

# An abstract target which which executes application with options defined in ``java_application_options``
$(java_application_main_class): compile $(generated_test_file_path)
	date && time java $(java_runtime_options) \
		-cp $(java_build_dir):. \
		$(java_application_main_class) \
		$(java_application_options) \
		-file $(generated_test_file_path) && date

# Do not afraid of this :)
# It's just faster way to create HUGE files
$(generated_test_file_path):
	@perl -e \
	'open(my $$input_data, ">", "$(generated_test_file_path)"); for( $$a = 0; $$a < $(generated_test_file_lines_count); $$a = $$a + 1 ){ print $$input_data "$(generated_test_file_text)\n"; }; close($$input_data);'
	@echo "test data generated"

# Appends ``java_runtime_debug_options`` to ``java_runtime_options``  and executes application
export-debug:
	$(eval java_runtime_options += $(java_runtime_debug_options))
debug: export-debug $(java_application_main_class)

# Appends ``java_runtime_profile_options`` to ``java_runtime_options``  and executes application
export-prof:
	$(eval java_runtime_options += $(java_runtime_profile_options))
prof: export-prof $(java_application_main_class)

# Removes compiled classes
clean:
	rm -rf $(java_build_dir)/com/

# Distribution clean up steps.
distclean: clean clean-test-data
	find ./ -name "*~" | xargs rm > /dev/null 2>&1 || true
	[ -d target ] && rm -rf target || true
	[ -d $(java_build_dir) ] && rm -rf $(java_build_dir) || true

# Removing generated input file
clean-test-data:
	rm -f $(generated_test_file_path)
	
test: test-sanity $(test_sanity_expect) test-long-line $(test_long_line_expect)
	@echo "\nTests passed"

test-sanity:
	@echo "\nStarting test-sanity"
	java $(java_runtime_options) \
		-cp $(java_build_dir):. \
		$(java_application_main_class) \
		$(java_application_options) \
		-ignoreCase true \
		-file $(test_sanity_data_file) > $(java_build_dir)/log

test-long-line:
	@echo "\nStarting test-long-line"
	java $(java_runtime_options) \
		-cp $(java_build_dir):. \
		$(java_application_main_class) \
		$(java_application_options) \
		-delta 100 \
		-file $(test_long_line_file) > $(java_build_dir)/log

$(test_sanity_expect):
	@echo "Expect from output: $@"
	@grep "$@" $(java_build_dir)/log || exit 1

$(test_long_line_expect):
	@echo "Expect from output: $@"
	@grep "$@" $(java_build_dir)/log || exit 1