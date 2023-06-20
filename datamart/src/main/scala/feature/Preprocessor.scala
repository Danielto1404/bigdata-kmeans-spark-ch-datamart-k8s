package feature

import org.apache.spark.ml.feature.{MinMaxScaler, VectorAssembler}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.col


object Preprocessor {
  def toFloat(df: DataFrame): DataFrame = {
    val expressions = Columns.features.map(c => col(c).cast("float"))
    df.select(expressions: _*)
  }

  def fillNull(df: DataFrame): DataFrame = df.na.fill(0.0)

  def toVector(df: DataFrame): DataFrame = {
    val assembler = new VectorAssembler()
      .setInputCols(df.columns)
      .setOutputCol("vectorFeatures")
      .setHandleInvalid("error")
    assembler.transform(df)
  }

  def scale(df: DataFrame): DataFrame = {
    val scaler = new MinMaxScaler()
      .setInputCol("vectorFeatures")
      .setInputCol("features")
    val model = scaler.fit(df)
    model.transform(df)
  }
}
