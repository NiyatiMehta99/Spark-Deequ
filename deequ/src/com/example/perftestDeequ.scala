package com.example

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{IntegerType, StringType, TimestampType}
import com.amazon.deequ.schema.RowLevelSchema
import com.amazon.deequ.schema.RowLevelSchemaValidator

object perftestDeequ {
  
      
      def main(args: Array[String]): Unit = {
        
        val spark = SparkSession.builder()
      .master("local") 
      .appName("SparkSession")
      .getOrCreate()
      
      import spark.implicits._ 
    
    val schema=RowLevelSchema()
    .withIntColumn("Emp Id", isNullable=false)
    .withStringColumn("Name Prefix", isNullable=false,matches=Some("[a-zA-Z]+[.]$"))
    .withStringColumn("First Name", isNullable=false, matches=Some("[a-z,A-Z]+"))
    .withStringColumn("Middle Initial", isNullable=false,maxLength=Some(1), matches=Some("[a-z,A-Z]"))
    .withStringColumn("Last Name", isNullable=false,matches=Some("[a-z,A-Z]+"))
    .withStringColumn("Gender", isNullable=false,maxLength=Some(1),matches=Some("[a-z,A-Z]"))
    .withStringColumn("E Mail", isNullable=false, matches=Some("[@]"))
    .withDecimalColumn("Age in Yrs",precision=10,scale=2, isNullable=false)
    .withIntColumn("Weight in Kgs", isNullable=false,minValue=Some(30))
    .withIntColumn("Salary", isNullable=false,minValue=Some(30000))
     
    
    val data=spark.read
    .option("header",true)
    .csv("src/resources/5000 Records.csv")
    
    
    val result=RowLevelSchemaValidator.validate(data, schema);
    println("The number of invalid rows are "+result.numInvalidRows)
    println("The number of valid rows are "+result.numValidRows)
    result.invalidRows.show(false);
  }
}