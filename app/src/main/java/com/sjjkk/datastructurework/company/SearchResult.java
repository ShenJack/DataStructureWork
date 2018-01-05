package com.sjjkk.datastructurework.company;

public class SearchResult {
    public AvlTree.AvlNode avlNode = null;
    public int height = 0;

    public SearchResult(AvlTree.AvlNode avlNode, int height) {
        this.avlNode = avlNode;
        this.height = height;
    }
}
