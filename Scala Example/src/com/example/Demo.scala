package example

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.{FloatType, IntegerType, LongType, StringType, StructField, StructType}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.DataFrame

object Demo {
  
  val spark = SparkSession.builder()
      .master("local")
      .appName("sparkSession")
      .getOrCreate()
      
      def main(args: Array[String]): Unit = {
        
      val customSchema=StructType(Array(
        StructField("nz port",IntegerType,false),
        StructField("citizenship",StringType,false),
        StructField("count",IntegerType,false),
        StructField("year",IntegerType,false)
      )
      )    
      
      val data=spark.read
      .schema(customSchema)
      .option("mode", "FAILFAST")
      .option("inferschema", "true")
      .option("header", "false")
      .csv("C:/Users/niyati/Desktop/example_dataset.csv");
      
      val df=data.toDF();
      df.show();
      
      
      }
  
}  