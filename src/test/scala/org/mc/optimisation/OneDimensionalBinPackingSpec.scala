package org.mc.optimisation

import org.mc.optimisation.OneDimensionalBinPackingSolver.{Bin, Item}
import org.scalatest.{FlatSpec, Matchers, PrivateMethodTester}


class OneDimensionalBinPackingSpec extends FlatSpec with Matchers with PrivateMethodTester {

  "The lowerBound" should " 1 when the sum of all the items' size is less than a bin's capacity" in {
    val bin = Bin(List(), 10)
    val items = List(Item(1),Item(6))
    OneDimensionalBinPackingSolver.lowerBound(items, bin.capacity) should equal (1)
  }

  it should "be 1 when the sum of all the items' size equals a bin's capacity" in {
    val bin = Bin(List(), 10)
    val items = List(Item(1),Item(6),Item(3))
    OneDimensionalBinPackingSolver.lowerBound(items, bin.capacity) should equal (1)
  }

  "The stringToItems method" should "create a list of items" in {
    val itemString = "163841689525773"
    val itemList = OneDimensionalBinPackingSolver.stringToItems(itemString)

    itemList shouldBe a [List[_]]
    itemList foreach(_ shouldBe a [Item])
  }

  it should "create a list which number of elements equals the number of characters in the input string" in {
    val itemString = "163841689525773"
    val itemList = OneDimensionalBinPackingSolver.stringToItems(itemString)

    itemList.size should equal (itemString.size)
  }

  "The numBins" should "be 0 when the list of bins is empty" in {
    val bins = List[Bin]()
    OneDimensionalBinPackingSolver.numBins(bins) should equal (0)
  }

  it should "be 2 when the list of bins contains 2 elements" in {
    val bins = List[Bin](Bin(List[Item](),10),Bin(List[Item](),10))
    OneDimensionalBinPackingSolver.numBins(bins) should equal (2)
  }

  "The naive strategy" should "use a number of bins that at least equals the lower bound" in {
    val items = "163841689525773"
    val capacity = 10

    val lowerBound = OneDimensionalBinPackingSolver.lowerBound(items, capacity)
    val bins = OneDimensionalBinPackingSolver.naive(items, capacity)

    bins.size should be >= lowerBound
  }

  "The First Fit strategy" should "use a number of bins that is greater than or equals the lower bound " +
    "and lesser than or equals the number of bins used by the naive strategy" in {
    val items = "163841689525773"
    val capacity = 10

    val lowerBound = OneDimensionalBinPackingSolver.lowerBound(items, capacity)
    val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)
    val ffBins = OneDimensionalBinPackingSolver.ff(items, capacity)

    ffBins.size should be >= lowerBound
    ffBins.size should be <= naiveBins.size
  }

  "The First Fit Decreasing strategy" should "use a number of bins that is greater than or equals the lower bound " +
    "and lesser than or equals the number of bins used by the naive strategy" in {
    val items = "163841689525773"
    val capacity = 10

    val lowerBound = OneDimensionalBinPackingSolver.lowerBound(items, capacity)
    val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)
    val ffdBins = OneDimensionalBinPackingSolver.ffd(items, capacity)

    ffdBins.size should be >= lowerBound
    ffdBins.size should be <= naiveBins.size
  }
}
