package com.example

import org.apache.spark.sql.SparkSession
import com.amazon.deequ.analyzers.runners.{AnalysisRunner, AnalyzerContext}
import com.amazon.deequ.analyzers.runners.AnalyzerContext.successMetricsAsDataFrame
import com.amazon.deequ.analyzers.{Compliance, Correlation, Size, Completeness, Mean, ApproxCountDistinct,Sum,CountDistinct,DataType,Maximum,Minimum,Entropy}


object Analyze {
  
  val spark = SparkSession.builder()
      .master("local") 
      .appName("PsarkSession")
      .getOrCreate()
      
     def main(args: Array[String]): Unit = {
       val data=spark.read
       .option("inferschema", "true")
       .option("header","false")
       .csv("src/resources/example_dataset_2.csv");
       
       val df=data.toDF("id","nz","citi","count","year","boolean","star_rating","top_star_rating");
      // df.show();
       
       val analysisResult: AnalyzerContext = { AnalysisRunner
  // data to run the analysis on
    .onData(df)
  // define analyzers that compute metrics
    
  .addAnalyzer(Sum("year"))
    
  .addAnalyzer(Mean("star_rating"))
    
  .addAnalyzer(Size())
  
  .addAnalyzer(Correlation("year","count")) 
  
  //.addAnalyzer(ApproxCountDistinct("count")) 
  
  .addAnalyzer(Completeness("id"))
  
  //.addAnalyzer(CountDistinct("year"))
  .addAnalyzer(DataType("boolean"))
  //.addAnalyzer(Entropy("id")) //no output shown
  
  .addAnalyzer(Compliance("star_rating","top_star_rating>2.5"))
  
//  .addAnalyzer(Maximum("count"))
//  .addAnalyzer(Minimum("count"))
  .run()
  
  
}
       val metrics = successMetricsAsDataFrame(spark, analysisResult);
  metrics.show(false);
       
  }
  
}