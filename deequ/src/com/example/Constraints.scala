package com.example


import scala.util.matching.Regex
import com.amazon.deequ.{VerificationResult, VerificationSuite}
import com.amazon.deequ.VerificationResult.checkResultsAsDataFrame
import com.amazon.deequ.checks.{Check, CheckLevel}
import com.amazon.deequ.constraints.ConstrainableDataTypes
import org.apache.spark.sql.{SaveMode, SparkSession}

object Constraints {

  def main(args: Array[String]): Unit = {


    val spark = SparkSession.builder()
      .master("local")
      .appName("Creating Spark Context with Spark Session")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")


    val file = "src/resources/Hr1m.csv"

    val df = spark.read
      .option("header", "true")
      .option("inferschema","true")
      .csv(file)


    val _check = Check(CheckLevel.Warning, "Data Validation Check")
      .hasSize(_ == 1000000)
      .isComplete("Father's Name")
      .isComplete("Mother's Name")
      .isComplete("County")
      .isComplete("User Name")
      .isComplete("Password")
      .isComplete("Zip")
      .isComplete("City")
      .isContainedIn("DOW of Joining", Array("Wednesday","Sunday","Friday","Saturday","Thursday","Monday","Tuesday"))
      .isComplete("Emp ID")
      .isUnique("Emp ID")
      .isNonNegative("Age_in_Yrs")
      .isContainedIn("Quarter of Joining", Array("Q1","Q2","Q3","Q4"))
      .isContainedIn("Half of Joining", Array("H1","H2"))
      .hasMax("Month of Joining", _ == 12)
      .isNonNegative("Weight_in_Kgs")
      .isComplete("First Name")
      .isComplete("Month Name of Joining")
      .isContainedIn("Month Name of Joining", Array("August", "July", "January", "April", "December", "November", "February", "March", "June", "September", "May", "October"))
      .isComplete("Phone_no")
      .containsEmail("E Mail")
      .containsSocialSecurityNumber("SSN")
      .isContainedIn("Day of Joining", 1, 31, includeLowerBound = true, includeUpperBound = true)
      .hasPattern("Phone_no", """^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\s\./0-9]*$""".r)
      .hasDataType("Emp ID", ConstrainableDataTypes.Integral)

    val verificationResult: VerificationResult = {
      VerificationSuite()
        .onData(df)
        .addCheck(_check)
        .run()
    }
    val resultDataFrame = checkResultsAsDataFrame(spark, verificationResult)

    resultDataFrame.show();
  }
}
