#!/bin/bash
####
# Copyright (c) 2019 Chris J Daly (github user cjdaly)
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

espeaker "Hello from Comp U Canvas"
sleep 2

espeaker "I P address"
espeaker -s 100 "$ESPEAK_IP_ADDR"
sleep 2

espeaker "repeating I P address"
espeaker -s 100 "$ESPEAK_IP_ADDR"
