package org.mc.optimisation


object OneDimensionalBinPackingApp extends App {

  args.length match {

    case 2 =>

      val items: String = args(0)
      val capacity: OneD_Capacity = OneD_Capacity(args(1).toInt)

      println(s"Articles : ${args(0)}")
      println(s"Capacité : ${args(1)}")
      println(s"Borne inférieure du nombre de cartons : ${OneDimensionalBinPackingSolver.lowerBound(items, capacity)}")

      val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)
      println(s"Robot glouton naïf : ${OneDimensionalBinPackingSolver.prettyPrint(naiveBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.num(naiveBins)} cartons utilisés")

      val ffBins = OneDimensionalBinPackingSolver.ff(items, capacity)
      println(s"Robot glouton (First Fit) : ${OneDimensionalBinPackingSolver.prettyPrint(ffBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.num(ffBins)} cartons utilisés")

      val ffdBins = OneDimensionalBinPackingSolver.ffd(items, capacity)
      println(s"Robot glouton (First Fit Decreasing) : ${OneDimensionalBinPackingSolver.prettyPrint(ffdBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.num(ffdBins)} cartons utilisés")

      val bfBins = OneDimensionalBinPackingSolver.bf(items, capacity)
      println(s"Robot glouton (Best Fit) : ${OneDimensionalBinPackingSolver.prettyPrint(bfBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.num(bfBins)} cartons utilisés")

      val bfdBins = OneDimensionalBinPackingSolver.bfd(items, capacity)
      println(s"Robot glouton (Best Fit Decreasing) : ${OneDimensionalBinPackingSolver.prettyPrint(bfdBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.num(bfdBins)} cartons utilisés")

      val wfBins = OneDimensionalBinPackingSolver.wf(items, capacity)
      println(s"Robot glouton (Worst Fit) : ${OneDimensionalBinPackingSolver.prettyPrint(wfBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.num(wfBins)} cartons utilisés")

      val wfdBins = OneDimensionalBinPackingSolver.wfd(items, capacity)
      println(s"Robot glouton (Worst Fit Decreasing) : ${OneDimensionalBinPackingSolver.prettyPrint(wfdBins)} " +
        s"=> ${OneDimensionalBinPackingSolver.num(wfdBins)} cartons utilisés")

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
