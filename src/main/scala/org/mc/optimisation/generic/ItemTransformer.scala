package org.mc.optimisation.generic

import scala.collection.immutable.List

/**
  * This trait defines functions that build a list of items from an item string
  * @tparam I An Item type
  */
trait ItemTransformer[I <: Item] {

  /**
    * Transforms a string of items into a list of items,
    *
    * @param items The string of items
    * @return The list of items
    */
  def stringToList(itemSort: List[I] => List[I])(items: String): List[I]

  /**
    * Transforms a string of items into a list of items,
    * assuming that each character in the string is a number that stands for the size of an item
    */
  val stringToUnsortedList: String => List[I] = stringToList(unSortItems)(_)

  /**
    * Transforms a string of items into a list of items then sorts it by decreasing size,
    * assuming that each character in the string is a number that stands for the size of an item
    */
  val stringToSortedListByDecreasingSize: String => List[I] = stringToList(sortItemsByDecreasingSize)(_)

  /**
    * Returns the input list of items
    * @param items A list of items
    * @return The same list of items untouched
    */
  def unSortItems(items: List[I]): List[I] = items

  /**
    * Sorts a list of items by decreasing size
    * @param items A list of items
    * @return A sorted list of items
    */
  def sortItemsByDecreasingSize(items: List[I]): List[I]
}
