package com.uzm.common.spigot.inventories;

import com.google.common.collect.Lists;
import com.uzm.common.spigot.items.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */

public abstract class ProgressiveMenu {

    protected String name;
    protected int rows;
    protected int endColumn;
    protected int startColumn;
    protected int endRow;
    protected int startRow;
    protected int amountOfPages;

    protected List<Menu> menus = Lists.newArrayList();
    protected Map<Integer, ItemStack> fixed = new HashMap<>();
    protected Map<ItemStack, Object> attached = new HashMap<>();
    protected Map<Menu, Integer> id = new HashMap<>();
    protected int currentPage = 1;


    public int previousPage = 45, nextPage = 53, emptySlot = 22;
    public String previousStack = "INK_SACK:8 : 1 : nome>&aPágina {page}", nextStack = "INK_SACK:10 : 1 : nome>&aPágina {page}", emptyStack = null;


    public ProgressiveMenu(String name, int rows) {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.rows = rows > 6 ? 6 : Math.max(rows, 1);
    }

    public void setEndColumn(int endColumn) {
        this.endColumn = endColumn;
    }

    public int getEndColumn() {
        return this.endColumn;
    }

    public void setStartColumn(int startColumn) {
        this.startColumn = startColumn;
    }

    public int getStartColumn() {
        return this.startColumn;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getStartRow() {
        return this.startRow;
    }

    public int getAmountOfPages() {
        return this.amountOfPages;
    }

    public int getMaxColumns() {
        return this.endColumn - this.startColumn;
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


    public ProgressiveMenu setPrevious(int slot, String stack) {
        this.previousPage = slot;
        this.previousStack = stack;
        return this;
    }

    public ProgressiveMenu setNext(int slot, String stack) {
        this.nextPage = slot;
        this.nextStack = stack;
        return this;
    }

    public ProgressiveMenu setEmptyItem(int slot, String stack) {
        this.emptySlot = slot;
        this.emptyStack = stack;
        return this;
    }

    public ProgressiveMenu setItem(int slot, ItemStack stack) {
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

    public ProgressiveMenu setItems(List<ProgressiveItem> items) {

        this.menus.forEach(menu -> menu.getInventory().getViewers().forEach(HumanEntity::closeInventory));
        this.menus.clear();

        this.amountOfPages = Math.max(1, (items.size() - getMaxColumns()) + 1);

        ItemStack[] copyContents = null;
        for (int i = 0; i < getAmountOfPages(); i++) {
            List<ProgressiveItem> itens = items.size() >= getMaxColumns() ? (List<ProgressiveItem>) copyOfList(items, i, getMaxColumns() + i) : (List<ProgressiveItem>) copyOfList(items, 0, items.size());
            Menu menu = new Menu(this.name.replace("{page}", String.valueOf((i + 1))), rows);

            if (i == 0) {
                this.fixed.forEach(menu::setItem);
                copyContents = menu.getContents();
            } else {
                menu.getInventory().setContents(copyContents);
            }

            if (items.size() == 0) if (emptyStack != null) menu.setItem(emptySlot, new ItemBuilder(emptyStack).build());
            itens.forEach(item -> {
                int posX = itens.indexOf(item) + 1;
                menu.setItem(toOrdinalIndex((startColumn + posX), item.getPosY()), item.getPrimaryItem());
                menu.setItem(toOrdinalIndex((startColumn + posX), (endRow + 1)), item.getSecondaryItem());
            });
            if (getAmountOfPages() > 1 && (i + 1) < getAmountOfPages())
                menu.setItem(nextPage, new ItemBuilder(nextStack.replace("{page}", String.valueOf((i + 1)))).build());
            if (i > 0)
                menu.setItem(previousPage, new ItemBuilder(previousStack.replace("{page}", String.valueOf((i + 1)))).build());
            this.menus.add(menu);
            this.id.put(menu, i + 1);
        }

        return this;
    }


    private List<?> copyOfList(List<?> list, int start, int end) {
        return new ArrayList<>(list).subList(start, end);
    }

    private int toOrdinalIndex(int x, int y) {
        return (y - 1) * 9 + (x - 1);
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

    public static class ProgressiveItem {
        private ItemStack primaryItem;
        private ItemStack secondaryItem;
        private int posX;
        private int posY;

        public ProgressiveItem(ItemStack primaryItem, ItemStack secondaryItem, int posY) {
            this.primaryItem = primaryItem;
            this.secondaryItem = secondaryItem;
            this.posY = posY;
        }

        public ItemStack getPrimaryItem() {
            return primaryItem;
        }

        public ItemStack getSecondaryItem() {
            return secondaryItem;
        }

        public int getPosX() {
            return posX;
        }

        public void setPosX(int posX) {
            this.posX = posX;
        }

        public int getPosY() {
            return this.posY;
        }

    }

    public abstract void click(InventoryClickEvent event);

    public abstract void cancel();
}
