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

import board, time
import adafruit_dotstar as dotstar

dots = dotstar.DotStar(board.APA102_SCK, board.APA102_MOSI, 1, brightness=0.4)
dots[0] = [0,100,100]

def set_color(r,g,b):
  global dots
  dots[0] = [r,g,b]

def set_brightness(b):
  global dots
  if b > 0.5:
    dots.brightness = 0.5
  else:
    dots.brightness = b

def pulse(steps, period):
  global dots
  max_br = dots.brightness
  db = max_br / steps
  dt = period / (steps * 2.0) 
  for i in range(0, steps):
    dots.brightness = max_br - i * db
    time.sleep(dt)
  for i in range(0, steps):
    dots.brightness = i * db
    time.sleep(dt)
  dots.brightness = max_br

