#!/bin/bash
####
# Copyright (c) 2016 Chris J Daly (github user cjdaly)
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#   cjdaly - initial API and implementation
####

git clone https://github.com/cjdaly/Show2-Eboogaloo.git ~/Show2-Eboogaloo
ant -f ~/Show2-Eboogaloo/Show2-Eboogaloo-SETUP/setup.xml
ant -f ~/Show2-Eboogaloo/Show2-Eboogaloo-SETUP/setup.xml weatherThing.build
