package com.example

import org.apache.spark.sql.SparkSession
import com.amazon.deequ.VerificationSuite
import com.amazon.deequ.checks.{Check, CheckLevel, CheckStatus}
import com.amazon.deequ.constraints.ConstraintStatus
import com.amazon.deequ.VerificationResult.checkResultsAsDataFrame
import org.apache.spark.sql.types.IntegerType
import com.amazon.deequ.constraints.ConstrainableDataTypes.{Numeric}
import com.amazon.deequ.checks.CheckWithLastConstraintFilterable
import com.amazon.deequ.constraints.Constraint._
import com.amazon.deequ.constraints._

object Demo {
  
  val spark = SparkSession.builder()
      .master("local") 
      .appName("SparkSession")
      .getOrCreate()
      
     def main(args: Array[String]): Unit = {
       val data=spark.read
       .option("inferschema", "true")
       .option("header","false")
       .csv("src/resources/example_dataset_3.csv");
       
       val df=data.toDF("id","nz","citi","count","year","boolean");
      // df.show();
       
       val verify=VerificationSuite()
       .onData(df).addCheck(
       Check(CheckLevel.Error,"checks")
//       .hasSize(_==5)
//       .isUnique("id")
//       .isComplete("year")
//       .isGreaterThan("boolean", "1")
//       .isContainedIn("boolean",Array("0","1"))
//       .isNonNegative("count")
//       .hasDataType("count",Numeric) //if it has a string--shows 0.8 failure
       .areComplete(Array("nz","citi"))
       
       ).run()
              
       val verificationResultDf = checkResultsAsDataFrame(spark, verify)
verificationResultDf.show()
       
    }
}
