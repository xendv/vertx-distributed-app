package common;

public class Clan {
    private String name;
    private Integer limit;

    public Clan(String name){
        setName(name);
    }

    public Clan(String name, int limit){
        setName(name);
        setLimit(limit);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Integer getLimit(){
        return limit;
    }
}
