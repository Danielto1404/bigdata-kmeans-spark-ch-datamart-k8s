package clickhouse

import org.apache.spark.sql.DataFrame

case class Writer(url: String, user: String, password: String, driver: String, table: String) {
  def write(df: DataFrame): Unit = {
    df.write
      .format("jdbc")
      .mode("append")
      .option("url", url)
      .option("user", user)
      .option("password", password)
      .option("dbtable", table)
      .option("driver", driver)
      .save()
  }
}
