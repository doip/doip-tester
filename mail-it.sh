#!/bin/bash

if [ -z "$1" ]
then
	echo "ERROR: You must specify a email address to which the email shall be sent to"
	exit 1
fi

mutt -s "DoIP Test Reports" $1 -a ./reports.zip < mail.txt

