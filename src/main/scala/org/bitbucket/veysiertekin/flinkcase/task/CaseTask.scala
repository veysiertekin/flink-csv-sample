package org.bitbucket.veysiertekin.flinkcase.task

import org.apache.flink.table.api.scala.BatchTableEnvironment

trait CaseTask {
  def register(tableEnv: BatchTableEnvironment, tableName: String)
}
