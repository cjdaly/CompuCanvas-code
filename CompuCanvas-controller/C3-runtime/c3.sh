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
    if [ -d "/proc/$C3_PID" ]; then
      echo "C3 process $C3_PID is already running!"
      exit 1
    else
      echo "C3 is removing old c3.PID file."
      rm "$C3_PID_FILE"
    fi
  fi
  
  # log setup
  C3_LOGS_DIR="$C3_HOME/logs"
  mkdir -p $C3_LOGS_DIR
  C3_LOG="$C3_LOGS_DIR/c3-$TIMESTAMP.log"
  touch $C3_LOG
  rm -f "$C3_HOME/c3.log"
  ln -s $C3_LOG "$C3_HOME/c3.log"
  
  # CompuCanvas model
  if [ -z "$2" ]; then
    if [ -f "$C3_HOME/config/CC.id" ]; then
      read CC_id_first CC_id_rest < "$C3_HOME/config/CC.id"
      CC_MODEL_ID="$CC_id_first"
    else
      CC_MODEL_ID="CCd"
    fi
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
  echo "C3 process: $C3_PID" >> $C3_LOG
  echo "C3 process: $C3_PID"
  echo "C3 log: ./c3.log -> $C3_LOG"
  ;;
  stop)
  if [ -f "$C3_PID_FILE" ]; then
    C3_PID=`cat $C3_PID_FILE`
    rm $C3_PID_FILE
    echo "C3 process $C3_PID is now shutting down."
    tail -f "$C3_HOME/c3.log" --pid=$C3_PID
  else
    echo "C3 is already stopped or stopping."
  fi
  ;;
  status)
  if [ -f "$C3_PID_FILE" ]; then
    C3_PID=`cat $C3_PID_FILE`
    if [ -d "/proc/$C3_PID" ]; then
      echo "C3 process $C3_PID is apparently running."
    else
      rm "$C3_PID_FILE"
      echo "C3 is stopped (removed old c3.PID file)."
    fi
  else
    echo "C3 is stopped or stopping."
  fi
  ;;
  *)
  echo "c3 - CompuCanvas Controller - usage:"
  echo "  ./c3.sh status"
  echo "  ./c3.sh start [CompuCanvas modelId]"
  echo "  ./c3.sh stop"
  ;;
esac

