package com.example

import org.apache.spark.sql.SparkSession
import com.amazon.deequ.suggestions.{ConstraintSuggestionRunner, Rules}



object constraintsuggestions {
  
  val spark = SparkSession.builder()
      .master("local") 
      .appName("PsarkSession")
      .getOrCreate()
      
      import spark.implicits._ // for toDS method
      
     def main(args: Array[String]): Unit = {
       val data=spark.read
       .option("inferschema", "true")
       .option("header","false")
       .csv("src/resources/example_dataset_2.csv");
       
       val df=data.toDF("id","nz","citi","count","year","boolean","star_rating","top_star_rating");
       
       // We ask deequ to compute constraint suggestions for us on the data
        val suggestionResult = { ConstraintSuggestionRunner()
      // data to suggest constraints for
        .onData(df)
      // default set of rules for constraint suggestion
        .addConstraintRules(Rules.DEFAULT)
      // run data profiling and constraint suggestion
        .run()
}
        
        val suggestionDataFrame = suggestionResult.constraintSuggestions.flatMap { 
  case (column, suggestions) => 
    suggestions.map { constraint =>
      (column, constraint.description, constraint.codeForConstraint)
    } 
}.toSeq.toDF("column","description","suggestion code")

suggestionDataFrame.show(false);
        
// suggestionResult.constraintSuggestions.foreach { case (column, suggestions) =>
//  suggestions.foreach { suggestion =>
//    println(s"Constraint suggestion for '$column':\t${suggestion.description}\n" +
//      s"The corresponding scala code is ${suggestion.codeForConstraint}\n")
//  }
}
 
 //println(suggestionResult.columnProfiles.toSeq)
       

  
}