####
# Copyright (c) 2017 Chris J Daly (github user cjdaly)
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#   cjdaly - initial API and implementation
####

import time
from blinkt import set_pixel, set_brightness, show, clear

set_brightness(0.3)
clear()
set_pixel(0,   0,   0, 100)
set_pixel(1,   0, 100, 0)
set_pixel(2, 100,   0, 0)
set_pixel(3,   0, 100, 100)
set_pixel(4, 100, 100, 0)
set_pixel(5, 100,   0, 100)
set_pixel(6, 100, 100, 100)
set_pixel(7,  10,  10, 10)
show()

while True:
  time.sleep(1.0)

