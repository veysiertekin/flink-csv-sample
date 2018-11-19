package org.bitbucket.veysiertekin.flinkcase.task.impl

import org.apache.flink.core.fs.FileSystem
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.apache.flink.table.sinks.CsvTableSink
import org.bitbucket.veysiertekin.flinkcase.task.CaseTask

class EventsOfUserFortySeven(outputFileName: String) extends CaseTask {
  override def register(tableEnv: BatchTableEnvironment, tableName: String): Unit = {
    val eventCountsOfUser47 = tableEnv.scan(tableName)
      .where("userId=47")
      .groupBy("eventName")
      .select("eventName,eventName.count")

    val sink = new CsvTableSink(path = outputFileName, fieldDelim = Some("|"), numFiles = None, writeMode = Some(FileSystem.WriteMode.OVERWRITE))
    eventCountsOfUser47.writeToSink(sink)
  }
}
