package org.mc.optimisation.oneD

import org.mc.optimisation.generic.Bin
import scala.collection.immutable.List

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
