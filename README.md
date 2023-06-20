## K-Means Spark

Example of K-Means clustering using [PySpark](https://spark.apache.org/docs/latest/api/python/)

### Dataset

Open food facts dataset contains data about food products from all over the world. It is available
on https://world.openfoodfacts.org/data

Link to csv file: https://static.openfoodfacts.org/data/en.openfoodfacts.org.products.csv.gz

### Clickhouse jar file

Clickhouse jar file is available
on https://github.com/ClickHouse/clickhouse-java/releases/download/v0.4.6/clickhouse-jdbc-0.4.6-all.jar and should be
placed in `jars` directory.

### Datamart jar file

First you need to install [sbt](https://www.scala-sbt.org/) package manager.
Build project using following command:

```shell
bash scripts/build_datamart.sh
```

Jar file will be placed in `datamart/target/<scala_version>/datamart_<scala_version>-0.1.0-SHAPSHOT.jar`
Also this file should be placed in `jars` directory.

### Example usage

```shell
docker-compose up
```

### Project structure

* [K-Means Spark](src/kmeans.py)
* [Preprocessing](src/datamart.py)
* [Launching](src/main.py)
* [Clickhouse and PySpark integration](docker-compose.yml)