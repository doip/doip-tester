#!/bin/bash

function printerror {
	echo "############################################################################"
	echo "ERROR: Call \"$1\" failed, exit code = $2"
	echo "############################################################################"
}

function runcommand {
	echo "Call" $1
	eval "$1"
	exitcode=$?
	if [ $exitcode -ne 0 ]; then
		echo "############################################################################"
		echo "ERROR: Call \"$1\" failed, exit code = $exitcode"
		echo "############################################################################"
		if [ "$2" = true ]; then
			exit $exitcode
		fi
	fi
	return $exitcode
}


if [ ! -d "./logs" ]; then
	mkdir ./logs
else
	rm -f ./logs/*.log
fi

# Log some information about the Git repository
echo '------------------------------------------------------------------------------' >> ./logs/git.log
echo 'Output of command "git log HEAD^..HEAD"' >> ./logs/git.log
echo '------------------------------------------------------------------------------' >> ./logs/git.log
runcommand 'git log HEAD^..HEAD >> logs/git.log' false

echo '------------------------------------------------------------------------------' >> ./logs/git.log
echo 'Output of command "git status"' >> ./logs/git.log
echo '------------------------------------------------------------------------------' >> ./logs/git.log
runcommand 'git status >> logs/git.log' false

echo '------------------------------------------------------------------------------' >> ./logs/git.log
echo 'Output of command "git diff"' >> ./logs/git.log
echo '------------------------------------------------------------------------------' >> ./logs/git.log
runcommand 'git diff >> logs/git.log' false

if [ -z "$1" ]; then
	testselection='*TR_*'
else
	testselection=$1
fi


runcommand './gradlew clean' true
runcommand './gradlew build -x test' true
runcommand './gradlew test --tests '$testselection false
runcommand './gradlew jacocoTestReport' false

if [ -f ./reports.tar.gz ]; then
	rm reports.tar.gz
fi

if [ -d "./build/reports" ]; then
	cp -r ./logs ./build/reports
	cd ./build
	echo "Create archive for reports"
	tar -czf reports.tar.gz reports
	mv reports.tar.gz ..
	cd ../
fi

echo "TESTS EXECUTED"
echo "  - A HTML test report generated from Gradle is available in"
echo "    subfolder \"./build/reports/tests/test\". The HTML test report"
echo "    contains the log messages which have been written to the"
echo "    console."
echo "  - The output of the tests is configured in the file"
echo "    \"src/test/resources/log4j2.xml\". Depending on the configuration"
echo "    within this file there might have been written log files, results"
echo "    stored in a database, sent to a TCP or UDP server or whatever else"
echo "    is possible in Log4j 2."
echo "  - A code coverage report will be created after the test execution."
echo "    It will be written to \"./build/reports/jacoco\""
echo "  - A TAR archive will be created. It contains the HTML report,"
echo "    all the log files which have been written in subfolder \"./logs\""
echo "    (Be sure that log4j2.xml writes the logs into subfolder"
echo "    \"./logs\") and the code coverage report. Technically the subfolder"
echo "    \"./logs\" will be copied to subfolder \"./build/reports\" and then"
echo "    the subfolder \"./build/reports\" will be archived."
echo "    The archive has the name reports.tar.gz and is located in the"
echo "    root folder of the project."
