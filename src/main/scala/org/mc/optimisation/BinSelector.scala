package org.mc.optimisation

import scala.collection.immutable.List

trait BinSelector[I <: Item, B <: Bin] {

  /**
    * Selects a bin as follows:
    * <ul>
    *   <li>Evaluates the capacity of one or more bins regarding the item size, then keeps only those that
    *   can fit the item (binEvaluate)</li>
    *   <li>Sorts the list of selected bins (binSort)</li>
    *   <li>Returns the first bin of the sorted list, if the list is not empty; None otherwise</li>
    * </ul>
    * @param binEvaluate The candidate bin evaluation function
    * @param binSort The candidate bin sort function
    * @param item An item
    * @param bins The list of bins
    * @return
    */
  private def select(binEvaluate: (I,List[B]) => List[B])
                    (binSort: List[B] => List[B])
                    (item: I, bins: List[B]): Option[B] =

    binSort(binEvaluate(item, bins)) match {
      case Nil => None
      case bestBin::_ => Some(bestBin)
    }

  /**
    * Evaluates the capacity of the last bin regarding the item size (evaluateLast),
    * then returns it if it can fit the item.
    */
  val naiveSelect: (I,List[B]) => Option[B] = select(evaluateLast)(unSortBins)(_,_)

  /**
    * For a given item, finds the first bin that matches the first fit condition,
    * i.e. the first bin in which the item can be packed:
    * <ul>
    *   <li>Evaluates the capacity of each bin regarding the item size,
    *   then keeps only those which capacity is enough to fit the item (evaluateAll)</li>
    *   <li>Does not sort the selected bins</li>
    *   <li>Returns the first one</li>
    * </ul>
    */
  val ffSelect: (I,List[B]) => Option[B] = select(evaluateAll)(unSortBins)(_,_)

  /**
    * For a given item, finds a bin that matches the best fit condition, i.e. the bin that will have the least room
    * left over after the item is placed into it:
    * <ul>
    *   <li>Evaluates the capacity of each bin regarding the item size,
    *   then keeps only those which capacity is enough to fit the item (evaluateAll)</li>
    *   <li>Sorts the selected bins by increasing remaining capacity (sortByIncreasingCapacity)</li>
    *   <li>Returns the first one</li>
    * </ul>
    */
  val bfSelect: (I,List[B]) => Option[B] = select(evaluateAll)(sortByIncreasingCapacity)(_,_)

  /**
    * For a given item, finds a bin that matches the worst fit condition, i.e. the bin that will have the most room
    * left over after the item is placed into it:
    * <ul>
    *   <li>Evaluates the capacity of each bin regarding the item size,
    *   then keeps only those which capacity is enough to fit the item (evaluateAll)</li>
    *   <li>Sorts the selected bins by decreasing remaining capacity (sortByDecreasingCapacity)</li>
    *   <li>Returns the first one</li>
    * </ul>
    */
  val wfSelect: (I,List[B]) => Option[B] = select(evaluateAll)(sortByDecreasingCapacity)(_,_)

  /**
    * Given an item, evaluates whether or not the item can fit in the last bin.
    * @param item An item
    * @param bins A list of bins
    * @return Some list of bins with the last bin as its single element if it can match the item's size;
    *         None otherwise
    */
  def evaluateLast(item: I, bins: List[B]): List[B]

  /**
    * Given an item, evaluates whether or not the item can fit into each bin.
    * @param item An item
    * @param bins A list of bins
    * @return Some list of bins if at least one of the input bins can match the item's size; None otherwise
    */
  def evaluateAll(item: I, bins: List[B]): List[B]

  /**
    * Returns the input list of bins
    * @param bins A list of bins
    * @return The same list of bins untouched
    */
  def unSortBins(bins: List[B]): List[B] = bins

  /**
    * Sorts a list of bins by decreasing capacity.
    * @param bins A list of bins
    * @return A sorted list of bins
    */
  def sortByDecreasingCapacity(bins: List[B]): List[B]

  /**
    * Sorts a list of bins by increasing capacity.
    * @param bins A list of bins
    * @return A sorted list of bins
    */
  def sortByIncreasingCapacity(bins: List[B]): List[B]

  /**
    * Pretty prints a list of bins
    *
    * @param bins The list of bins
    * @return A pretty string
    */
  def prettyPrint(bins: List[B]): String = (for (bin <- bins) yield bin.toString) mkString "/"

  /**
    * Computes the number of bins in a list of bins
    *
    * @param bins The list of bins
    * @return The number of bins
    */
  def num(bins: List[B]): Int = bins.size
}
