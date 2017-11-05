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

def lum(rgb, pct):
  return int(rgb*(pct/100.0))

def random_pulse(r,g,b):
  px = random.randint(0,7)
  for pct in range(0, 100, 10):
    set_pixel(px, lum(r,pct), lum(g,pct), lum(b,pct))
    show()
    time.sleep(0.02)
  set_pixel(px, lum(r,90), lum(g,90), lum(b,90))
  show()
  time.sleep(0.02)
  set_pixel(px, lum(r,80), lum(g,80), lum(b,80))
  show()
  time.sleep(0.02)

while True:
  random_pulse(120, 120, 0)
  time.sleep(0.1)
  random_pulse(160, 0, 0)
  time.sleep(1.0)

