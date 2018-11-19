package org.bitbucket.veysiertekin.flinkcase

import org.apache.flink.api.scala._
import org.apache.flink.table.api.TableEnvironment
import org.bitbucket.veysiertekin.flinkcase.CaseCsvLoader._
import org.bitbucket.veysiertekin.flinkcase.configuration.ArgsConfig
import org.bitbucket.veysiertekin.flinkcase.configuration.EnvConstants._
import org.bitbucket.veysiertekin.flinkcase.task.impl._


object AnalyseTextFile {
  def main(args: Array[String]) {
    val config = new ArgsConfig(args)

    val env = ExecutionEnvironment.getExecutionEnvironment
    val tableEnv = TableEnvironment.getTableEnvironment(env)

    loadCaseCsvAsATableSource(config.csvFile, tableEnv, SOURCE_TABLE_NAME)

    Seq(
      new UniqueProductViews(config.outputPath + "/task-1_unique-product-views.csv"),
      new UniqueEventCounts(config.outputPath + "/task-2_unique-event-counts.csv"),
      new TopFiveUsersThatFulfilledAllEvents(config.outputPath + "/task-3_top-five-users-fulfilled-all-events.csv"),
      new EventsOfUserFortySeven(config.outputPath + "/task-4_event-counts-of-user-47.csv"),
      new ProductViewsOfUserFortySeven(config.outputPath + "/task-5_product-views-of-user-47.csv")
    ).foreach(task => task.register(tableEnv, SOURCE_TABLE_NAME))

    env.execute("AnalyseTextFile")
  }
}
