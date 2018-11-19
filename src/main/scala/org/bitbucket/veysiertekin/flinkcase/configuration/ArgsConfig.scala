package org.bitbucket.veysiertekin.flinkcase.configuration

class ArgsConfig(arguments: Array[String]) {
  var csvFile = ""
  var outputPath = ""
  arguments.sliding(2, 2).toList.collect {
    case Array("--csvFile", argCsvFile: String) => csvFile = argCsvFile
    case Array("--outputPath", argOutputPath: String) => outputPath = argOutputPath
  }
  if (csvFile == "") throw new IllegalArgumentException("Invalid 'csvFile' parameter!")
  if (outputPath == "") throw new IllegalArgumentException("Invalid 'outputPath' parameter!")
}
