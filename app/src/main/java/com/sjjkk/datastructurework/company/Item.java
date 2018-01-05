package com.sjjkk.datastructurework.company;

//定义哈希表中存放的数据类型，可以为任意的类型
public class Item {
    HashImage image;

    public Item(HashImage image) {
        this.image = image;
    }


    public String getKey() {
        return image.fingerPrint;
    }
}
