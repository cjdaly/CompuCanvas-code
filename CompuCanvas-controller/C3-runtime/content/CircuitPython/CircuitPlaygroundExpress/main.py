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

import c3common, c3neopixel
import time, board, analogio, pulseio

dac_led = analogio.AnalogOut(board.A0)
pwm_led = pulseio.PWMOut(board.A1)

c3common.blink()

while True:
  if c3common.refresh_control():
    dac_led.value = int(c3common.read_control("DAC.led", 0))
    pwm_led.duty_cycle = int(c3common.read_control("PWM.led", 0))
    #
    r = c3common.read_control("NeoPixel.R", 100)
    g = c3common.read_control("NeoPixel.G", 0)
    b = c3common.read_control("NeoPixel.B", 0)
    neo_pix.fill_solid(int(r), int(g), int(b))
    #
    time.sleep(0.5)
  else:
    c3common.blink(n=1,s=1)

