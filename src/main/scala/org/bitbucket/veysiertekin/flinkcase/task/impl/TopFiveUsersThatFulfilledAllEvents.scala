package org.bitbucket.veysiertekin.flinkcase.task.impl

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.scala._
import org.apache.flink.core.fs.FileSystem
import org.apache.flink.table.api.scala.BatchTableEnvironment
import org.bitbucket.veysiertekin.flinkcase.AnalyseTextFile._
import org.bitbucket.veysiertekin.flinkcase.task.CaseTask

class TopFiveUsersThatFulfilledAllEvents(outputFileName: String) extends CaseTask {
  override def register(tableEnv: BatchTableEnvironment, tableName: String): Unit = {
    val usersByEventInteraction = tableEnv.scan(tableName)
      .select("userId,eventName")

    val value = tableEnv.toDataSet[(Int, String)](usersByEventInteraction)
      .map(row => (row._1, Set(row._2), 1))
      .groupBy(0)
      .reduce {
        (left, right) =>
          val (userId, leftEventTypes, leftEventCounts) = left
          val (_, rightEventTypes, rightEventCounts) = right
          (userId, leftEventTypes ++ rightEventTypes, leftEventCounts + rightEventCounts)
      }
      .filter(element => element._2.size == NUMBER_OF_EVENT_TYPES)
      .sortPartition(2, Order.DESCENDING).setParallelism(1)
      .map(row => row._1)
      .first(5)

    value.writeAsText(filePath = outputFileName, writeMode = FileSystem.WriteMode.OVERWRITE)
  }
}
