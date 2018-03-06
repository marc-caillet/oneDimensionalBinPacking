package org.mc.optimisation.oneD

import org.mc.optimisation.generic.Item


/**
  * A OneD_Item is defined by its one-dimensional size
  *
  * @param size The one-dimensional size of the item
  */
case class OneD_Item(size: Int) extends Item {
  override def toString: String = size.toString
}
