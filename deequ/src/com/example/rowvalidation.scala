package com.example

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, StringType, TimestampType}
import com.amazon.deequ.schema.RowLevelSchema
import com.amazon.deequ.schema.RowLevelSchemaValidator

//import org.scalatest.WordSpec

object rowvalidation {
  
  val spark = SparkSession.builder()
      .master("local") 
      .appName("SparkSession")
      .getOrCreate()
      
      import spark.implicits._ 
      
      def main(args: Array[String]): Unit = {
    
//    val data = Seq(
//        ("123", "Product A", "2012-07-22 22:59:59"),
//        ("N/A", "Product B", null),
//        ("456", null, "2012-07-22 22:59:59"),
//        (null, "Product C", "2012-07-22 22:59:59")
//      ).toDF("id", "name", "event_time")
//      
//      data.show()
//      
//      val schema = RowLevelSchema()
//        .withIntColumn("id", isNullable = false)
//        .withStringColumn("name", maxLength = Some(10))
//        .withTimestampColumn("event_time", mask = "yyyy-MM-dd HH:mm:ss", isNullable = false)
//
//      val result = RowLevelSchemaValidator.validate(data, schema)
//      
//      result.invalidRows.show();
//      println(result.numInvalidRows)
        
    val file="src/resources/example_dataset_3.csv";
    
    val data=spark.read.csv(file).toDF("id","nz","citizen","count","yr","boolean");
    
    val schema=RowLevelSchema()
    .withIntColumn("id", isNullable=false)
    .withStringColumn("nz", isNullable=false)
    .withStringColumn("citizen", isNullable=false)
    .withIntColumn("count", isNullable=false)
    .withIntColumn("yr", isNullable=false)
    .withStringColumn("boolean", isNullable=false)
    
    val result=RowLevelSchemaValidator.validate(data, schema)
    
    result.validRows.show();
    result.invalidRows.show();
      }
}