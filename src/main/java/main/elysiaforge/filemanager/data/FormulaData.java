package main.elysiaforge.filemanager.data;

import java.util.List;

public class FormulaData {
    private final String id;
    private final String group;
    private final String produce;
    private final int number;
    private final List<String> item;
    private final int money;
    public FormulaData(String id, String group, String produce, int number, List<String> item, int money) {
        this.id = id;
        this.group = group;
        this.produce = produce;
        this.number = number;
        this.item = item;
        this.money = money;
    }
    public String getId() {
        return id;
    }
    public String getGroup() {
        return group;
    }
    public String getProduce() {
        return produce;
    }
    public int getNumber() {
        return number;
    }
    public List<String> getItem() {
        return item;
    }
    public int getMoney() {
        return money;
    }
}
