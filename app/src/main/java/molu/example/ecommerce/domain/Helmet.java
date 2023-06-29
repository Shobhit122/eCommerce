package molu.example.ecommerce.domain;

import java.util.List;
import java.util.Map;

public class Helmet {
    private String name;
    private int mrp;
    private int price;
    private List<String> tags;
    private String url;
    private int stock;
    private String pid;
    private List<String> features;

    private Map<String, Integer> size;

    public Helmet() {
    }

    public Helmet(String name, int mrp, int price, List<String> tags, String url, int stock, String pid, List<String> features, Map<String, Integer> size) {
        this.name = name;
        this.mrp = mrp;
        this.price = price;
        this.tags = tags;
        this.url = url;
        this.stock = stock;
        this.pid = pid;
        this.features = features;
        this.size = size;
    }

    public Map<String, Integer> getSize() {
        return size;
    }

    public void setSize(Map<String, Integer> size) {
        this.size = size;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
