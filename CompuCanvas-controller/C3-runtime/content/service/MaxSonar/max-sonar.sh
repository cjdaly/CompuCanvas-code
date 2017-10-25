#!/bin/bash
####
# Copyright (c) 2016 Chris J Daly (github user cjdaly)
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#   cjdaly - initial API and implementation
####

if [ -z "$2" ]; then
  MAX_SONAR_DEV=/dev/ttyUSB0
else
  MAX_SONAR_DEV=$2
fi

echo "MaxSonar: device=$MAX_SONAR_DEV"

( echo "MaxSonar: PID=$$" ; stty -F $MAX_SONAR_DEV 57600 ; cat $MAX_SONAR_DEV )
