from pyspark.sql import SparkSession
from pyspark.sql.types import StructType, StructField, StringType, IntegerType
from pyspark.sql.functions import col, when, from_json
# \
    #.config("spark.sql.adaptive.enabled", "true") \
    #.config("spark.sql.adaptive.coalescePartitions.enabled", "true") \

    
spark = SparkSession.builder \
    .master("spark://spark-master:7077") \
    .appName("OrderKafkaProcessor") \
    .config("spark.jars.packages","org.apache.spark:spark-sql-kafka-0-10_2.13:4.0.0,org.apache.kafka:kafka-clients:4.0.0") \
    .getOrCreate()

spark.sparkContext .setLogLevel("WARN")

sc = spark.sparkContext

subscribeTopic = "ORDERS"

#dfOrders = (
#    spark.readStream.format("kafka")
#    .option("kafka.bootstrap.servers","broker01:29092,broker02:29092,broker03:29092")
#    .option("subscribe", subscribeTopic)
#    .option("startingOffsets","earliest")    
#    .load()
#)

df = spark.readStream.format("kafka") \
    .option("kafka.bootstrap.servers","broker01:29092,broker02:29092,broker03:29092") \
    .option("subscribe", subscribeTopic) \
    .option("startingOffsets","earliest") \
    .load()

# df_parsed = df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)")

schema = StructType([
    StructField("ordertime", StringType()),
    StructField("orderid",  StringType()),
    StructField("itemid",  StringType()),
    StructField("city",  StringType()),
])
 
dfOrdersTest = df \
        .withColumn("json_data", from_json( col("value").cast("string"), schema)) \
        .withColumn("ordertime",  col("json_data.ordertime").cast("string")) \
        .withColumn("orderid",  col("json_data.orderid").cast("string")) \
        .withColumn("itemid",  col("json_data.itemid").cast("string")) \
        .withColumn("address.city",  col("json_data.city").cast("string")) \
        .drop(col("address"))


query = dfOrdersTest.writeStream \
    .format("console") \
    .outputMode("append") \
    .start()

query.awaitTermination()





# dfOrdersTest = (
#     dfOrders
#         .withColumn("json_data", from_json( col("value").cast("string"), schema))
#         .withColumn("ordertime",  col("json_data.ordertime").cast("string"))
#         .withColumn("orderid",  col("json_data.orderid").cast("string"))
#         .withColumn("itemid",  col("json_data.itemid").cast("string"))
#         .withColumn("address.city",  col("json_data.city").cast("string"))
#         .drop(col("address"))
# )

 