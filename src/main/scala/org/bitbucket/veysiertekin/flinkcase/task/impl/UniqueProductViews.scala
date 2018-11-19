package org.bitbucket.veysiertekin.flinkcase.task.impl

import org.apache.flink.core.fs.FileSystem
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.apache.flink.table.sinks.CsvTableSink
import org.bitbucket.veysiertekin.flinkcase.task.CaseTask

class UniqueProductViews(outputFileName: String) extends CaseTask {
  override def register(tableEnv: BatchTableEnvironment, tableName: String): Unit = {
    val uniqueProductViews = tableEnv.scan(tableName)
      .where("eventName='view'")
      .select("userId,productId")
      .distinct()
      .groupBy("productId")
      .select("productId,productId.count")

    val sink = new CsvTableSink(path = outputFileName, fieldDelim = Some("|"), numFiles = None, writeMode = Some(FileSystem.WriteMode.OVERWRITE))
    uniqueProductViews.writeToSink(sink)
  }
}
