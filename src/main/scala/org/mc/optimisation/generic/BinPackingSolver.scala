package org.mc.optimisation.generic

import scala.collection.immutable.List

/**
  * This trait defines some heuristics for the bin packing problem
  * @tparam I An Item type
  * @tparam B A bin type
  * @tparam C A capacity type
  */
trait BinPackingSolver[I <: Item, B <: Bin, C<: Capacity] extends BinSelector[I, B] with ItemTransformer[I] {

  /**
    * Computes the lower bound of the number of bins that are needed for packing the items
    *
    * @param items    The list of items as a String
    * @param capacity The capacity of a bin
    * @return The lower bound
    */
  def lowerBound(items: String, capacity: C): Int



  // GREEDY HEURISTICS

  /**
    * At each step, the greedy heuristics select the bin that fit their strategy.
    * Its curried form allows creating partial functions for each bin selector.
    *
    * @param binSelector      The bin selector function
    * @param itemStringToList The function that transforms the item string into a list of items
    * @param items            The item string
    * @param capacity         The capacity of a bin
    * @return A list of bins
    */
  def greedy(binSelector: (I, List[B]) => Option[B])
            (itemStringToList: String => List[I])
            (items: String, capacity: C): List[B]

  /**
    * The naive bin packing heuristic uses a new bin each time it cannot fit an item into the current bin
    * then never comes back to the bins it previously used.
    */
  val naive: (String, C) => List[B] = greedy(naiveSelect)(stringToUnsortedList)(_, _)

  /**
    * The first fit greedy heuristic packs a given item into the first bin in which the item can be packed.
    */
  val ff: (String, C) => List[B] = greedy(ffSelect)(stringToUnsortedList)(_, _)

  /**
    * The first fit decreasing greedy heuristic does the same as the first fit method
    * but first sorts the list of items in decreasing order, thus packs the bigger items first.
    */
  val ffd: (String, C) => List[B] = greedy(ffSelect)(stringToSortedListByDecreasingSize)(_, _)

  /**
    * The best fit greedy heuristic packs a given item into a bin that will have the least room left over
    * after the item is placed into it.
    */
  val bf: (String, C) => List[B] = greedy(bfSelect)(stringToUnsortedList)(_, _)

  /**
    * The best fit decreasing greedy heuristic does the same as the best fit method
    * but first sorts the list of items in decreasing order, thus packs bigger items first
    */
  val bfd: (String, C) => List[B] = greedy(bfSelect)(stringToSortedListByDecreasingSize)(_, _)

  /**
    * The worst fit greedy heuristic packs a given item into a bin that will have the most room left over
    * after the item is placed into it.
    */
  val wf: (String, C) => List[B] = greedy(wfSelect)(stringToUnsortedList)(_, _)

  /**
    * The worst fit decreasing greedy heuristic does the same as the worst fit method
    * but first sorts the list of items in decreasing order, thus packs bigger items first
    */
  val wfd: (String, C) => List[B] = greedy(wfSelect)(stringToSortedListByDecreasingSize)(_, _)



  // UTILS

  /**
    * Pretty prints a list of bins
    *
    * @param bins The list of bins
    * @return A pretty string
    */
  def prettyFormatBins(bins: List[B]): String = (for (bin <- bins) yield bin.toString) mkString "/"

  /**
    * Computes the number of bins in a list of bins
    *
    * @param bins The list of bins
    * @return The number of bins
    */
  def numBins(bins: List[B]): Int = bins.size

  def prettyFormatBinPackingSolution(label: String, bins: List[B]): String =
    s"$label : ${prettyFormatBins(bins)} => ${numBins(bins)} cartons utilisés"
}
