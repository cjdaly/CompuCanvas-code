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

import time, board
import c3common, c3dotstar

c3common.blink()

while True:
  if c3common.refresh_control():
    r = c3common.read_control("DotStar.R", 100)
    g = c3common.read_control("DotStar.G", 0)
    b = c3common.read_control("DotStar.B", 0)
    c3dotstar.set_color(int(r), int(g), int(b))
    time.sleep(0.5)
  else:
    c3common.blink(n=1,s=1)

