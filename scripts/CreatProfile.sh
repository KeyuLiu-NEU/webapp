#!/bin/bash
{ echo "${aws_access_key}";
  echo "${aws_secret_key}";
  echo "us-east-1";
  echo "text"
} |aws configure
#expect "AWS Access Key ID [None]:"
#send "${aws_access_key}"
#send "1234"
#expect "AWS Secret Access Key [None]:"
#send "${aws_secret_key}"
#send "asadasdas"
#expect "Default region name [None]:"
#send "us-east-2"
#expect "Default output format [None]:"
#send "text"