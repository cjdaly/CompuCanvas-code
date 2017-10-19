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

while True:
  if c3common.refresh_control():
    dac_led.value = c3common.read_control_int("DAC.led", 0)
    pwm_led.duty_cycle = c3common.read_control_int("PWM.led", 0)
    #
    r = c3common.read_control_int("NeoPixel.R", 80)
    g = c3common.read_control_int("NeoPixel.G", 0)
    b = c3common.read_control_int("NeoPixel.B", 0)
    #
    r2 = c3common.read_control_int("NeoPixel.alt.R", 0)
    g2 = c3common.read_control_int("NeoPixel.alt.G", 80)
    b2 = c3common.read_control_int("NeoPixel.alt.B", 0)
    c3neopixel.fill_solid((r, g, b), (r2, g2, b2))
    #
    time.sleep(1.0)
  else:
    c3common.blink(n=1,s=1)

