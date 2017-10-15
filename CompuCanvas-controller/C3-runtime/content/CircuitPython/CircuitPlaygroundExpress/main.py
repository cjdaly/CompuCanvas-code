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

#device_type:CircuitPlaygroundExpress
#device_id:CircuitPlaygroundExpress

import board, digitalio, time
import neopixel

led = digitalio.DigitalInOut(board.D13)
led.direction = digitalio.Direction.OUTPUT

for i in range(0,3):
  led.value = True
  time.sleep(0.5)
  led.value = False
  time.sleep(0.5)

control={}

def refresh_control():
  with open('control.txt') as fp:
    for line in fp:
      kvs=line.split(';')
      for kv in kvs:
        k,v=kv.split('=')
        control[k]=v

def read_control(key, default):
  if key in control:
    return control[key]
  else:
    return default

while True:
  refresh_control()
  time.sleep(1.0)
