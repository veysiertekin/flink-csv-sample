#!/bin/sh

# Execute task just on job manager
if [ "$1" = "jobmanager" ]; then
    until flink run /job.jar --csvFile "$CSV_FILE" --outputPath "$OUTPUT_PATH"; do
      echo "Flink is not active. Will be waiting for 2 seconds to retry..."
      sleep 2
    done &
fi

exec /docker-entrypoint.sh "$@"