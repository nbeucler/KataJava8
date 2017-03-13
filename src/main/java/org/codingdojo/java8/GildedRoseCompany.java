package org.codingdojo.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Julien on 22/02/2017.
 */
public class GildedRoseCompany {

    final private List<GildedRose> shops = new ArrayList<>();

    public GildedRoseCompany() {
        List<Item> itemShop1Stream = Arrays.asList(
                new Item("+5 Dexterity Vest", 10, 10),
                new Item("+5 Dexterity Vest", 0, 0),
                new Item("Aged Brie", 20, 10),
                new Item("Sulfuras, Hand of Ragnaros", 50, 25),
                new Item("Backstage passes to a TAFKAL80ETC concert", 15, 20),
                new Item("Backstage passes to a TAFKAL80ETC concert", 5, 30),
                new Item("Conjured Mana Cake", 10, 20));

        List<Item> itemShop2Stream = Arrays.asList(
                new Item("+5 Dexterity Vest", 10, 0),
                new Item("+5 Dexterity Vest", 0, 0),
                new Item("Aged Brie", 20, 50),
                new Item("Backstage passes to a TAFKAL80ETC concert", 5, 49),
                new Item("Backstage passes to a TAFKAL80ETC concert", 0, 20),
                new Item("Conjured Mana Cake", 10, 1));

        GildedRose shop1 = new GildedRose(itemShop1Stream);
        GildedRose shop2 = new GildedRose(itemShop2Stream);

        shops.add(shop1);
        shops.add(shop2);
    }

    public GildedRose shop() {
        return shops.get(0);
    }

    public List<GildedRose> getShops() {
        return shops;
    }
}
