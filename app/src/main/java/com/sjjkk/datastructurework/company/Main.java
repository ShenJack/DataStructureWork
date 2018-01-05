package com.sjjkk.datastructurework.company;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static AvlTree<HashImage> imageAvlTree = new AvlTree<>();
    private static ArrayList<HashImage> imageData;


    public static void main(String[] args) {
        String BASE_URL = "";
        BuildFingerPrintStoreWithLinear(BASE_URL);
        // TODO: 2017/12/11  Select Image (Matisse)
        Image image = selectImage();
        findSimilarImage(new HashImage("", ""));
    }

    private static Image selectImage() {

        return null;
    }

    public static void checkImage(String baseUrl) {
//        查看文件目录对不对？？
    }

    public static Image scaleDownPicAndShow(Image pic) {
        // TODO: 2017/12/11 scaleDown and show it in UI
//        后台处理图片
        return null;
    }

    public static void processPicture(Image pic) {

    }

    public static String calculateFingerPrint(HashImage pic) {
        return null;
    }

    public static String findSimilarImage(HashImage pic) {
//        根据选中的图片处理并查找相似的图片
//        return Image Url
        String fingerPrint = calculateFingerPrint(pic);
        pic.fingerPrint = fingerPrint;

        return null;
    }

    public static void BuildFingerPrintStoreWithLinear(String baseUrl) {
        String fileName = ".\\filename.txt";
        String fingetPrint = ".\\fingerprint.txt";
        FileReader fileNameReader = null;
        try {
            fileNameReader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner fileNameScanner = new Scanner(fileNameReader);
        FileReader fingerPrintReader = null;
        try {
            fingerPrintReader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner fingerPrintScanner = new Scanner(fingerPrintReader);


        while (fileNameScanner.hasNext()) {
            String name = fileNameScanner.next();
            int count = fileNameScanner.nextInt();
            float rate = fileNameScanner.nextFloat();
            String fingerPrint = fingerPrintScanner.next();
            imageData.add(new HashImage(name,fingerPrint));
        }
//        根据每张图片的指纹建立哈希表
        for (HashImage imageDatum : imageData) {
            imageAvlTree.insert(imageDatum);
        }
    }


}