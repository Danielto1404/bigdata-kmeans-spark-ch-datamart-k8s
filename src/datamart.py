from pyspark.sql import SparkSession, SQLContext, DataFrame


class DataMart:
    def __init__(self, spark: SparkSession):
        self.spark_context = spark.sparkContext
        self.sql_context = SQLContext(self.spark_context, spark)
        self._datamart = self.spark_context._jvm.DataMart

    def load_dataset(self) -> DataFrame:
        jvm_data = self._datamart.readTransformOpenFoodDataset()
        return DataFrame(jvm_data, self.sql_context)

    def write_predictions(self, df: DataFrame):
        self._datamart.writeClickhouse(df._jdf)
