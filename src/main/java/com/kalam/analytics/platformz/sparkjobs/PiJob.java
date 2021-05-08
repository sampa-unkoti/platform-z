package com.kalam.analytics.platformz.sparkjobs;
import java.util.*;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.livy.*;

public class PiJob implements Job<Double>, Function<Integer, Integer>,
  Function2<Integer, Integer, Integer> {

  private final int samples;

  public PiJob(int samples) {
    this.samples = samples;
  }

  @Override
  public Double call(JobContext ctx) throws Exception {
    List<Integer> sampleList = new ArrayList<Integer>();
    for (int i = 0; i < samples; i++) {
      sampleList.add(i + 1);
    }
    
 // Creates a DataFrame based on a table named "people"
 // stored in a MySQL database.
    SparkSession spark = SparkSession
    	      .builder()
    	      .appName("Java Spark SQL user-defined Datasets aggregation example")
    	      .getOrCreate();
 String url =
   "jdbc:mysql://yourIP:yourPort/test?user=yourUsername;password=yourPassword";
 Dataset df = spark
   .read()
   .format("jdbc")
   .option("url", url)
   .option("dbtable", "people")
   .load();

 // Looks the schema of this DataFrame.
 df.printSchema();

 // Counts people by age
 Dataset countsByAge = df.groupBy("age").count();
 countsByAge.show();

 // Saves countsByAge to S3 in the JSON format.
 countsByAge.write().format("json").save("s3a://...");
 
 Dataset<Row> peopleDFCsv = spark.read().format("csv")
		  .option("sep", ";")
		  .option("inferSchema", "true")
		  .option("header", "true")
		  .load("examples/src/main/resources/people.csv");
 
 Dataset<Row> peopleDF =
		  spark.read().format("json").load("examples/src/main/resources/people.json");
		peopleDF.select("name", "age").write().format("parquet").save("namesAndAges.parquet");
		
		Dataset<Row> sqlDF =
				  spark.sql("SELECT * FROM parquet.`examples/src/main/resources/users.parquet`");
		
		// Note: JDBC loading and saving can be achieved via either the load/save or jdbc methods
		// Loading data from a JDBC source
		Dataset<Row> jdbcDF = spark.read()
		  .format("jdbc")
		  .option("url", "jdbc:postgresql:dbserver")
		  .option("dbtable", "schema.tablename")
		  .option("user", "username")
		  .option("password", "password")
		  .load();

		Properties connectionProperties = new Properties();
		connectionProperties.put("user", "username");
		connectionProperties.put("password", "password");
		Dataset<Row> jdbcDF2 = spark.read()
		  .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties);

		// Saving data to a JDBC source
		jdbcDF.write()
		  .format("jdbc")
		  .option("url", "jdbc:postgresql:dbserver")
		  .option("dbtable", "schema.tablename")
		  .option("user", "username")
		  .option("password", "password")
		  .save();

		jdbcDF2.write()
		  .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties);

		// Specifying create table column data types on write
		jdbcDF.write()
		  .option("createTableColumnTypes", "name CHAR(64), comments VARCHAR(1024)")
		  .jdbc("jdbc:postgresql:dbserver", "schema.tablename", connectionProperties);

    return 4.0d * ctx.sc().parallelize(sampleList).map(this).reduce(this) / samples;
  }

  @Override
  public Integer call(Integer v1) {
    double x = Math.random();
    double y = Math.random();
    return (x*x + y*y < 1) ? 1 : 0;
  }

  @Override
  public Integer call(Integer v1, Integer v2) {
    return v1 + v2;
  }

}
