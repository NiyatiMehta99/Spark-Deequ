package com.example


import com.amazon.deequ.analyzers.runners.{AnalysisRunner, AnalyzerContext}
import com.amazon.deequ.analyzers.runners.AnalyzerContext.successMetricsAsDataFrame
import com.amazon.deequ.analyzers.{ApproxCountDistinct, Completeness, Compliance, Correlation, Mean, Size}
import org.apache.spark.sql.SparkSession


object Performance {
  def main(args: Array[String]): Unit = {
      val spark = SparkSession.builder()
        .master("local")
        .appName("Analyzer")
        .getOrCreate()

      spark.sparkContext.setLogLevel("ERROR")


      //  val file = "src/main/resources/sample_aws.tsv"
      val file = "src/resources/Hr1m.csv"

      // data to run the analysis on
      val df = spark.read
        .option("header", "true")
        .option("inferschema", "true")
        .csv(file)

      val analysisResult: AnalyzerContext = { AnalysisRunner
      .onData(df)
      // define analyzers that compute metrics
      .addAnalyzer(Size())
      .addAnalyzer(Completeness("Emp ID"))
      .addAnalyzer(ApproxCountDistinct("Emp ID"))
      .addAnalyzer(Mean("Age_in_Yrs"))
      .addAnalyzer(Compliance("Emp Age 18+", "Age_in_Yrs > 18.0",None))
      .addAnalyzer(Correlation("Age_in_Yrs", "Weight in Kgs"))
      .addAnalyzer(Correlation("Age in Company (Years)", "Salary"))
      // compute metrics
      .run()
    }
    // retrieve successfully computed metrics as a Spark data frame

    analysisResult.metricMap.foreach { case (analyzer, metric) =>
      println(s"\t$analyzer: ${metric.value.get}")
    }

    /*val metrics = successMetricsAsDataFrame(spark, analysisResult)
    metrics.show(truncate = false)*/


  }
}

