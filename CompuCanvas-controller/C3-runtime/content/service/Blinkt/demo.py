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

import random
import time
from blinkt import set_pixel, set_brightness, show, clear

set_brightness(0.4)

while True:
  r = 0
  g = random.randint(0,40)
  b = random.randint(20,100)
  for i in range(8):
    set_pixel(i,80,80,80)
    show()
    time.sleep(0.3)
    #
    set_pixel(i,100,100,200)
    show()
    time.sleep(0.3)
    #
    set_pixel(i,80,80,80)
    show()
    time.sleep(0.3)
    #
    set_pixel(i,r,g,b)
    show()
    time.sleep(0.5)
