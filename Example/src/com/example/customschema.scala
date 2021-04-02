package com.example
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.{FloatType, IntegerType, LongType, StringType, StructField, StructType}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.BinaryType
import org.apache.spark.sql.types.BooleanType

object customschema {
  
  val spark = SparkSession.builder()
      .master("local")
      .appName("sparkSession")
      .getOrCreate()
      
      def main(args: Array[String]): Unit = {
        
      val customSchema=StructType(Array(
        StructField("nz port",StringType,nullable=false),
        StructField("citizenship",StringType,false),
        StructField("count",IntegerType,false),
        StructField("year",IntegerType,false),
        StructField("boolean",BooleanType,false),
       // StructField("binary",BinaryType,false) 
        
        /*binary is not datatype of scala, therefore it won't print it's values
         * But can be included in the schema, and will accept in spark and print as datatype when we do printSchema function
         */
      )
      )   
      
      val datatext=spark.sparkContext.textFile("C:/Users/niyati/Desktop/example_dataset_2.csv")
      
      val data=spark.read
      .schema(customSchema)
      .option("mode", "PERMISSIVE")
      .option("inferschema", "true")
      .option("header", "false")
      .csv("C:/Users/niyati/Desktop/example_dataset_2.csv");
      
      val df=data.toDF();
      df.printSchema();
      df.show();     
      }
  
}