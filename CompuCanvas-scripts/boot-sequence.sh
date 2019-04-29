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

function espeaker() {
  espeak -a 20 "$@" 2>/dev/null
}

IP_ADDR=`hostname -I`
while [ -z $IP_ADDR ]; do
  sleep 1
  IP_ADDR=`hostname -I`
done
ESPEAK_IP_ADDR=`echo $IP_ADDR | sed -e 's/\(.\)/\1 /g' | sed -e 's/\./dot/g'`

sleep 3
blinkstick --index 0 off
blinkstick --index 1 off
sleep 1
blinkstick --index 0 --pulse white
sleep 1

blinkstick --index 1 --pulse blue
espeaker "Hello from Comp U Canvas"
sleep 2

blinkstick --index 1 --pulse blue
espeaker "I P address"
espeaker -s 100 "$ESPEAK_IP_ADDR"
sleep 2

blinkstick --index 1 --pulse blue
espeaker "repeating I P address"
espeaker -s 100 "$ESPEAK_IP_ADDR"
sleep 1

blinkstick --index 0 --limit 5 blue
blinkstick --index 1 --limit 5 green
