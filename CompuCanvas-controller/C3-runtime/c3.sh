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

TIMESTAMP=`date +%Y%m%d-%H%M%S`

# http://www.ostricher.com/2014/10/the-right-way-to-get-the-directory-of-a-bash-script/
C3_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

C3_PID_FILE="$C3_HOME/c3.PID"

case "$1" in
  start)
  if [ -f "$C3_PID_FILE" ]; then
    C3_PID=`cat $C3_PID_FILE`
    echo "c3 process $C3_PID already running."
  else
    # log setup
    C3_LOGS_DIR="$C3_HOME/logs"
    mkdir -p $C3_LOGS_DIR
    C3_LOG="$C3_LOGS_DIR/c3-$TIMESTAMP.log"
    touch $C3_LOG
    rm -f "$C3_HOME/c3.log"
    ln -s $C3_LOG "$C3_HOME/c3.log"
    
    # CompuCanvas model
    if [ -z "$2" ]; then
      CC_MODEL_ID="A0"
    else
      CC_MODEL_ID="$2"
    fi
    
    # bundles.info location
    if [ -f "$C3_HOME/extend/plugins/bundles.info" ]; then
      C3_BUNDLES_INFO="file://$C3_HOME/extend/plugins/bundles.info"
    else
      C3_BUNDLES_INFO="file://$C3_HOME/eclipse/configuration/org.eclipse.equinox.simpleconfigurator/bundles.info"
    fi
    
    # launch eclipse
    java \
     -Dorg.eclipse.equinox.simpleconfigurator.configUrl=$C3_BUNDLES_INFO \
     -Dnet.locosoft.CompuCanvas.modelId=$CC_MODEL_ID \
     -jar $C3_HOME/eclipse/plugins/org.eclipse.equinox.launcher_1.3.100.v20150511-1540.jar \
     -consoleLog -clean \
     -data $C3_HOME/data/eclipse/workspace \
     -vmargs \
     -Xms40m -Xmx256m \
     1>> $C3_LOG 2>&1 &
    
    C3_PID=$!
    
    echo "$C3_PID" > $C3_PID_FILE
    echo "c3 process: $C3_PID" >> $C3_LOG
    echo "c3 process: $C3_PID"
    echo "c3 log: ./c3.log -> $C3_LOG"
  fi
  ;;
  stop)
  if [ -f "$C3_PID_FILE" ]; then
    C3_PID=`cat $C3_PID_FILE`
    rm $C3_PID_FILE
    echo "c3 process $C3_PID now shutting down."
    tail -f "$C3_HOME/c3.log" --pid=$C3_PID
  else
    echo "c3 already stopped or stopping."
  fi
  ;;
  status)
  if [ -f "$C3_PID_FILE" ]; then
    C3_PID=`cat $C3_PID_FILE`
    echo "c3 process $C3_PID apparently running."
  else
    echo "c3 stopped or stopping."
  fi
  ;;
  *)
  echo "c3 - CompuCanvas Controller - usage:"
  echo "  ./c3.sh status"
  echo "  ./c3.sh start [CompuCanvas modelId]"
  echo "  ./c3.sh stop"
  ;;
esac
