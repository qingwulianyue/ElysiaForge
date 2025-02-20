package main.elysiaforge.filemanager.data;

import java.util.List;

public class FormulaData {
    private final String id;
    private final String name;
    private final String group;
    private final String produce;
    private final int number;
    private final boolean permission;
    private final List<String> item;
    private final int money;
    public FormulaData(String id, String name, String group, String produce, int number, boolean permission, List<String> item, int money) {
        this.id = id;
        this.name = name;
        this.group = group;
        this.produce = produce;
        this.number = number;
        this.permission = permission;
        this.item = item;
        this.money = money;
    }
    public String getId() {
        return id;
    }
    public String getName(){ return name; }
    public String getGroup() {
        return group;
    }
    public String getProduce() {
        return produce;
    }
    public int getNumber() {
        return number;
    }
    public boolean isPermission() {
        return permission;
    }
    public List<String> getItem() {
        return item;
    }
    public int getMoney() {
        return money;
    }
}
