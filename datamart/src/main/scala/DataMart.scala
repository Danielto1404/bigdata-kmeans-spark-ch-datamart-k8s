import clickhouse.{Reader, Writer}
import feature.Preprocessor
import org.apache.spark.sql.{DataFrame, SparkSession}

object DataMart {
  private val CLICKHOUSE_URL = "jdbc:clickhouse://clickhouse"
  private val USER = "default"
  private val PASSWORD = ""
  private val DRIVER = "com.clickhouse.jdbc.ClickHouseDriver"
  private val OPEN_FOOD_TABLE = "datasets.openfood"
  private val OPEN_FOOD_PREDICTIONS_TABLE = "datasets.openfood_predictions"

  private def readOpenFoodDataset(): DataFrame = {
    implicit val spark: SparkSession = SparkSession.builder.getOrCreate()
    val reader = Reader(CLICKHOUSE_URL, USER, PASSWORD, DRIVER, OPEN_FOOD_TABLE)
    reader.read(spark)
  }

  def readTransformOpenFoodDataset(): DataFrame = {
    val rawDataset = readOpenFoodDataset()

    val transforms: Seq[DataFrame => DataFrame] = Seq(
      Preprocessor.toFloat,
      Preprocessor.fillNull,
      Preprocessor.toVector,
      Preprocessor.scale
    )

    val transformed = transforms.foldLeft(rawDataset) { (df, f) => f(df) }

    transformed
  }

  def writeClickhouse(df: DataFrame): Unit = {
    val writer = Writer(CLICKHOUSE_URL, USER, PASSWORD, DRIVER, OPEN_FOOD_PREDICTIONS_TABLE)
    writer.write(df)
  }
}
