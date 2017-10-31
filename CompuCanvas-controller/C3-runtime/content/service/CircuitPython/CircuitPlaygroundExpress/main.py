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
#device_id:CPX-1

import c3common, c3neopixel
import time, board, analogio, pulseio

dac_led = analogio.AnalogOut(board.A0)
pwm_led = pulseio.PWMOut(board.A1)

c3common.blink()

flip = True

while True:
  if c3common.refresh_control():
    dac_lo = c3common.read_control_int("DAC.led.lo", 47000)
    dac_hi = c3common.read_control_int("DAC.led.hi", 48000)
    pwm_lo = c3common.read_control_int("PWM.led.lo", 0)
    pwm_hi = c3common.read_control_int("PWM.led.hi", 0)
    #
    if flip:
      for i in range(dac_lo, dac_hi, 2):
        dac_led.value = i
        time.sleep(0.01)
      for i in range(pwm_lo, pwm_hi, 2):
        pwm_led.duty_cycle = i
        time.sleep(0.01)
    else:
      for i in range(dac_hi, dac_lo, -2):
        dac_led.value = i
        time.sleep(0.01)
      for i in range(pwm_hi, pwm_lo, -2):
        pwm_led.duty_cycle = i
        time.sleep(0.01)
    #
    r = c3common.read_control_int("NeoPixel.R", 80)
    g = c3common.read_control_int("NeoPixel.G", 0)
    b = c3common.read_control_int("NeoPixel.B", 0)
    #
    r2 = c3common.read_control_int("NeoPixel.alt.R", 0)
    g2 = c3common.read_control_int("NeoPixel.alt.G", 80)
    b2 = c3common.read_control_int("NeoPixel.alt.B", 0)
    flip = not flip
    if flip:
      c3neopixel.fill_solid((r, g, b), (r2, g2, b2))
    else:
      c3neopixel.fill_solid((r2, g2, b2), (r, g, b))
    #
    time.sleep(0.1)
  else:
    c3common.blink(n=1,s=1)

