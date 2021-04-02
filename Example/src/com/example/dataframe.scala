package com.example
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.{FloatType, IntegerType, LongType, StringType, StructField, StructType}
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.types.BinaryType
import org.apache.spark.sql.types.BooleanType

object dataframe {
  
  val spark = SparkSession.builder()
      .master("local")
      .appName("sparkSession")
      .getOrCreate()
      
      def main(args: Array[String]): Unit = {
        
      val customSchema=StructType(Array(
          StructField("id",IntegerType,false),
        StructField("nz port",StringType,nullable=false),
        StructField("citizenship",StringType,false),
        StructField("count",IntegerType,false),
        StructField("year",IntegerType,false),
        StructField("boolean",BooleanType,false),
       // StructField("binary",BinaryType,false)
      )
      )   
      
      val file="C:/Users/niyati/Desktop/example_dataset_2.csv"
      
      val data1=spark.read
      .schema(customSchema)
      .option("mode", "DROPMALFORMED")
      .option("inferschema", "true")
      .option("header", "false")
      .csv(file);
      
       val data2=spark.read
      //.schema(customSchema)
      //.option("mode", "DROPMALFORMED")
      .option("inferschema", "true")
      .option("header", "false")
      .csv(file);
      
      val df1=data1.toDF();
      val df2=data2.toDF("id","nz","citizen","cnt","yr","bool");
      val df3=df2.join(df1,Seq("id"),"left_outer");
      val df4= df3.filter(df3("nz port").isNull);
      df4.show(false);
      //df2.filter(df2("nz")===df1("nz port")).show();
     // df3.filter(df3("nz")=!=df3("nz port")).show();
      //df3("nz port").contains("null");
      
      
      //val df4=
      //  df2.join(df1,df1("nz port")<=>df2("nz")).show();
  }
}