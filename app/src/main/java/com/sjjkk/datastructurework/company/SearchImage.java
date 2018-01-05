package com.sjjkk.datastructurework.company;



import com.sjjkk.datastructurework.Image;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SearchImage {
    static List<Image> imageData = new ArrayList<>();

    public static void main(String[] args) {
        readText();
    }

    private static void readText() {
        String fileName = ".\\database.txt";
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(fileReader);


        while (scanner.hasNext()) {
            String name = scanner.next();
            int count = scanner.nextInt();
            float rate = scanner.nextFloat();
            imageData.add(new Image(name, count, rate));
        }
        AvlTree<Image> avlTree = new AvlTree<>();
        for (Image imageDatum : imageData) {
            avlTree.insert(imageDatum);
        }

        Image image;
        SearchResult result = avlTree.getNodeByName("airplanes");
        if (result != null) {
            image = (Image) result.avlNode.element;
            System.out.println("seccess");

        } else {
            System.out.println("error");
        }


    }

    private static void searchImage(String key) {

    }

}



