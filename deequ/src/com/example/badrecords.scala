package com.example

import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.{FloatType, IntegerType, LongType, StringType, StructField, StructType}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.BinaryType
import org.apache.spark.sql.types.BooleanType
import com.amazon.deequ.schema.RowLevelSchemaValidator

object badrecords {
  
  val spark = SparkSession.builder()
      .master("local")
      .appName("sparkSession")
      .getOrCreate()
      
      import spark.implicits._
      
      def main(args: Array[String]): Unit = {
        
      val customSchema=StructType(Array(
          StructField("id",IntegerType,false),
        StructField("nz port",StringType,false),
        StructField("citizenship",StringType,false),
        StructField("count",IntegerType,false),
        StructField("year",IntegerType,false),
        StructField("boolean",BooleanType,false),
        StructField("_corrupt_record", StringType, true)))   
      
      
      
      val file="src/resources/example_dataset_3.csv"
      
      val dataFrame: DataFrame = spark.read
                                .schema(customSchema)
                                .option("header", false)
                                .option("mode", "PERMISSIVE")
                                .csv(file)
                                .cache()
                                
                                dataFrame.filter("_corrupt_record is not NULL")
                                .select("_corrupt_record").show(false);
                                
                              
                                     
      
      }
  
}