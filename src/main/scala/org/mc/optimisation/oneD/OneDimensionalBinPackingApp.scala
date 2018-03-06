package org.mc.optimisation.oneD

import org.mc.optimisation.oneD.OneDimensionalBinPackingSolver._

object OneDimensionalBinPackingApp extends App {

  args.length match {

    case 2 =>

      val items: String = args(0)
      val capacity: OneD_Capacity = OneD_Capacity(args(1).toInt)

      println(s"Articles : ${args(0)}")
      println(s"Capacité : ${args(1)}")
      println(s"Borne inférieure du nombre de cartons : ${lowerBound(items, capacity)}")

      val naiveBins = naive(items, capacity)
      println(prettyFormatBinPackingSolution("Robot glouton naïf", naiveBins))

      val ffBins = ff(items, capacity)
      println(prettyFormatBinPackingSolution("Robot glouton (First Fit)", ffBins))

      val ffdBins = ffd(items, capacity)
      println(prettyFormatBinPackingSolution("Robot glouton (First Fit Decreasing)", ffdBins))

      val bfBins = bf(items, capacity)
      println(prettyFormatBinPackingSolution("Robot glouton (Best Fit)", bfBins))

      val bfdBins = bfd(items, capacity)
      println(prettyFormatBinPackingSolution("Robot glouton (Best Fit Decreasing)", bfdBins))

      val wfBins = wf(items, capacity)
      println(prettyFormatBinPackingSolution("Robot glouton (Worst Fit)", wfBins))

      val wfdBins = wfd(items, capacity)
      println(prettyFormatBinPackingSolution("Robot glouton (Worst Fit Decreasing)", wfdBins))

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
