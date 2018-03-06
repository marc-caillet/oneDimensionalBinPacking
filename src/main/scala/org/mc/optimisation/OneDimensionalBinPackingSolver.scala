package org.mc.optimisation

import scala.annotation.tailrec
import scala.collection.immutable.List
import scala.language.postfixOps


/**
  * This object provides some heuristics to solve the one-dimensional bin packing problem.
  */
object OneDimensionalBinPackingSolver extends BinPackingSolver[OneD_Item,OneD_Bin,OneD_Capacity] {

  // LOWER BOUND

  /**
    * Computes the lower bound of the number of bins that are needed for packing the items
    *
    * @param items    The list of items as a String
    * @param capacity The capacity of a bin
    * @return The lower bound
    */
  def lowerBound(items: String, capacity: OneD_Capacity): Int =
    math.ceil(stringToUnsortedList(items).foldLeft(0) { (acc: Int, item: OneD_Item) => acc + item.size }.toDouble / capacity.amount).toInt

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
  def greedy(binSelector: (OneD_Item, List[OneD_Bin]) => Option[OneD_Bin])
            (itemStringToList: String => List[OneD_Item])
            (items: String, capacity: OneD_Capacity): List[OneD_Bin] = {
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
            // No such bin could be found. Adds a new bin to the list and packs the item into it.
            greedyRec(
              bins :+ OneD_Bin(
                List(items.head),
                OneD_Capacity(capacity.amount - items.head.size),
                bins.length),
              items.tail
            )

          case Some(bin) =>
            // One such bin has been found. Packs the item into it.
            greedyRec(
              bins.updated(
                bin.id,
                OneD_Bin(
                  bins(bin.id).items :+ items.head,
                  OneD_Capacity(bins(bin.id).capacity.amount - items.head.size),
                  bin.id
                )
              ),
              items.tail
            )
        }
      }
    }

    greedyRec(List.empty[OneD_Bin], itemStringToList(items))
  }

  /**
    * Given an item, evaluates whether or not tthe item can fit into the last bin.
    *
    * @param item An item
    * @param bins A list of bins
    * @return Some list of bins with the last bin as its single element if it can match the item's size;
    *         None otherwise
    */
  def evaluateLastBin(item: OneD_Item, bins: List[OneD_Bin]): List[OneD_Bin] = {

    val last = bins.length - 1

    if (bins.isEmpty || bins(last).capacity.amount < item.size)
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
  def evaluateAllBins(item: OneD_Item, bins: List[OneD_Bin]): List[OneD_Bin] = {

    val candidateBins = bins map {
      bin => OneD_Bin(bin.items, OneD_Capacity(bin.capacity.amount - item.size), bin.id)
    } filter {
      _.capacity.amount >= 0
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
  def sortBinsByDecreasingCapacity(bins: List[OneD_Bin]): List[OneD_Bin] =
    bins sortWith (_.capacity.amount > _.capacity.amount)

  /**
    * Sorts a list of bins by increasing capacity.
    *
    * @param bins A list of bins
    * @return A sorted list of bins
    */
  def sortBinsByIncreasingCapacity(bins: List[OneD_Bin]): List[OneD_Bin] =
    bins sortWith (_.capacity.amount < _.capacity.amount)

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
  def sortItemsByDecreasingSize(items: List[OneD_Item]): List[OneD_Item] =
    items sortWith (_.size > _.size)
}

/**
  * A OneD_Item is defined by its one-dimensional size
  *
  * @param size The one-dimensional size of the item
  */
case class OneD_Item(size: Int) extends Item {
  override def toString: String = size.toString
}

/**
  * A OneD_Bin is defined by:
  * <ul>
  * <li>The list of items it contains</li>
  * <li>Its remaining one-dimensional capacity</li>
  * <li> An identifier</li>
  * </ul>
  *
  * @param items    The list of items
  * @param capacity The remaining one-dimensional capacity
  * @param id       The identifier of the bin
  */
case class OneD_Bin(items: List[OneD_Item], capacity: OneD_Capacity, id: Int) extends Bin {
  override def toString: String = (for (item <- items) yield item.toString) mkString ""
}

/**
  * A OneD_Capacity is defined by a one-dimensional amount
  * @param amount The one-dimensional amount
  */
case class OneD_Capacity(amount: Int) extends Capacity {
  override def toString: String = amount.toString
}