package org.bitbucket.veysiertekin.flinkcase

import org.apache.flink.api.scala._
import org.apache.flink.table.api.TableEnvironment
import org.bitbucket.veysiertekin.flinkcase.CaseCsvLoader._
import org.bitbucket.veysiertekin.flinkcase.task.impl._

object AnalyseTextFile {
  // Table name of the csv file will be loaded into table-environment context
  val SOURCE_TABLE_NAME = "CASE_TABLE"
  // Number of event types: view, add, remove, click
  val NUMBER_OF_EVENT_TYPES = 4

  def main(args: Array[String]) {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val tableEnv = TableEnvironment.getTableEnvironment(env)

    loadCaseCsvAsATableSource("data/case.csv", tableEnv, SOURCE_TABLE_NAME)

    Seq(
      new ProductInteractionCounts("data/task-1_product-interactions.csv"),
      new EventCounts("data/task-2_event-counts.csv"),
      new TopFiveUsersThatFulfilledAllEvents("data/task-3_top-five-users-fulfilled-all-events.csv"),
      new EventsOfUserFortySeven("data/task-4_event-counts-of-user-47.csv"),
      new ProductViewsOfUserFortySeven("data/task-5_product-views-of-user-47.csv")
    ).foreach(task => task.register(tableEnv, SOURCE_TABLE_NAME))

    env.execute("AnalyseTextFile")
  }
}
