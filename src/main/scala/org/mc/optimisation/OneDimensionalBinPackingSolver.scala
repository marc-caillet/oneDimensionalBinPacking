package org.mc.optimisation

import scala.annotation.tailrec
import scala.collection.immutable.List
import scala.language.postfixOps


/**
  * This object provides some methods to solve the one-dimensional bin packing problem.
  */
object OneDimensionalBinPackingSolver {

  // LOWER BOUND

  /**
    * Computes the lower bound of the number of bins that are needed for packing the items
    *
    * @param items    The list of items as a String
    * @param capacity The capacity of a bin
    * @return The lower bound
    */
  def lowerBound(items: String, capacity: Int): Int = lowerBound(Items.stringToList(items), capacity)

  /**
    * Computes the lower bound of the number of bins that are needed for packing the items
    *
    * @param items    The list of items as a List of Items
    * @param capacity The capacity of a bin
    * @return The lower bound
    */
  def lowerBound(items: List[Item], capacity: Int): Int =
    math.ceil(items.foldLeft(0) { (acc: Int, item: Item) => acc + item.size }.toDouble / capacity).toInt


  // GREEDY HEURISTICS

  /**
    * The naive bin packing heuristic uses a new bin each time it cannot fit an item into the current bin
    * then never comes back to the bins it previously used.
    *
    * @param items    The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def naive(items: String, capacity: Int): List[Bin] = greedy(Items.stringToList(items), capacity, Bins.naiveSelector)

  /**
    * The first fit greedy heuristic packs a given item into the first bin in which the item can be packed.
    *
    * @param items    The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def ff(items: String, capacity: Int): List[Bin] = greedy(Items.stringToList(items), capacity, Bins.ffSelector)

  /**
    * The first fit decreasing greedy heuristic does the same as the first fit method but first sorts the list of items in decreasing order,
    * thus packs the bigger items first.
    *
    * @param items    The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def ffd(items: String, capacity: Int): List[Bin] = greedy(Items.stringToSortedListByDecreasingSize(items), capacity, Bins.ffSelector)

  /**
    * The best fit greedy heuristic packs a given item into a bin that will have the least room left over
    * after the item is placed into it.
    *
    * @param items    The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def bf(items: String, capacity: Int): List[Bin] = greedy(Items.stringToList(items), capacity, Bins.bfSelector)

  /**
    * The best fit decreasing greedy heuristic does the same as the best fit method but first sorts the list of items in decreasing order,
    * thus packs bigger items first
    *
    * @param items    The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def bfd(items: String, capacity: Int): List[Bin] = greedy(Items.stringToSortedListByDecreasingSize(items), capacity, Bins.bfSelector)

  /**
    * The worst fit greedy heuristic packs a given item into a bin that will have the most room left over
    * after the item is placed into it.
    *
    * @param items    The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def wf(items: String, capacity: Int): List[Bin] = greedy(Items.stringToList(items), capacity, Bins.wfSelector)

  /**
    * The worst fit decreasing greedy heuristic does the same as the worst fit method but first sorts the list of items in decreasing order,
    * thus packs bigger items first
    *
    * @param items    The list of items
    * @param capacity The capacity of a bin
    * @return A list of bins
    */
  def wfd(items: String, capacity: Int): List[Bin] = greedy(Items.stringToSortedListByDecreasingSize(items), capacity, Bins.wfSelector)

  /**
    * At each step, the greedy heuristics select the bin that fit their strategy.
    *
    * @param items       The list of items
    * @param capacity    The capacity of a bin
    * @param binSelector The bin selector function
    * @return A list of bins
    */
  private[OneDimensionalBinPackingSolver] def greedy(items: List[Item], capacity: Int, binSelector: (Item, List[Bin]) => Option[Bin]): List[Bin] = {
    /**
      * Recursively computes the list of bins
      *
      * @param bins  The bins that have already been used for packing
      * @param items The list of items that remain to be packed
      * @return A list of bins
      */
    @tailrec
    def greedyRec(bins: List[Bin], items: List[Item]): List[Bin] = {

      if (items isEmpty)
        bins

      else {

        // Tries to find a bin that matches the strategy condition.
        binSelector(items.head, bins) match {

          case None =>
            // No such bin could be found. Adds a new bin to the list.
            greedyRec(bins :+ Bin(List(items.head), capacity - items.head.size, bins.length), items.tail)

          case Some(bin) =>
            // One such bin has been found. Updates it.
            greedyRec(bins.updated(bin.id, Bin(bins(bin.id).items :+ items.head, bins(bin.id).capacity - items.head.size, bin.id)), items.tail)
        }
      }
    }

    greedyRec(List.empty[Bin], items)
  }



  // DATA MODEL

