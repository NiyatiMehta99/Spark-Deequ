package com.example

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{FloatType, IntegerType, LongType, StringType, StructField, StructType}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.DataFrame

object inferschema {
  
  val spark = SparkSession.builder()
      .master("local")
      .appName("sparkSession")
      .getOrCreate()
      
    def main(args: Array[String]): Unit = {
      val data=spark.read
      .option("inferschema", "true")
      .option("header", "true")
      .csv("src/resources/example_dataset_2.csv");
      
      val df=data.toDF();
      
      df.printSchema();
    }
  
}