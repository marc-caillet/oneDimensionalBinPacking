package org.mc.optimisation

/**
  * Created by marc on 28/02/2018.
  */
object OneDimensionalBinPackingApp extends App {

  args.length match {

    case 2 =>

      val items: String = args(0)
      val capacity: Int = args(1).toInt

      println(s"Articles : ${args(0)}")
      println(s"Capacité : ${args(1)}")
      println(s"Borne inférieure du nombre de cartons : ${OneDimensionalBinPackingSolver.lowerBound(items, capacity)}")

      val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)
      println(s"Robot glouton naïf : ${OneDimensionalBinPackingSolver.prettyPrintBins(naiveBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.numBins(naiveBins)} cartons utilisés")

      val ffBins = OneDimensionalBinPackingSolver.ff(items, capacity)
      println(s"Robot glouton (First Fit) : ${OneDimensionalBinPackingSolver.prettyPrintBins(ffBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.numBins(ffBins)} cartons utilisés")

      val ffdBins = OneDimensionalBinPackingSolver.ffd(items, capacity)
      println(s"Robot glouton (First Fit Decreasing) : ${OneDimensionalBinPackingSolver.prettyPrintBins(ffdBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.numBins(ffdBins)} cartons utilisés")

      val bfBins = OneDimensionalBinPackingSolver.bf(items, capacity)
      println(s"Robot glouton (Best Fit) : ${OneDimensionalBinPackingSolver.prettyPrintBins(bfBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.numBins(bfBins)} cartons utilisés")

      val bfdBins = OneDimensionalBinPackingSolver.bfd(items, capacity)
      println(s"Robot glouton (Best Fit Decreasing) : ${OneDimensionalBinPackingSolver.prettyPrintBins(bfdBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.numBins(bfdBins)} cartons utilisés")

      val wfBins = OneDimensionalBinPackingSolver.wf(items, capacity)
      println(s"Robot glouton (Worst Fit) : ${OneDimensionalBinPackingSolver.prettyPrintBins(wfBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.numBins(wfBins)} cartons utilisés")

      val wfdBins = OneDimensionalBinPackingSolver.wfd(items, capacity)
      println(s"Robot glouton (Worst Fit Decreasing) : ${OneDimensionalBinPackingSolver.prettyPrintBins(wfdBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.numBins(wfdBins)} cartons utilisés")

    case _ => println(
      s"""
         |Usage:
         |sbt "run items capacity"
         |  items The list of one-dimensional items to be packed
         |  capacity The capacity of a single bin
         |
         |Example:
         |sbt "run 163841689525773 10"
       """.stripMargin)
  }
}
