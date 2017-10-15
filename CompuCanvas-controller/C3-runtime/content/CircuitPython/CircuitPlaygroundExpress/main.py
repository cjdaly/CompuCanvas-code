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

import common, time, board
import analogio, pulseio, neopixel

dac_led = analogio.AnalogOut(board.A0)
pwm_led = pulseio.PWMOut(board.A1)

common.blink()

while True:
  common.refresh_control()
  dac_led.value = int(common.read_control("DAC.led", 0))
  pwm_led.duty_cycle = int(common.read_control("PWM.led", 0))
  time.sleep(1.0)

