package com.example
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{FloatType, IntegerType, LongType, StringType, StructField, StructType}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.DataFrame
object Demo {
  
 //accepts a dataframe, column name and value to be matched and filters acc to that
   def filterByCiti(d:DataFrame,value:String,col:String){
     //using spark functions
//    d.filter(d(col)===value).show();
    
     
     // using spark sql to execute the same
    d.createOrReplaceTempView("people");
    val result=spark.sql("select * from people where people."+col+"="+"'"+value+"'");
    result.show();
  }
  
  val spark = SparkSession.builder()
      .master("local")
      .appName("PsarkSession")
      .getOrCreate()
      
     def main(args: Array[String]): Unit = {
       val data=spark.read
       .option("inferschema", "true")
       .option("header","false")
       .csv("C:/Users/niyati/Desktop/example_dataset.csv");
       
       val df=data.toDF("nz","citi","count","year");
       df.show();
       
       //df.filter(df("citi")==="India").groupBy("nz");
       filterByCiti(df, "Auckland","nz");
  }
  
}