  /**
    * An Item is defined by its one-dimensional size
    *
    * @param size The size of the item
    */
  case class Item(size: Int) {
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
  case class Bin(items: List[Item], capacity: Int, id: Int) {
    override def toString: String = (for (item <- items) yield item.toString) mkString ""
  }



  /**
    * The Bins object implements:
    * <ul>
    *   <li>a bin selector for each greedy heuristic</li>
    *   <li>some utility functions</li>
    * </ul>
    */
  object Bins {

    /**
      * Evaluates the capacity of the last bin used regarding the item size.
      *
      * @param item An item
      * @param bins The list of bin
      * @return The last bin if it has enough capacity; None otherwise
      */
    private[OneDimensionalBinPackingSolver] def naiveSelector(item: Item, bins: List[Bin]): Option[Bin] =
      select(evaluateLast(item, bins))

    /**
      * For a given item, finds the first bin that matches the first fit condition,
      * i.e. the first bin in which the item can be packed.
      *
      * @param item An item
      * @param bins The list of bins
      * @return Some bin if at least one has been found; None otherwise
      */
    private[OneDimensionalBinPackingSolver] def ffSelector(item: Item, bins: List[Bin]): Option[Bin] =
      select(evaluateAll(item, bins))

    /**
      * For a given item, finds a bin that matches the best fit condition, i.e. the bin that will have the least room
      * left over after the item is placed into it.
      *
      * @param item An item
      * @param bins The list of bins
      * @return Some bin if at least one has been found; None otherwise
      */
    private[OneDimensionalBinPackingSolver] def bfSelector(item: Item, bins: List[Bin]): Option[Bin] =
      select(
        evaluateAll(item, bins),
        Some(sortByIncreasingCapacity)
      )

    /**
      * For a given item, finds a bin that matches the worst fit condition, i.e. the bin that will have the most room
      * left over after the item is placed into it.
      *
      * @param item An item
      * @param bins The list of bins
      * @return Some bin if at least one has been found; None otherwise
      */
    private[OneDimensionalBinPackingSolver] def wfSelector(item: Item, bins: List[Bin]): Option[Bin] =
      select(
        evaluateAll(item, bins),
        Some(Bins.sortByDecreasingCapacity)
      )

    /**
      * Selects a bin.
      * @param candidates Optional candidate bins
      * @param candidateSort Optional bin sort function
      * @return Some bin if one could be found; None otherwise
      */
    private[Bins] def select(candidates: Option[List[Bin]], candidateSort: Option[List[Bin] => List[Bin]] = None): Option[Bin] = candidates match {

      case None => None

      case Some(candidateBins) => candidateSort match {
        case None => Some(candidateBins.head)
        case Some(sort) => Some(sort(candidateBins).head)
      }
    }

    /**
      * Given an item, evaluates whether or not the last bin can be a candidate bin, i.e. can match the item's size.
      * @param item An item
      * @param bins A list of bins
      * @return Some list of bins with the last bin as its single element if it can match the item's size; None otherwise
      */
    private[Bins] def evaluateLast(item: Item, bins: List[Bin]): Option[List[Bin]] = {

      val last = bins.length - 1

      if (bins.isEmpty || bins(last).capacity < item.size)
        None
      else
        Some(List(bins(last)))
    }

    /**
      * Given an item, evaluates whether or not each bin can be a candidate bin, i.e. can match the item's size.
      * @param item An item
      * @param bins A list of bins
      * @return Some list of bins if at least one of the input bins can match the item's size; None otherwise
      */
    private[Bins] def evaluateAll(item: Item, bins: List[Bin]): Option[List[Bin]] = {

      val candidateBins = bins map {
        bin => Bin(bin.items, bin.capacity - item.size, bin.id)
      } filter {
        _.capacity >= 0
      }

      if (candidateBins isEmpty)
        None
      else
        Some(candidateBins)
    }

    /**
      * Sorts a list of bins by decreasing capacity.
      * @param bins A list of bins
      * @return A sorted list of bins
      */
    private[Bins] def sortByDecreasingCapacity(bins: List[Bin]): List[Bin] =
      bins sortWith (_.capacity > _.capacity)

    /**
      * Sorts a list of bins by increasing capacity.
      * @param bins A list of bins
      * @return A sorted list of bins
      */
    private[Bins] def sortByIncreasingCapacity(bins: List[Bin]): List[Bin] =
      bins sortWith (_.capacity < _.capacity)

    /**
      * Pretty prints a list of bins
      *
      * @param bins The list of bins
      * @return A pretty string
      */
    def prettyPrint(bins: List[Bin]): String = (for (bin <- bins) yield bin.toString) mkString "/"

    /**
      * Computes the number of bins in a list of bins
      *
      * @param bins The list of bins
      * @return The number of bins
      */
    def num(bins: List[Bin]): Int = bins.size
  }


  /**
    * The Items object implements some utility functions for list of items.
    */
  object Items {

    /**
      * Transforms a string of items into a list of items,
      * assuming that each character in the string is a number that stands for the size of an item
      *
      * @param items The string of items
      * @return The list of items
      */
    def stringToList(items: String, itemSort: Option[List[Item] => List[Item]] = None): List[Item] = {

      val itemList = ((items split "") map (i => Item(i.toInt))).toList

      itemSort match {
        case None => itemList
        case Some(sort) => sort(itemList)
      }
    }

    /**
      * Transforms a string of items into a list of items the sorts it by decreasing size,
      * assuming that each character in the string is a number that stands for the size of an item
      *
      * @param items The string of items
      * @return The list of items
      */
    private[OneDimensionalBinPackingSolver] def stringToSortedListByDecreasingSize(items: String): List[Item] =
      stringToList(items, Some(sortByDecreasingSize))

    /**
      * Sorts a list of items by decreasing size
      * @param items A list of items
      * @return A sorted list of items
      */
    private[Items] def sortByDecreasingSize(items: List[Item]): List[Item] =
      items sortWith (_.size > _.size)
  }
}