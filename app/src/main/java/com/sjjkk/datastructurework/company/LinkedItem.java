package com.sjjkk.datastructurework.company;

import java.util.ArrayList;
import java.util.List;

//链地址法
public class LinkedItem {
    List<HashImage> imageArrayList;


    public LinkedItem() {
        imageArrayList = new ArrayList<>();
    }

    public boolean insert(HashImage hashImage){
        if(find(HashTableWithLinkedList.hash(hashImage.fingerPrint))==null){
            imageArrayList.add(hashImage);
            return true;
        }else {
            return false;
        }
    }

    public HashImage find(int hashCode){
        hashCode = Math.abs(hashCode);

        for (HashImage hashImage : imageArrayList) {
            if(hashCode == HashTableWithLinkedList.hash(hashImage.fingerPrint)){
                return hashImage;
            }
        }
        return null;
    }
}
