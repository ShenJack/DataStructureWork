package com.sjjkk.datastructurework;

/**
 * sjjkk on 2017/12/14 in Beijing.
 */

public class Image implements Comparable<Image> {
    public String name = null;
    public int count = 0;
    public float rate = 0;

    public Image(String name, int count, float rate) {
        this.name = name;
        this.count = count;
        this.rate = rate;
    }

    @Override
    public int compareTo(Image o) {
        return name.compareTo(o.name);
    }
}
