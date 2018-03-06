package org.mc.optimisation.oneD

import org.mc.optimisation.generic.Capacity

/**
  * A OneD_Capacity is defined by a one-dimensional amount
  *
  * @param amount The one-dimensional amount
  */
case class OneD_Capacity(amount: Int) extends Capacity {
  override def toString: String = amount.toString
}
