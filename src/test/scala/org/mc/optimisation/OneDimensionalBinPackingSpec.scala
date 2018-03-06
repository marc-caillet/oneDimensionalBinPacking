package org.mc.optimisation

import org.scalatest.{FlatSpec, Matchers, PrivateMethodTester}


class OneDimensionalBinPackingSpec extends FlatSpec with Matchers with PrivateMethodTester {

  "The lowerBound" should " 1 when the sum of all the items' size is less than a bin's capacity" in {
    val bin = OneD_Bin(List(), OneD_Capacity(10), 0)
    val items = "16"
    OneDimensionalBinPackingSolver.lowerBound(items, bin.capacity) should equal (1)
  }

  it should "be 1 when the sum of all the items' size equals a bin's capacity" in {
    val bin = OneD_Bin(List(), OneD_Capacity(10), 0)
    val items = "163"
    OneDimensionalBinPackingSolver.lowerBound(items, bin.capacity) should equal (1)
  }

  "The stringToItems method" should "create a list of items" in {
    val itemString = "163841689525773"
    val itemList = OneDimensionalBinPackingSolver.stringToUnsortedList(itemString)

    itemList shouldBe a [List[_]]
    itemList foreach(_ shouldBe a [OneD_Item])
  }

  it should "create a list which number of elements equals the number of characters in the input string" in {
    val itemString = "163841689525773"
    val itemList = OneDimensionalBinPackingSolver.stringToUnsortedList(itemString)

    itemList.size should equal (itemString.length)
  }

  "The numBins" should "be 0 when the list of bins is empty" in {
    val bins = List[OneD_Bin]()
    OneDimensionalBinPackingSolver.numBins(bins) should equal (0)
  }

  it should "be 2 when the list of bins contains 2 elements" in {
    val bins = List[OneD_Bin](OneD_Bin(List[OneD_Item](),OneD_Capacity(10), 0),OneD_Bin(List[OneD_Item](),OneD_Capacity(10), 1))
    OneDimensionalBinPackingSolver.numBins(bins) should equal (2)
  }

  "The naive strategy" should "use a number of bins that at least equals the lower bound" in {
    val items = "163841689525773"
    val capacity = OneD_Capacity(10)

    val lowerBound = OneDimensionalBinPackingSolver.lowerBound(items, capacity)
    val bins = OneDimensionalBinPackingSolver.naive(items, capacity)

    bins.size should be >= lowerBound
  }

  it should "pack an item into the last bin if the item size is less than this bin's remaining capacity" in {
    val items = "63"
    val capacity = OneD_Capacity(10)

    val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)

