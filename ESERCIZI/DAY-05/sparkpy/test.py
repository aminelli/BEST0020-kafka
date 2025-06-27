from pyspark.sql import SparkSession

spark = SparkSession.builder.master("spark://localhost:7077").getOrCreate()
sc = spark.sparkContext

rdd = sc.parallelize(range(100 + 1))
rdd.sum()