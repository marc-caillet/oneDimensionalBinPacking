package org.mc.optimisation


object OneDimensionalBinPackingApp extends App {

  args.length match {

    case 2 =>

      val items: String = args(0)
      val capacity: Int = args(1).toInt

      println(s"Articles : ${args(0)}")
      println(s"Capacité : ${args(1)}")
      println(s"Borne inférieure du nombre de cartons : ${OneDimensionalBinPackingSolver.lowerBound(items, capacity)}")

      val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)
      println(s"Robot glouton naïf : ${OneDimensionalBinPackingSolver.Bins.prettyPrint(naiveBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.Bins.num(naiveBins)} cartons utilisés")

      val ffBins = OneDimensionalBinPackingSolver.ff(items, capacity)
      println(s"Robot glouton (First Fit) : ${OneDimensionalBinPackingSolver.Bins.prettyPrint(ffBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.Bins.num(ffBins)} cartons utilisés")

      val ffdBins = OneDimensionalBinPackingSolver.ffd(items, capacity)
      println(s"Robot glouton (First Fit Decreasing) : ${OneDimensionalBinPackingSolver.Bins.prettyPrint(ffdBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.Bins.num(ffdBins)} cartons utilisés")

      val bfBins = OneDimensionalBinPackingSolver.bf(items, capacity)
      println(s"Robot glouton (Best Fit) : ${OneDimensionalBinPackingSolver.Bins.prettyPrint(bfBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.Bins.num(bfBins)} cartons utilisés")

      val bfdBins = OneDimensionalBinPackingSolver.bfd(items, capacity)
      println(s"Robot glouton (Best Fit Decreasing) : ${OneDimensionalBinPackingSolver.Bins.prettyPrint(bfdBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.Bins.num(bfdBins)} cartons utilisés")

      val wfBins = OneDimensionalBinPackingSolver.wf(items, capacity)
      println(s"Robot glouton (Worst Fit) : ${OneDimensionalBinPackingSolver.Bins.prettyPrint(wfBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.Bins.num(wfBins)} cartons utilisés")

      val wfdBins = OneDimensionalBinPackingSolver.wfd(items, capacity)
      println(s"Robot glouton (Worst Fit Decreasing) : ${OneDimensionalBinPackingSolver.Bins.prettyPrint(wfdBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.Bins.num(wfdBins)} cartons utilisés")

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
