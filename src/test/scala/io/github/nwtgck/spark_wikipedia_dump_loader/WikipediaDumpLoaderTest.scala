package io.github.nwtgck.spark_wikipedia_dump_loader

import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.FunSuite

class WikipediaDumpLoaderTest extends FunSuite {

  val sparkSession: SparkSession = SparkSession
    .builder()
    .appName("Wikipedia Dump Loader Test [Spark session]")
    .master("local[*]")
    .config("spark.executor.memory", "1g")
    .getOrCreate()

  val classLoader: ClassLoader = this.getClass.getClassLoader

  // (from: https://github.com/mindfulmachines/wiki-parser/blob/6a4a49caabd544eda01259c739468075d60aae1b/src/test/scala/wiki/ParserTest.scala)
  test("testPage's count"){
    val pageDs: Dataset[Page] = WikipediaDumpLoader.readXmlFilePath(
      sparkSession,
      classLoader.getResource("wikidump.xml").getPath
    )

    assert(pageDs.count() == 85)
  }

  // (from: https://github.com/mindfulmachines/wiki-parser/blob/6a4a49caabd544eda01259c739468075d60aae1b/src/test/scala/wiki/ParserTest.scala)
  test("testRedirects"){
    val pageDs: Dataset[Page] = WikipediaDumpLoader.readXmlFilePath(
      sparkSession,
      classLoader.getResource("wikidump.xml").getPath
    )

    assert(pageDs.filter(_.redirectOpt.isDefined).count() == 67)
  }
}
