#!/bin/bash
echo "Stopping wiki.jar..."

process_id=$(ps -ef | grep wiki.jar | grep -v grep | awk '{print $2}')
if [ "$process_id" ]; then
  kill -9 $process_id
  echo "wiki.jar (PID $process_id) stopped."
else
  echo "No wiki.jar process found."
fi
