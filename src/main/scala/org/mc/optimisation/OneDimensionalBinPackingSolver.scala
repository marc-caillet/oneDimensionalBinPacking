package org.mc.optimisation

import scala.annotation.tailrec
import scala.collection.immutable.List
import scala.language.postfixOps


/**
  * This object provides some methods to solve the one-dimensional bin packing problem.
  */
object OneDimensionalBinPackingSolver {

  // DATA MODEL

  /**
    * An Item is defined by its one-dimensional size
    * @param size The size of the item
    */
  case class Item(size: Int)

  /**
    * A Bin is defined by:
    * <ul>
    *   <li>The list of items it contains</li>
    *   <li>Its remaining capacity</li>
    * </ul>
    * @param items The list of items
    * @param capacity The remaining capacity
    * @param id The identifier of the bin
    */
  case class Bin(items: List[Item], capacity: Int, id: Int)



  // LOWER BOUND

  /**
    * Computes the lower bound of the number of bins that are needed for packing the items
    * @param items The list of items as a String
    * @param capacity The capacity of a bin
    * @return The lower bound
    */
  def lowerBound(items: String, capacity: Int): Int = lowerBound(stringToItems(items), capacity)

  /**
    * Computes the lower bound of the number of bins that are needed for packing the items
    * @param items The list of items as a List of Items
    * @param capacity The capacity of a bin
    * @return The lower bound
    */
  def lowerBound(items: List[Item], capacity: Int): Int =
    math.ceil(items.foldLeft(0){ (acc: Int, item: Item) => acc + item.size }.toDouble / capacity).toInt




  // GREEDY STRATEGIES

  /**
    * At each step, the greedy methods select the bin that fit their strategy.
    * @param items The list of items
    * @param capacity The capacity of a bin
    * @param binSelector The bin selector function
    * @return A list of bins
    */
  private[OneDimensionalBinPackingSolver] def greedy(items: List[Item], capacity: Int, binSelector: (Item, List[Bin]) => Int): List[Bin] = {
    /**
      * Recursively computes the list of bins
      * @param bins The bins that have already been used for packing
      * @param items the list of items
      * @return The list of bins
      */
    @tailrec
    def greedyRec(bins: List[Bin], items: List[Item]): List[Bin] = {

      if (items.isEmpty)
        bins

      else {

        // Tries to find a bin that matches the strategy condition.
        val binId = binSelector(items.head, bins)

        if (binId == -1)
          // No such bin could be found. Adds a new bin to the list.
          greedyRec(bins :+ Bin(List(items.head), capacity - items.head.size, bins.length), items.tail)

        else
          // One such bin has been found. Updates it.
          greedyRec(bins.updated(binId, Bin(bins(binId).items :+ items.head, bins(binId).capacity - items.head.size, binId)), items.tail)

      }
    }

    greedyRec(List[Bin](), items)
  }

  /**
    * The naive bin packing method uses a new bin each time it cannot fit an item into the current bin
    * then nevers comes back to the bins it already used.
    * @param items The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def naive(items: String, capacity: Int): List[Bin] = greedy(stringToItems(items), capacity, naiveBinSelector)

  /**
    * Evaluates the capacity of the last bin used regarding the item size.
    * @param item An item
    * @param bins The list of bin
    * @return The identifier of the last bin if it can fit the item;
    *         -1 otherwise, meaning a new bin must be used
    */
  private[OneDimensionalBinPackingSolver] def naiveBinSelector(item: Item, bins: List[Bin]): Int = {

    val last = bins.length - 1

    if (last >= 0 && bins(last).capacity >= item.size)
      last
    else
      -1
  }

