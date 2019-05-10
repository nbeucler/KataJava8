package org.codingdojo.java8;

import com.sun.deploy.util.StringUtils;
import jdk.nashorn.internal.ir.LiteralNode;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.assertj.core.data.MapEntry.entry;


public class GildedRoseStreamTest {
    private GildedRoseCompany company = new GildedRoseCompany();

    @Test
    public void shouldFilterBySellEquals10()  {
        //Given
        GildedRose shop = company.shop();

        //When
        List<Item> items = shop.getItems().stream().filter(item -> item.getSellIn() == 10).collect(Collectors.toList());

        //Then
        assertThat(items).hasSize(2).extracting("name").containsOnly("+5 Dexterity Vest", "Conjured Mana Cake");
    }

    @Test
    public void shouldGetItemsQuality()  {
        //Given
        GildedRose shop = company.shop();

        //When
        List<Integer> itemsQuality = shop.getItems().stream().map(Item::getQuality).collect(Collectors.toList());

        //Then
        assertThat(itemsQuality).containsExactlyInAnyOrder(10,0,10,25,20,30,20);
    }

    @Test
    public void shouldSortByName()  {
        //Given
        GildedRose shop = company.shop();

        //When
        List<String> itemsName = shop.getItems().stream().map(Item::getName).sorted().collect(Collectors.toList());

        //Then
        assertThat(itemsName).containsExactly("+5 Dexterity Vest",
                "+5 Dexterity Vest",
                "Aged Brie",
                "Backstage passes to a TAFKAL80ETC concert",
                "Backstage passes to a TAFKAL80ETC concert",
                "Conjured Mana Cake",
                "Sulfuras, Hand of Ragnaros");
    }

    @Test
    public void shouldSortBySellInDescending()  {
        //Given
        GildedRose shop = company.shop();

        //When
        List<Integer> itemsSellIn =
                shop.getItems().stream().map(Item::getSellIn).sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        //Then
        assertThat(itemsSellIn).containsExactly(50,20,15,10,10,5,0);
    }

    @Test
    public void shouldGetTop3Expensive()  {
        //Given
        GildedRose shop = company.shop();

        //When
        List<Item> items =
                shop.getItems().stream().sorted(Comparator.comparing(Item::getSellIn).reversed()
        ).limit(3).collect(Collectors.toList());

        //Then
        assertThat(items).extracting("name")
                .containsOnly("Sulfuras, Hand of Ragnaros",
                        "Aged Brie",
                        "Backstage passes to a TAFKAL80ETC concert");
    }

    @Test
    public void shouldGetQualityDistinct()  {
        //Given
        GildedRose shop = company.shop();

        //When
        List<Integer> itemsQuality = shop.getItems().stream().map(Item::getQuality).distinct().collect(Collectors.toList());

        //Then
        assertThat(itemsQuality).containsExactlyInAnyOrder(0,10,20,25,30);
    }

    @Test
    public void shouldCountTotalSellIn()  {
        //Given
        GildedRose shop = company.shop();

        //When
        Integer total = shop.getItems().stream().map(Item::getSellIn).reduce(0, Integer::sum);

        //Then
        assertThat(total).isEqualTo(110);
    }

    @Test
    public void shouldGetMaxQuality()  {
        //Given
        GildedRose shop = company.shop();

        //When
        Integer maxQuality =
                shop.getItems().stream().map(Item::getQuality).max(Integer::compareTo).orElse(0);

        //Then
        assertThat(maxQuality).isEqualTo(30);
    }

    @Test
    public void shouldGetMinValue()  {
        //Given
        GildedRose shop = company.shop();

        //When
        Integer minSellIn = shop.getItems().stream().map(Item::getSellIn).min(Integer::compareTo).orElse(-100);

        //Then
        assertThat(minSellIn).isZero();
    }

    @Test
    public void shouldGetFirst() throws Exception {
        //Given
        GildedRose shop = company.shop();

        //When
        Item firstItem = shop.getItems().stream().findFirst().orElseThrow(Exception::new);

        //Then
        assertThat(firstItem.getName()).isEqualTo("+5 Dexterity Vest");
        assertThat(firstItem.getSellIn()).isEqualTo(10);
        assertThat(firstItem.getQuality()).isEqualTo(10);
    }

    @Test
    public void isThereOneWithNoQuality()  {
        //Given
        GildedRose shop = company.shop();

        //When
        boolean hasItemWithNoQuality = shop.getItems().stream().anyMatch(item -> item.getQuality() == 0);

        //Then
        assertThat(hasItemWithNoQuality).isTrue();
    }

    @Test
    public void areEveryItemNamed()  {
        //Given
        GildedRose shop = company.shop();

        //When
        boolean allItemsHaveName =
                shop.getItems().stream().allMatch(item -> null != item.getName()&& !"".equals(item.getName()) &&
                        !" ".equals(item.getName()));

        //Then
        assertThat(allItemsHaveName).isTrue();
    }

    @Test
    public void shouldCSVItems()  {
        //Given
        GildedRose shop = company.shop();

        //When
        String names = shop.getItems().stream().map(Item::getName).collect(Collectors.joining(",", "[","]"));

        //Then
        assertThat(names).isEqualTo("[+5 Dexterity Vest,+5 Dexterity Vest,Aged Brie,Sulfuras, Hand of Ragnaros,Backstage passes to a TAFKAL80ETC concert,Backstage passes to a TAFKAL80ETC concert,Conjured Mana Cake]");
    }

    @Test
    public void shouldGroupByQuality()  {
        //Given
        GildedRose shop = company.shop();

        //When
        Map<Integer, Long> qualitiesItems = shop.getItems().stream().collect(Collectors.groupingBy(Item::getQuality,
                Collectors.counting()));

        //Then
        assertThat(qualitiesItems).hasSize(5).containsOnly(
                entry(0, 1L),
                entry(10, 2L),
                entry(20, 2L),
                entry(25, 1L),
                entry(30, 1L));
    }

    @Test
    public void shouldGetAverageSellIn()  {
        //Given
        GildedRose shop = company.shop();

        //When
        double averageSellIn = shop.getItems().stream().collect(Collectors.averagingDouble(Item::getSellIn));

        //Then
        assertThat(averageSellIn).isEqualTo(15.71, offset(0.01));
    }

    @Test
    public void shouldGetQualityFromAllShops()  {
        //Given
        List<GildedRose> shops = company.getShops();

        //When
        List<Integer> qualities =
                shops.stream().map(GildedRose::getItems).flatMap(List::stream
        ).map(Item::getQuality).collect(Collectors.toList());

        //Then
        assertThat(qualities).containsOnly(10,0,10,25,20,30,20,0,0,50,49,20,1);
    }

    @Test
    public void howMuchToBuyAllItems()  {
        //Given
        List<GildedRose> shops = company.getShops();

        //When
        Integer priceOfAllItems =
                shops.stream().map(GildedRose::getItems).flatMap(List::stream).mapToInt(Item::getSellIn).sum();

        //Then
        assertThat(priceOfAllItems).isEqualTo(155);
    }

    @Test
    public void getItemsNotOnSaleInAnyShops()  {
        //Given
        List<GildedRose> shops = company.getShops();

        //When
        List<String> notSellingItems =
                shops.stream().map(GildedRose::getItems).flatMap(List::stream).filter(item -> item.getSellIn() == 0).map(Item::getName).collect(Collectors.toList());

        //Then
        assertThat(notSellingItems).containsOnly("+5 Dexterity Vest", "+5 Dexterity Vest", "Backstage passes to a TAFKAL80ETC concert");
    }
}
