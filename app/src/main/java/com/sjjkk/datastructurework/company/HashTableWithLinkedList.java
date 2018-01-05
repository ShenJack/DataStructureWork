package com.sjjkk.datastructurework.company;

import com.sjjkk.datastructurework.HashSearchResult;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class HashTableWithLinkedList {
    LinkedItem[] hashArray;
    int arraySize;//定义数组长度

    public HashTableWithLinkedList(int size) {//构造器，初始化
        arraySize = size;
        hashArray = new LinkedItem[arraySize];
    }

    public static int hash(String key) {
        int hash = 0;
        if (key.toCharArray().length > 0) {
            char val[] = key.toCharArray();

            for (int i = 0; i < key.toCharArray().length; i++) {
                hash = 31 * hash + val[i];
            }
            hash = hash;
        }
        return Math.abs(hash);
    }

    public void insert(HashImage item) {
        String key = item.fingerPrint;
        int hashCode = hash(key);
        int place = hashCode%arraySize;
        if (hashArray[place] == null) {
            hashArray[place] = new LinkedItem();
        }
        hashArray[place].insert(item);
        //链地址法
    }

    public boolean delete(HashImage item) {
        int hashCode = hash(item.fingerPrint);
        if (find(item.fingerPrint) != null) {
            int place = hashCode%arraySize;
            Iterator iterator = hashArray[place].imageArrayList.iterator();
            while (iterator.hasNext()){
                if(iterator.next() == item){
                    iterator.remove();
                }
            }
            return true;
        }else {
            System.out.println("Does Not Exist");
            return false;
        }
    }

    //          查找
    public HashSearchResult find(String key) {
        int height = 1;
        int hashCode = hash(key);
        hashCode = Math.abs(hashCode);

        int place = hashCode % arraySize;
        if (hashArray[place] == null) {
            hashArray[place] = new LinkedItem();
        }

//        非空
        HashImage result = hashArray[place].find(hashCode);
        if (result != null) {
            return new HashSearchResult(result,height);
        }else {
            return new HashSearchResult(null,height);
        }
    }

}

