package dto;

public class ObjectDTO {
    private String name;
    private int ttl;
    public long timeAdded;

    public ObjectDTO(String name, int ttl, long timeAdded) {
        this.name = name;
        this.ttl = ttl;
        this.timeAdded = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }
    public long getTimeAdded() {
        return timeAdded;
    }
}