  /**
    * The first fit greedy method packs a given item into the first bin in which the item can be packed.
    * @param items The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def ff(items: String, capacity: Int): List[Bin] = greedy(stringToItems(items), capacity, ffBinSelector)

  /**
    * The first fit decreasing greedy method does the same as the first fit method but first sorts the list of items in decreasing order,
    * thus packs the bigger items first.
    * @param items The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def ffd(items: String, capacity: Int): List[Bin] = greedy(stringToItems(items) sortWith ( _.size > _.size ), capacity, ffBinSelector)

  /**
    * For a given item, finds the first bin that matches the first fit condition,
    * i.e. the first bin in which the item can be packed.
    * @param item An item
    * @param bins The list of bins
    * @return The identifier of the first bin that matched the first fit condition if it exists;
    *         -1 otherwise, meaning a new bin must be used
    */
  private[OneDimensionalBinPackingSolver] def ffBinSelector(item: Item, bins: List[Bin]): Int = bins find (_.capacity >= item.size) match {
    case Some(bin) => bins indexOf bin
    case None => -1
  }

  /**
    * The best fit greedy method packs a given item into a bin that will have the least room left over
    * after the item is placed into it.
    * @param items The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def bf(items: String, capacity: Int): List[Bin] = greedy(stringToItems(items), capacity, bfBinSelector)

  /**
    * The best fit decreasing greedy method does the same as the best fit method but first sorts the list of items in decreasing order,
    * thus packs bigger items first
    * @param items The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def bfd(items: String, capacity: Int): List[Bin] = greedy(stringToItems(items) sortWith ( _.size > _.size ), capacity, bfBinSelector)

  /**
    * For a given item, finds a bin that matches the best fit condition, i.e. the bin that will have the least room
    * left over after the item is placed into it.
    * @param item An item
    * @param bins The list of bins
    * @return
    */
  private[OneDimensionalBinPackingSolver] def bfBinSelector(item: Item, bins: List[Bin]): Int = {

    val candidateBins = bins map {
      bin => Bin(bin.items, bin.capacity - item.size, bin.id)
    } filter {
      _.capacity >= 0
    }

    if (candidateBins isEmpty)
      -1
    else
      (candidateBins sortWith(_.capacity < _.capacity)).head.id
  }

  /**
    * The worst fit greedy method packs a given item into a bin that will have the most room left over
    * after the item is placed into it.
    * @param items The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def wf(items: String, capacity: Int): List[Bin] = greedy(stringToItems(items), capacity, wfBinSelector)

  /**
    * The worst fit decreasing greedy method does the same as the worst fit method but first sorts the list of items in decreasing order,
    * thus packs bigger items first
    * @param items The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def wfd(items: String, capacity: Int): List[Bin] = greedy(stringToItems(items) sortWith ( _.size > _.size ), capacity, wfBinSelector)

  /**
    * For a given item, finds a bin that matches the worst fit condition, i.e. the bin that will have the most room
    * left over after the item is placed into it.
    * @param item An item
    * @param bins The list of bins
    * @return
    */
  private[OneDimensionalBinPackingSolver] def wfBinSelector(item: Item, bins: List[Bin]): Int = {

    val candidateBins = bins map {
      bin => Bin(bin.items, bin.capacity - item.size, bin.id)
    } filter {
      _.capacity >= 0
    }

    if (candidateBins isEmpty)
      -1
    else
      (candidateBins sortWith(_.capacity > _.capacity)).head.id
  }



  // UTILITY FUNCTIONS

  /**
    * Pretty prints a list of bins
    * @param bins The list of bins
    * @return A pretty string
    */
  def prettyPrintBins(bins: List[Bin]): String = bins map (_.items map(_.size) mkString "") mkString "/"

  /**
    * Computes the number of bins in a list of bins
    * @param bins The list of bins
    * @return The number of bins
    */
  def numBins(bins: List[Bin]): Int = bins.size

  /**
    * Transforms a string of items into a list of items,
    * assuming that each character in the string is a number that stands for the size of an item
    * @param items The string of items
    * @return The list of items
    */
  def stringToItems(items: String): List[Item] =
    ((items split "") map (i => Item(i.toInt))).toList
}
