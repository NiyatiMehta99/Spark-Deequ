package com.example


import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.{FloatType, IntegerType, LongType, StringType, StructField, StructType, BinaryType,BooleanType}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.DateType

object perftestSpark {
  
  val spark = SparkSession.builder()
      .master("local")
      .appName("sparkSession")
      .getOrCreate()
      
      import spark.implicits._
      
      def main(args: Array[String]): Unit = {
        
      val customSchema=StructType(Array(
        StructField("Emp Id",IntegerType,false),
        StructField("Name Prefix",StringType,false),
        StructField("First Name",StringType,false),
        StructField("Middle Initial",StringType,false),
        StructField("Last Name",StringType,false),
        StructField("Gender",StringType,false),
        StructField("E Mail",StringType,false),
        StructField("Age in Yrs",FloatType,false),
        StructField("Weight in Kgs",IntegerType,false),
        StructField("Salary",IntegerType,false),
        StructField("_corrupt_record", StringType, true)
        ))  
        
        val data:DataFrame=spark.read
        .schema(customSchema)
        .option("header",true)
        .option("mode","PERMISSIVE") //makes values null
      // .option("mode","DROPMALFORMED") //drops the rows
        //.option("mode","FAILFAST") //throws error
        .csv("src/resources/5000 Records.csv")
        .cache();
      
      data.filter("_corrupt_record is not NULL")
                                .select("_corrupt_record").show(false)    
      
   //   data.show(false);
  }
  
}