package com.sjjkk.datastructurework.company;

import com.sjjkk.datastructurework.HashSearchResult;

public class HashTable {
    HashImage[] hashArray;
    int arraySize;//定义数组长度

    public HashTable(int size) {//构造器，初始化
        arraySize = size;
        hashArray = new HashImage[arraySize];
    }

    public int hash(String key) {
        int hash = 0;
        if (key.toCharArray().length > 0) {
            char val[] = key.toCharArray();

            for (int i = 0; i < key.toCharArray().length; i++) {
                hash = 31 * hash + val[i];
            }
        }
        return hash;
    }

    public void insert(HashImage item) {
        String key = item.fingerPrint;
        int hashCode = hash(key);
        hashCode %= arraySize;
        hashCode = Math.abs(hashCode);
        //顺序探测法
        while (hashArray[hashCode] != null) {
            ++hashCode;
            hashCode %= arraySize;
        }
        hashArray[hashCode] = item;
    }

    public HashImage delete(Item key) {
        int hashCode = hash(key.image.fingerPrint);
        hashCode = Math.abs(hashCode);
        hashCode %= arraySize;

        while (hashArray[hashCode] != null) {
            if (hashArray[hashCode].fingerPrint.equals(key.getKey())) {
                HashImage temp = hashArray[hashCode];
                hashArray[hashCode] = null;
                return temp;
            }
            ++hashCode;
            hashCode %= arraySize;
        }
        return null;
    }

    //          查找
    public HashSearchResult find(String key) {
        int height = 1;
        int hashCode = hash(key);
        hashCode = Math.abs(hashCode);
        hashCode %= arraySize;
//        非空
        while (hashArray[hashCode] != null) {
//        key相等
            if (hashArray[hashCode].fingerPrint.equals(key))
                return new HashSearchResult(hashArray[hashCode], height);
            ++hashCode;
            height++;
            hashCode %= arraySize;
        }
        return new HashSearchResult(null,height);
    }

    //打印
    public void show() {
        for (int i = 0; i < arraySize; i++) {
            if (hashArray[i] != null)
                System.out.print(hashArray[i].fingerPrint + " ");
            else
                System.out.print("* ");
        }
    }

}

