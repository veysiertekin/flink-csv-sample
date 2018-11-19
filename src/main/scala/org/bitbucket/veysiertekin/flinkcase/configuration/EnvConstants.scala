package org.bitbucket.veysiertekin.flinkcase.configuration

object EnvConstants {
  // Table name of the csv file will be loaded into table-environment context
  val SOURCE_TABLE_NAME = "CASE_TABLE"

  // Number of event types: view, add, remove, click
  val NUMBER_OF_EVENT_TYPES = 4
}
