import argparse

from pyspark.sql import SparkSession

from datamart import DataMart
from kmeans import KmeansParams, PySparkKMeans

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--table_name", default="datasets.openfood")
    parser.add_argument("--columns_json_path", default="config/columns.json")
    parser.add_argument("--save_path", default="models/kmeans.model")
    parser.add_argument("--k", default=2, type=int)
    parser.add_argument("--max_iter", default=8, type=int)
    parser.add_argument("--distance_measure", default="euclidean")
    parser.add_argument("--tol", default=1e-4, type=float)
    parser.add_argument("--seed", default=42, type=int)
    parser.add_argument("--driver_cores", default=4, type=int),
    parser.add_argument("--driver_memory", default="8g"),
    parser.add_argument("--executor_memory", default="16g"),
    parser.add_argument("--clickhouse_jdbc_path", default="jars/clickhouse-jdbc-0.4.6-all.jar")
    parser.add_argument("--datamart_jar_path", default="jars/datamart.jar")
    args = parser.parse_args()

    print("Initializing SparkSession...")
    app = SparkSession.builder \
        .appName("openfood-kmeans") \
        .master("local[*]") \
        .config("spark.driver.cores", args.driver_cores) \
        .config("spark.driver.memory", args.driver_memory) \
        .config("spark.executor.memory", args.executor_memory) \
        .config("spark.driver.extraClassPath", args.clickhouse_jdbc_path) \
        .config("spark.jars", args.datamart_jar_path) \
        .getOrCreate()

    print("Loading data...")
    datamart = DataMart(spark=app)
    df = datamart.load_dataset()

    params = KmeansParams(
        k=args.k,
        max_iter=args.max_iter,
        distance_measure=args.distance_measure,
        tol=args.tol,
        seed=args.seed
    )

    print("Training model...")
    model = PySparkKMeans(params).fit(df)
    model.save(args.save_path)

    print("Writing predictions...")
    predictions = model.predict(df).select("prediction")
    datamart.write_predictions(predictions)