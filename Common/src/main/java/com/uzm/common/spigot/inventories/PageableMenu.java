package com.uzm.common.spigot.inventories;

import com.google.common.collect.Lists;
import com.uzm.common.spigot.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JotaMPê (UzmStudio)
 */

public class PageableMenu {

    protected String name;
    protected int rows;
    protected int[] avaliableSlots;
    protected List<Menu> menus = Lists.newArrayList();
    protected Map<Integer, ItemStack> fixed = new HashMap<>();
    protected Map<ItemStack, Object> attached = new HashMap<>();
    protected Map<Menu, Integer> id = new HashMap<>();
    protected int currentPage = 1;

    public int previousPage = 45, nextPage = 53, emptySlot = 22;
    public String previousStack = "ARROW : 1 : display=&ePágina anterior", nextStack = "ARROW : 1 : display=&ePágina posterior", emptyStack = null;


    public PageableMenu(String name, int rows) {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.rows = rows > 6 ? 6 : Math.max(rows, 1);
    }

    public void open(Player player) {
        player.openInventory(menus.get(0).getInventory());
    }

    public void openPrevious(Player player, Inventory inv) {
        int currentPage = id.get(getCurrent(inv));
        if (currentPage == 1) {
            return;
        }

        player.openInventory(menus.get(currentPage - 2).getInventory());
    }

    public void openNext(Player player, Inventory inv) {
        int currentPage = id.get(getCurrent(inv));
        if (currentPage + 1 > menus.size()) {
            return;
        }

        player.openInventory(menus.get(currentPage).getInventory());
    }

    public PageableMenu setSlots(int... avaliableSlots) {
        this.avaliableSlots = avaliableSlots;
        return this;
    }

    public PageableMenu setPrevious(int slot, String stack) {
        this.previousPage = slot;
        this.previousStack = stack;
        return this;
    }

    public PageableMenu setNext(int slot, String stack) {
        this.nextPage = slot;
        this.nextStack = stack;
        return this;
    }

    public PageableMenu setPrevious(int slot) {
        this.previousPage = slot;
        return this;
    }

    public PageableMenu setNext(int slot) {
        this.nextPage = slot;
        return this;
    }

    public PageableMenu setEmptyItem(int slot, String stack) {
        this.emptySlot = slot;
        this.emptyStack = stack;
        return this;
    }

    public PageableMenu setItem(int slot, ItemStack stack) {
        this.fixed.put(slot, stack);
        return this;
    }

    public void attachObject(ItemStack item, Object value) {
        this.attached.put(item, value);
    }

    public Object getAttached(ItemStack item) {
        return this.attached.get(item);
    }

    public Map<ItemStack, Object> getAttached() {
        return this.attached;
    }

    public PageableMenu setItems(List<ItemStack> items) {

        this.menus.forEach(menu -> menu.getInventory().getViewers().forEach(HumanEntity::closeInventory));
        this.menus.clear();
        List<List<ItemStack>> splitted = split(items);
        if (splitted.isEmpty()) {
            splitted.add(new ArrayList<>());
        }
        ItemStack[] copyContents = null;
        for (int i = 0; i < splitted.size(); i++) {
            List<ItemStack> pageItems = splitted.get(i);
            Menu menu = new Menu(this.name.replace("{page}", String.valueOf((i + 1))), rows);
            if (i == 0) {
                this.fixed.forEach(menu::setItem);
                copyContents = menu.getContents();
            } else {
                menu.getInventory().setContents(copyContents);
            }
            for (int s = 0; s < pageItems.size(); s++) menu.setItem(avaliableSlots[s], pageItems.get(s));

            if (items.size() == 0) if (emptyStack != null) menu.setItem(emptySlot, new ItemBuilder(emptyStack).build());
            if (splitted.size() > 1 && (i + 1) < splitted.size())
                menu.setItem(nextPage, new ItemBuilder(nextStack.replace("{page}", String.valueOf((i + 1)))).build());
            if (i > 0)
                menu.setItem(previousPage, new ItemBuilder(previousStack.replace("{page}", String.valueOf((i + 1)))).build());
            this.menus.add(menu);
            this.id.put(menu, i + 1);

        }
        return this;
    }

    public PageableMenu updateItems(List<ItemStack> items) {
        List<List<ItemStack>> splitted = split(items);
        if (splitted.isEmpty()) {
            splitted.add(new ArrayList<>());
        }
        this.id.clear();
        for (int i = 0; i < splitted.size(); i++) {
            List<ItemStack> pageItems = splitted.get(i);
            Menu menu = (i + 1) > this.menus.size() ? new Menu(this.name.replace("{page}", String.valueOf((i + 1))), rows) : this.menus.get(i);
            menu.clear();
            for (int s = 0; s < pageItems.size(); s++) menu.setItem(avaliableSlots[s], pageItems.get(s));
            this.fixed.forEach(menu::setItem);
            if (items.size() == 0) if (emptyStack != null) menu.setItem(emptySlot, new ItemBuilder(emptyStack).build());
            if (splitted.size() > 1 && i != (splitted.size() - 1))
                this.setItem(nextPage, new ItemBuilder(nextStack.replace("{page}", String.valueOf((i + 1)))).build());
            if (i > 1)
                this.setItem(previousPage, new ItemBuilder(previousStack.replace("{page}", String.valueOf((i + 1)))).build());

            if (!this.menus.contains(menu)) this.menus.add(menu);
            this.id.put(menu, i + 1);
        }
        if (splitted.size() < menus.size()) menus.subList(0, splitted.size());
        return this;
    }

    public List<List<ItemStack>> split(List<ItemStack> items) {
        List<List<ItemStack>> list = new ArrayList<>();
        List<ItemStack> toadd = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            toadd.add(items.get(i));
            if ((i + 1) % avaliableSlots.length == 0) {
                list.add(toadd);
                toadd = new ArrayList<>();
            }
            if ((i + 1) == items.size()) {
                if (!toadd.isEmpty()) list.add(toadd);
                break;
            }
        }
        return list;
    }

    public Inventory getCurrentInventory() {
        return menus.get(currentPage - 1).getInventory();
    }

    public Menu getCurrent(Inventory inv) {
        for (Menu menu : menus) {
            if (menu.getInventory().equals(inv)) {
                return menu;
            }
        }

        return menus.get(0);
    }
}
