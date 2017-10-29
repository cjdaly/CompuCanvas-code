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

import board, neopixel

neo_pix =  neopixel.NeoPixel(board.NEOPIXEL, 10, auto_write=False)

def fill_solid(colorRGB, altColorRGB = None):
  neo_pix.fill(colorRGB)
  if (altColorRGB is not None):
    neo_pix[1] = altColorRGB
    neo_pix[3] = altColorRGB
    neo_pix[6] = altColorRGB
    neo_pix[8] = altColorRGB
  neo_pix.show()

