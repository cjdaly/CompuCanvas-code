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

#device_type:GemmaM0
#device_id:GemmaM0

import common, time, board
import adafruit_dotstar as dotstar

dot = dotstar.DotStar(board.APA102_SCK, board.APA102_MOSI, 1, brightness=0.4)
dot[0] = [0,100,100]

common.blink()

while True:
  common.refresh_control()
  r = common.read_control("DotStar.R", 100)
  g = common.read_control("DotStar.G", 0)
  b = common.read_control("DotStar.B", 0)
  dot[0] = [int(r), int(g), int(b)]
  time.sleep(1.0)

