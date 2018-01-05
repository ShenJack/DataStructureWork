package com.sjjkk.datastructurework.company;

public class HashImage implements Comparable<HashImage> {
    public String name;
    public String fingerPrint;

    @Override
    public int compareTo(HashImage o) {
        return fingerPrint.compareTo(o.fingerPrint);
    }

    public HashImage(String name, String fingerPrint) {
        this.name = name;
        this.fingerPrint = fingerPrint;
    }
}
