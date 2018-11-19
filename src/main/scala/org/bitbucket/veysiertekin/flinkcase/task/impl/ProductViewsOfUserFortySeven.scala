package org.bitbucket.veysiertekin.flinkcase.task.impl

import org.apache.flink.core.fs.FileSystem
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.apache.flink.table.sinks.CsvTableSink
import org.bitbucket.veysiertekin.flinkcase.task.CaseTask

class ProductViewsOfUserFortySeven(outputFileName: String) extends CaseTask {
  override def register(tableEnv: BatchTableEnvironment, tableName: String): Unit = {
    val productViewsOfUser47 = tableEnv.scan(tableName)
      .where("userId=47 && eventName='view'")
      .select("productId")
      .distinct()

    val sink = new CsvTableSink(path = outputFileName, fieldDelim = Some("|"), numFiles = None, writeMode = Some(FileSystem.WriteMode.OVERWRITE))
    productViewsOfUser47.writeToSink(sink)
  }
}
