package com.example
import org.apache.spark.sql.SparkSession
import com.amazon.deequ.profiles.ColumnProfilerRunner

object data_profiling {
  
  val spark = SparkSession.builder()
      .master("local") 
      .appName("SparkSession")
      .getOrCreate()
      
      import spark.implicits._ 
      
      def main(args: Array[String]): Unit = {
    
    val data=spark.read
       .option("inferschema", "true")
       .option("header","false")
       .csv("src/resources/example_dataset_3.csv");
       
       val df=data.toDF("id","nz","citi","count","year","boolean");
       
       val result=ColumnProfilerRunner()
       .onData(df)
       .run();
       
       result.profiles.foreach { case (colName, profile) =>
  println(s"Column '$colName':\n " +
    s"\tcompleteness: ${profile.completeness}\n" +
    s"\tapproximate number of distinct values: ${profile.approximateNumDistinctValues}\n" +
    s"\tdatatype: ${profile.dataType}\n")
}
        
      }
  
}