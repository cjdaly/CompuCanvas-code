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

# CompuCanvas model
if [ -z "$1" ]; then
  CC_MODEL_ID="CCd"
else
  CC_MODEL_ID="$1"
fi

COMPUCANVAS_SCRIPTS_HOME=/home/pi/CompuCanvas-code/CompuCanvas-scripts
COMPUCANVAS_C3_HOME=/home/pi/CompuCanvas-code/CompuCanvas-controller/C3-runtime

apt-get update
apt-get upgrade -y
apt-get install ant espeak mpg321 -y
pip install blinkstick
blinkstick --add-udev-rule

echo "Copying alsa-base.conf to /etc/modprobe.d ..."
cp "$COMPUCANVAS_SCRIPTS_HOME/setup/alsa-base.conf" /etc/modprobe.d

echo "Setting CompuCanvas model '$CC_MODEL_ID' in $COMPUCANVAS_C3_HOME/data/CC.id"
echo "$CC_MODEL_ID" > $COMPUCANVAS_C3_HOME/data/CC.id

echo "Adding startup callouts to /etc/rc.local ..."
sed -i -e "s:^exit 0$:$COMPUCANVAS_SCRIPTS_HOME/boot-sequence.sh\n\nexit 0:" /etc/rc.local
sed -i -e "s:^exit 0$:sudo -u pi -H $COMPUCANVAS_C3_HOME/c3.sh start\n\nexit 0:" /etc/rc.local
