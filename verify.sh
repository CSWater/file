#!/bin/bash

cp Trace.java ./ticketingsystem
javac -encoding UTF-8 -cp . ticketingsystem/Trace.java
java -cp . ticketingsystem/Trace >& trace

java -jar verify.jar trace
