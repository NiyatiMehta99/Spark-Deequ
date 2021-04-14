package com.example
import java.io.File
import org.apache.spark.sql.SparkSession
import com.amazon.deequ.VerificationSuite
import com.amazon.deequ.checks.{Check, CheckLevel, CheckStatus}
import com.amazon.deequ.constraints.ConstraintStatus
import com.amazon.deequ.VerificationResult.checkResultsAsDataFrame
import org.apache.spark.sql.types.IntegerType
import com.amazon.deequ.examples.ExampleUtils
import com.amazon.deequ.VerificationSuite
import com.amazon.deequ.analyzers.Completeness
import com.amazon.deequ.checks.{Check, CheckLevel}
import com.amazon.deequ.repository.fs.FileSystemMetricsRepository
import com.amazon.deequ.repository.{MetricsRepository, ResultKey}
import com.google.common.io.Files

object MetricsRepo {
  
  def main(args: Array[String]): Unit = {
    
    val spark = SparkSession.builder()
      .master("local") 
      .appName("SparkSession")
      .getOrCreate()
      
      import spark.implicits._
    
val data = Seq(
        ("123", "Product A", "2012-07-22 22:59:59"),
        ("N/A", "Product B", null),
        ("456", null, "2012-07-22 22:59:59"),
        (null, "Product C", "2012-07-22 22:59:59")
      ).toDF("id", "name", "event_time")
      
      data.show()
      
    val metricsfile=new File(Files.createTempDir(),"metrics.json")
    
    val repository:MetricsRepository=FileSystemMetricsRepository(spark,metricsfile.getAbsolutePath)
    
    val resultKey = ResultKey(System.currentTimeMillis(), Map("tag" -> "repositoryExample"))
    
    VerificationSuite().onData(data)
    .addCheck(Check(CheckLevel.Error,"integrity")
    .hasSize(_==3)
    .isComplete("id")
    .isPrimaryKey("event_time"))
    .useRepository(repository)
    .saveOrAppendResult(resultKey)
    .run()
    
    repository.load()
      .withTagValues(Map("tag" -> "repositoryExample"))
      .getSuccessMetricsAsDataFrame(spark)
      .show()
    
    println(metricsfile.getAbsolutePath)
    
    val json = repository.load()
  .after(System.currentTimeMillis() - 10000)
  .getSuccessMetricsAsJson()

  println(json)
  }
  
  
  
}