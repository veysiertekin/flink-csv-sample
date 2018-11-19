package org.bitbucket.veysiertekin.flinkcase

import org.apache.flink.table.api.Types
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.apache.flink.table.sources.CsvTableSource

object CaseCsvLoader {
  def loadCaseCsvAsATableSource(inputFilePath: String, tableEnv: BatchTableEnvironment, tableName: String): Unit = {
    val caseCsvData = CsvTableSource
      .builder()
      .path(inputFilePath)
      .ignoreFirstLine()
      .fieldDelimiter("|")
      .field("date", Types.INT)
      .field("productId", Types.INT)
      .field("eventName", Types.STRING)
      .field("userId", Types.INT)
      .build()
    tableEnv.registerTableSource(tableName, caseCsvData)
  }
}
