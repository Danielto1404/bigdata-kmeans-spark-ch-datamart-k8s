package clickhouse

import org.apache.spark.sql.{DataFrame, SparkSession}


case class Reader(url: String, user: String, password: String, driver: String, table: String, numPartitions: Int = 20) {
  def read(spark: SparkSession): DataFrame = {
    spark.read
      .format("jbdc")
      .option("url", url)
      .option("user", user)
      .option("password", password)
      .option("dbtable", table)
      .option("driver", driver)
      .option("numPartitions", numPartitions)
      .load()
  }
}
