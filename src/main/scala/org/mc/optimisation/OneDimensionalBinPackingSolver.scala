package org.mc.optimisation

import scala.annotation.tailrec
import scala.collection.immutable.List
import scala.language.postfixOps


/**
  * This object provides some methods to solve the one-dimensional bin packing problem.
  */
object OneDimensionalBinPackingSolver extends BinSelector[OneD_Item,OneD_Bin] with ItemTransformer[OneD_Item] {

  // LOWER BOUND

  /**
    * Computes the lower bound of the number of bins that are needed for packing the items
    *
    * @param items    The list of items as a String
    * @param capacity The capacity of a bin
    * @return The lower bound
    */
  def lowerBound(items: String, capacity: Int): Int =
    math.ceil(stringToUnsortedList(items).foldLeft(0) { (acc: Int, item: OneD_Item) => acc + item.size }.toDouble / capacity).toInt



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
  private[OneDimensionalBinPackingSolver] def greedy(binSelector: (OneD_Item, List[OneD_Bin]) => Option[OneD_Bin])
                                                    (itemStringToList: String => List[OneD_Item])
                                                    (items: String, capacity: Int): List[OneD_Bin] = {
    /**
      * Recursively computes the list of bins
      *
      * @param bins  The bins that have already been used for packing
      * @param items The list of items that remain to be packed
      * @return A list of bins
      */
    @tailrec
    def greedyRec(bins: List[OneD_Bin], items: List[OneD_Item]): List[OneD_Bin] = {

      if (items isEmpty)
        bins

      else {

        // Tries to find a bin that matches the strategy condition.
        binSelector(items.head, bins) match {

          case None =>
            // No such bin could be found. Adds a new bin to the list.
            greedyRec(
              bins :+ OneD_Bin(List(items.head), capacity - items.head.size, bins.length), items.tail)

          case Some(bin) =>
            // One such bin has been found. Updates it.
            greedyRec(bins.updated(bin.id, OneD_Bin(bins(bin.id).items :+ items.head, bins(bin.id).capacity - items.head.size, bin.id)), items.tail)
        }
      }
    }

    greedyRec(List.empty[OneD_Bin], itemStringToList(items))
  }

  /**
    * The naive bin packing heuristic uses a new bin each time it cannot fit an item into the current bin
    * then never comes back to the bins it previously used.
    */
  val naive: (String, Int) => List[OneD_Bin] = greedy(naiveSelect)(stringToUnsortedList)(_, _)

  /**
    * The first fit greedy heuristic packs a given item into the first bin in which the item can be packed.
    */
  val ff: (String, Int) => List[OneD_Bin] = greedy(ffSelect)(stringToUnsortedList)(_, _)

  /**
    * The first fit decreasing greedy heuristic does the same as the first fit method
    * but first sorts the list of items in decreasing order, thus packs the bigger items first.
    */
  val ffd: (String, Int) => List[OneD_Bin] = greedy(ffSelect)(stringToSortedListByDecreasingSize)(_, _)

  /**
    * The best fit greedy heuristic packs a given item into a bin that will have the least room left over
    * after the item is placed into it.
    */
  val bf: (String, Int) => List[OneD_Bin] = greedy(bfSelect)(stringToUnsortedList)(_, _)

  /**
    * The best fit decreasing greedy heuristic does the same as the best fit method
    * but first sorts the list of items in decreasing order, thus packs bigger items first
    */
  val bfd: (String, Int) => List[OneD_Bin] = greedy(bfSelect)(stringToSortedListByDecreasingSize)(_, _)

  /**
    * The worst fit greedy heuristic packs a given item into a bin that will have the most room left over
    * after the item is placed into it.
    */
  val wf: (String, Int) => List[OneD_Bin] = greedy(wfSelect)(stringToUnsortedList)(_, _)

  /**
    * The worst fit decreasing greedy heuristic does the same as the worst fit method
    * but first sorts the list of items in decreasing order, thus packs bigger items first
    */
  val wfd: (String, Int) => List[OneD_Bin] = greedy(wfSelect)(stringToSortedListByDecreasingSize)(_, _)


  /**
    * Given an item, evaluates whether or not tthe item can fit into the last bin.
    *
    * @param item An item
    * @param bins A list of bins
    * @return Some list of bins with the last bin as its single element if it can match the item's size;
    *         None otherwise
    */
  def evaluateLast(item: OneD_Item, bins: List[OneD_Bin]): List[OneD_Bin] = {

    val last = bins.length - 1

    if (bins.isEmpty || bins(last).capacity < item.size)
      List.empty[OneD_Bin]
    else
      List(bins(last))
  }

  /**
    * Given an item, evaluates whether or not the item can fit into each bin.
    *
    * @param item An item
    * @param bins A list of bins
    * @return Some list of bins if at least one of the input bins can match the item's size; None otherwise
    */
  def evaluateAll(item: OneD_Item, bins: List[OneD_Bin]): List[OneD_Bin] = {

    val candidateBins = bins map {
      bin => OneD_Bin(bin.items, bin.capacity - item.size, bin.id)
    } filter {
      _.capacity >= 0
    }

    if (candidateBins isEmpty)
      List.empty[OneD_Bin]
    else
      candidateBins
  }

  /**
    * Sorts a list of bins by decreasing capacity.
    *
    * @param bins A list of bins
    * @return A sorted list of bins
    */
  def sortByDecreasingCapacity(bins: List[OneD_Bin]): List[OneD_Bin] =
    bins sortWith (_.capacity > _.capacity)

  /**
    * Sorts a list of bins by increasing capacity.
    *
    * @param bins A list of bins
    * @return A sorted list of bins
    */
  def sortByIncreasingCapacity(bins: List[OneD_Bin]): List[OneD_Bin] =
    bins sortWith (_.capacity < _.capacity)

  /**
    * Transforms a string of items into a list of items,
    * assuming that each character in the string is a number that stands for the size of an item
    *
    * @param items The string of items
    * @return The list of items
    */
  def stringToList(itemSort: List[OneD_Item] => List[OneD_Item])(items: String): List[OneD_Item] =
    itemSort(((items split "") map (i => OneD_Item(i.toInt))).toList)


  /**
    * Sorts a list of items by decreasing size
    *
    * @param items A list of items
    * @return A sorted list of items
    */
  def sortByDecreasingSize(items: List[OneD_Item]): List[OneD_Item] =
    items sortWith (_.size > _.size)
}

/**
  * An Item is defined by its one-dimensional size
  *
  * @param size The size of the item
  */
case class OneD_Item(size: Int) extends Item {
  override def toString: String = size.toString
}

/**
  * A Bin is defined by:
  * <ul>
  * <li>The list of items it contains</li>
  * <li>Its remaining capacity</li>
  * </ul>
  *
  * @param items    The list of items
  * @param capacity The remaining capacity
  * @param id       The identifier of the bin
  */
case class OneD_Bin(items: List[OneD_Item], capacity: Int, id: Int) extends Bin {
  override def toString: String = (for (item <- items) yield item.toString) mkString ""
}