    naiveBins should equal (List(OneD_Bin(List(OneD_Item(6),OneD_Item(3)),OneD_Capacity(1),0)))
  }

  it should "pack each item into the last bin if the item size equals this bin's remaining capacity" in {
    val items = "64"
    val capacity = OneD_Capacity(10)

    val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)

    naiveBins should equal (List(OneD_Bin(List(OneD_Item(6),OneD_Item(4)),OneD_Capacity(0),0)))
  }

  it should "pack each item into a new bin if the item size is greater than the least bin's remaining capacity" in {
    val items = "65"
    val capacity = OneD_Capacity(10)

    val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)

    naiveBins should equal (List(OneD_Bin(List(OneD_Item(6)),OneD_Capacity(4),0),OneD_Bin(List(OneD_Item(5)),OneD_Capacity(5),1)))
  }

  it should "pack each item into a new bin if the item size is greater than the least bin's remaining capacity" +
    "even though the previous bin has enough room for the item" in {
    val items = "683"
    val capacity = OneD_Capacity(10)

    val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)

    naiveBins should equal (List(OneD_Bin(List(OneD_Item(6)),OneD_Capacity(4),0),OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),1),OneD_Bin(List(OneD_Item(3)),OneD_Capacity(7),2)))
  }

  "The First Fit strategy" should "use a number of bins that is greater than or equals the lower bound " +
    "and lesser than or equals the number of bins used by the naive strategy" in {
    val items = "163841689525773"
    val capacity = OneD_Capacity(10)

    val lowerBound = OneDimensionalBinPackingSolver.lowerBound(items, capacity)
    val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)
    val ffBins = OneDimensionalBinPackingSolver.ff(items, capacity)

    ffBins.size should be >= lowerBound
    ffBins.size should be <= naiveBins.size
  }

  it should "pack each item in the first bin which remaining capacity is greater than the item size" in {
    val items = "683"
    val capacity = OneD_Capacity(10)

    val ffBins = OneDimensionalBinPackingSolver.ff(items, capacity)

    ffBins should equal (List(OneD_Bin(List(OneD_Item(6),OneD_Item(3)),OneD_Capacity(1),0),OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),1)))
  }

  it should "pack each item in the first bin which remaining capacity equals the item size" in {
    val items = "684"
    val capacity = OneD_Capacity(10)

    val ffBins = OneDimensionalBinPackingSolver.ff(items, capacity)

    ffBins should equal (List(OneD_Bin(List(OneD_Item(6),OneD_Item(4)),OneD_Capacity(0),0),OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),1)))
  }

  it should "pack each item into a new bin if no bin has enough room for the item" in {
    val items = "685"
    val capacity = OneD_Capacity(10)

    val ffBins = OneDimensionalBinPackingSolver.ff(items, capacity)

    ffBins should equal (List(OneD_Bin(List(OneD_Item(6)),OneD_Capacity(4),0),OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),1),OneD_Bin(List(OneD_Item(5)),OneD_Capacity(5),2)))
  }

  "The First Fit Decreasing strategy" should "use a number of bins that is greater than or equals the lower bound " +
    "and lesser than or equals the number of bins used by the naive strategy" in {
    val items = "163841689525773"
    val capacity = OneD_Capacity(10)

    val lowerBound = OneDimensionalBinPackingSolver.lowerBound(items, capacity)
    val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)
    val ffdBins = OneDimensionalBinPackingSolver.ffd(items, capacity)

    ffdBins.size should be >= lowerBound
    ffdBins.size should be <= naiveBins.size
  }

  it should "sort the list of items by decreasing size then " +
    "pack each item in the first bin which remaining capacity is greater than the item size" in {
    val items = "683"
    val capacity = OneD_Capacity(10)

    val ffdBins = OneDimensionalBinPackingSolver.ffd(items, capacity)

    ffdBins should equal (List(OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),0),OneD_Bin(List(OneD_Item(6),OneD_Item(3)),OneD_Capacity(1),1)))
  }

  it should "sort the list of items by decreasing size then" +
    "pack each item in the first bin which remaining capacity equals the item size" in {
    val items = "684"
    val capacity = OneD_Capacity(10)

    val ffdBins = OneDimensionalBinPackingSolver.ffd(items, capacity)

    ffdBins should equal (List(OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),0),OneD_Bin(List(OneD_Item(6),OneD_Item(4)),OneD_Capacity(0),1)))
  }

  it should "sort the list of items by decreasing size then" +
    "pack each item into a new bin if no bin has enough room for the item" in {
    val items = "685"
    val capacity = OneD_Capacity(10)

    val ffdBins = OneDimensionalBinPackingSolver.ffd(items, capacity)

    ffdBins should equal (List(OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),0),OneD_Bin(List(OneD_Item(6)),OneD_Capacity(4),1),OneD_Bin(List(OneD_Item(5)),OneD_Capacity(5),2)))
  }

  "The Best Fit strategy" should "use a number of bins that is greater than or equals the lower bound " +
    "and lesser than or equals the number of bins used by the naive strategy" in {
    val items = "163841689525773"
    val capacity = OneD_Capacity(10)

    val lowerBound = OneDimensionalBinPackingSolver.lowerBound(items, capacity)
    val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)
    val bfBins = OneDimensionalBinPackingSolver.bf(items, capacity)

    bfBins.size should be >= lowerBound
    bfBins.size should be <= naiveBins.size
  }

  it should "pack each item into the bin which remaining capacity will be the lowest after the item is packed" in {
    val items = "681"
    val capacity = OneD_Capacity(10)

    val bfBins = OneDimensionalBinPackingSolver.bf(items, capacity)

    bfBins should equal (List(OneD_Bin(List(OneD_Item(6)),OneD_Capacity(4),0),OneD_Bin(List(OneD_Item(8),OneD_Item(1)),OneD_Capacity(1),1)))
  }

  it should "pack each item into the bin which remaining capacity will be the lowest after the item is packed" +
    "even in case the remaining capacity equals 0" in {
    val items = "682"
    val capacity = OneD_Capacity(10)

    val bfBins = OneDimensionalBinPackingSolver.bf(items, capacity)

    bfBins should equal (List(OneD_Bin(List(OneD_Item(6)),OneD_Capacity(4),0),OneD_Bin(List(OneD_Item(8),OneD_Item(2)),OneD_Capacity(0),1)))
  }

  it should "pack each item into a new bin if no bin has enough room for the item" in {
    val items = "685"
    val capacity = OneD_Capacity(10)

    val bfBins = OneDimensionalBinPackingSolver.bf(items, capacity)

    bfBins should equal (List(OneD_Bin(List(OneD_Item(6)),OneD_Capacity(4),0),OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),1),OneD_Bin(List(OneD_Item(5)),OneD_Capacity(5),2)))
  }

  "The Best Fit Decreasing strategy" should "use a number of bins that is greater than or equals the lower bound " +
    "and lesser than or equals the number of bins used by the naive strategy" in {
    val items = "163841689525773"
    val capacity = OneD_Capacity(10)

    val lowerBound = OneDimensionalBinPackingSolver.lowerBound(items, capacity)
    val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)
    val bfdBins = OneDimensionalBinPackingSolver.bfd(items, capacity)

    bfdBins.size should be >= lowerBound
    bfdBins.size should be <= naiveBins.size
  }

  it should "sort the list of items by decreasing size then" +
    "pack each item into the bin which remaining capacity will be the lowest after the item is packed" in {
    val items = "681"
    val capacity = OneD_Capacity(10)

    val bfdBins = OneDimensionalBinPackingSolver.bfd(items, capacity)

    bfdBins should equal (List(OneD_Bin(List(OneD_Item(8),OneD_Item(1)),OneD_Capacity(1),0),OneD_Bin(List(OneD_Item(6)),OneD_Capacity(4),1)))
  }

  it should "sort the list of items by decreasing size then" +
    "pack each item into the bin which remaining capacity will be the lowest after the item is packed" +
    "even in case the remaining capacity equals 0" in {
    val items = "682"
    val capacity = OneD_Capacity(10)

    val bfdBins = OneDimensionalBinPackingSolver.bfd(items, capacity)

    bfdBins should equal (List(OneD_Bin(List(OneD_Item(8),OneD_Item(2)),OneD_Capacity(0),0),OneD_Bin(List(OneD_Item(6)),OneD_Capacity(4),1)))
  }

  it should "sort the list of items by decreasing size then " +
    "pack each item into a new bin if no bin has enough room for the item" in {
    val items = "685"
    val capacity = OneD_Capacity(10)

    val bfdBins = OneDimensionalBinPackingSolver.bfd(items, capacity)

    bfdBins should equal (List(OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),0),OneD_Bin(List(OneD_Item(6)),OneD_Capacity(4),1),OneD_Bin(List(OneD_Item(5)),OneD_Capacity(5),2)))
  }

  "The Worst Fit strategy" should "use a number of bins that is greater than or equals the lower bound " +
    "and lesser than or equals the number of bins used by the naive strategy" in {
    val items = "163841689525773"
    val capacity = OneD_Capacity(10)

    val lowerBound = OneDimensionalBinPackingSolver.lowerBound(items, capacity)
    val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)
    val wfBins = OneDimensionalBinPackingSolver.wf(items, capacity)

    wfBins.size should be >= lowerBound
    wfBins.size should be <= naiveBins.size
  }

  it should "pack each item into the bin which remaining capacity will be the lowest after the item is packed" in {
    val items = "681"
    val capacity = OneD_Capacity(10)

    val wfBins = OneDimensionalBinPackingSolver.wf(items, capacity)

    wfBins should equal (List(OneD_Bin(List(OneD_Item(6),OneD_Item(1)),OneD_Capacity(3),0),OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),1)))
  }

  it should "pack each item into the bin which remaining capacity will be the lowest after the item is packed" +
    "even in case the remaining capacity equals 0" in {
    val items = "684"
    val capacity = OneD_Capacity(10)

    val wfBins = OneDimensionalBinPackingSolver.wf(items, capacity)

    wfBins should equal (List(OneD_Bin(List(OneD_Item(6),OneD_Item(4)),OneD_Capacity(0),0),OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),1)))
  }

  it should "pack each item into a new bin if no bin has enough room for the item" in {
    val items = "685"
    val capacity = OneD_Capacity(10)

    val wfBins = OneDimensionalBinPackingSolver.wf(items, capacity)

    wfBins should equal (List(OneD_Bin(List(OneD_Item(6)),OneD_Capacity(4),0),OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),1),OneD_Bin(List(OneD_Item(5)),OneD_Capacity(5),2)))
  }

  "The Worst Fit Decreasing strategy" should "use a number of bins that is greater than or equals the lower bound " +
    "and lesser than or equals the number of bins used by the naive strategy" in {
    val items = "163841689525773"
    val capacity = OneD_Capacity(10)

    val lowerBound = OneDimensionalBinPackingSolver.lowerBound(items, capacity)
    val naiveBins = OneDimensionalBinPackingSolver.naive(items, capacity)
    val wfdBins = OneDimensionalBinPackingSolver.wfd(items, capacity)

    wfdBins.size should be >= lowerBound
    wfdBins.size should be <= naiveBins.size
  }

  it should "sort the list of items by decreasing size then" +
    "pack each item into the bin which remaining capacity will be the lowest after the item is packed" in {
    val items = "681"
    val capacity = OneD_Capacity(10)

    val wfdBins = OneDimensionalBinPackingSolver.wfd(items, capacity)

    wfdBins should equal (List(OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),0),OneD_Bin(List(OneD_Item(6),OneD_Item(1)),OneD_Capacity(3),1)))
  }

  it should "sort the list of items by decreasing size then" +
    "pack each item into the bin which remaining capacity will be the lowest after the item is packed" +
    "even in case the remaining capacity equals 0" in {
    val items = "684"
    val capacity = OneD_Capacity(10)

    val wfdBins = OneDimensionalBinPackingSolver.wfd(items, capacity)

    wfdBins should equal (List(OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),0),OneD_Bin(List(OneD_Item(6),OneD_Item(4)),OneD_Capacity(0),1)))
  }

  it should "sort the list of items by decreasing size then " +
    "pack each item into a new bin if no bin has enough room for the item" in {
    val items = "685"
    val capacity = OneD_Capacity(10)

    val wfdBins = OneDimensionalBinPackingSolver.wfd(items, capacity)

    wfdBins should equal (List(OneD_Bin(List(OneD_Item(8)),OneD_Capacity(2),0),OneD_Bin(List(OneD_Item(6)),OneD_Capacity(4),1),OneD_Bin(List(OneD_Item(5)),OneD_Capacity(5),2)))
  }
}
