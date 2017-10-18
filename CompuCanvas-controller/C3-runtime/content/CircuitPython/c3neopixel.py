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

import neopixel

neo_pix =  neopixel.NeoPixel(board.NEOPIXEL, 10, auto_write=True)

def fill_solid(r, g, b):
  neo_pix.fill((r,g,b))

