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

import board, digitalio, time, os

led = digitalio.DigitalInOut(board.D13)
led.direction = digitalio.Direction.OUTPUT

def blink(n=3,s=0.5):
  for i in range(0,n):
    led.value = True
    time.sleep(s)
    led.value = False
    time.sleep(s)

control={}
control_mod_time=0


def refresh_control():
  try:
    check_mod_time = os.stat('control.txt')[8] # st_mtime?
    if (check_mod_time > control_mod_time):
      with open('control.txt') as fp:
        for line in fp:
          kvs=line.split(';')
          for kv in kvs:
            if kv.find('=') != -1:
              k,v=kv.split('=')
              control[k]=v
      control_mod_time = check_mod_time
    return True
  except OSError:
    return False

def read_control(key, default):
  if key in control:
    return control[key]
  else:
    return default

