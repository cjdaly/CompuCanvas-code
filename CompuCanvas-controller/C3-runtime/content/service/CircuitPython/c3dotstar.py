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

def pulse(delay=0.02):
  global dots
  r,g,b = dots[0]
  rgb = [r,g,b]
  while (r+g+b > 0):
    r=0 if r<=0 else r-1
    g=0 if g<=0 else g-1
    b=0 if b<=0 else b-1
    dots[0] = [r,g,b]
    time.sleep(delay)
  done=False
  while not done:
    r=r+1 if r<rgb[0] else rgb[0]
    g=g+1 if g<rgb[1] else rgb[1]
    b=b+1 if b<rgb[2] else rgb[2]
    dots[0] = [r,g,b]
    time.sleep(delay)
    done = rgb==[r,g,b]


