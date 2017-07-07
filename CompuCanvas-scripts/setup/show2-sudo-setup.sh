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

apt-get install arduino -y
pip install ino

sed -i -e "s:^# CompuCanvas$:# Show2\n\n/home/pi/Show2-Eboogaloo/show2.sh blt255 siz6 fg3 /0/Compu /1/Canvas ledB > /dev/null 2>\&1\n\n# CompuCanvas:" /etc/rc.local
