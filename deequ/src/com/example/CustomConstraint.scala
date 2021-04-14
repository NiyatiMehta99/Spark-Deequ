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


import com.amazon.deequ.anomalydetection.{AnomalyDetectionStrategy, AnomalyDetector, DataPoint}
import com.amazon.deequ.analyzers.runners.AnalyzerContext
import com.amazon.deequ.analyzers.{Analyzer, Histogram, Patterns, State, KLLParameters}
import com.amazon.deequ.constraints.Constraint._
import com.amazon.deequ.constraints._
import com.amazon.deequ.metrics.{BucketDistribution, Distribution, Metric}
import com.amazon.deequ.repository.MetricsRepository
import org.apache.spark.sql.expressions.UserDefinedFunction
import com.amazon.deequ.anomalydetection.HistoryUtils


case class Check1(
  level: CheckLevel.Value,
  description: String,
   val constraints: Seq[Constraint] = Seq.empty) {


  /**
    * Returns a new Check object with the given constraint added to the constraints list.
    *
    * @param constraint New constraint to be added
    * @return
    */
  def addConstraint(constraint: Constraint): Check = {
    Check(level, description, constraints :+ constraint)
  }

  /** Adds a constraint that can subsequently be replaced with a filtered version */
  private[this] def addFilterableConstraint(
      creationFunc: Option[String] => Constraint)
    : CheckWithLastConstraintFilterable = {

    val constraintWithoutFiltering = creationFunc(None)

    CheckWithLastConstraintFilterable(level, description,
      constraints :+ constraintWithoutFiltering, creationFunc)
  }
  
  def satisfies(
      columnCondition: String,
      constraintName: String,
      assertion: Double => Boolean = Check.IsOne,
      hint: Option[String] = None)
    : CheckWithLastConstraintFilterable = {

    addFilterableConstraint { filter =>
      complianceConstraint(constraintName, columnCondition, assertion, filter, hint)
    }
  }
  
  def isLessThan10(
  column:String,
  assertion:Double=>Boolean=Check.IsOne,
  hint:Option[String]=None
  ):CheckWithLastConstraintFilterable={
    satisfies(s"$column <10", s"$column is less than 10", assertion, hint=hint)
  }
  
}

object CustomConstraint {
  
  val spark = SparkSession.builder()
      .master("local") 
      .appName("SparkSession")
      .getOrCreate()
      
     def main(args: Array[String]): Unit = {
       val data=spark.read
       .option("inferschema", "true")
       .option("header","false")
       .csv("src/resources/example_dataset_3.csv")
       .toDF("id","nz","citi","count","year","bool")
       ;
       
       val verify=VerificationSuite()
       .onData(data).addCheck(
       Check1(CheckLevel.Error,"checks")
       .isLessThan10("id")
       ).run()
       
       val resultdataframe=checkResultsAsDataFrame(spark,verify)
       resultdataframe.show()
  }
}