package com.sjjkk.datastructurework.company;

import com.sjjkk.datastructurework.Image;

import java.lang.StringBuilder;

public class AvlTree<T extends Comparable<? super T>> {
    public static class AvlNode<T> {

        public T element;

        public AvlNode<T> left = null;

        public AvlNode<T> right = null;

        public int height;

        public AvlNode(T theElement) {
            this(theElement, null, null);
        }

        public AvlNode(T theElement, AvlNode<T> lt, AvlNode<T> rt) {
            element = theElement;
            left = lt;
            right = rt;
        }
    }

    public AvlNode<T> root;

    public int countInsertions;
    public int countSingleRotations;
    public int countDoubleRotations;

    public AvlTree() {
        root = null;

        countInsertions = 0;
        countSingleRotations = 0;
        countDoubleRotations = 0;
    }

    public int height(AvlNode<T> t) {
        return t == null ? -1 : t.height;
    }

    public int max(int a, int b) {
        if (a > b)
            return a;
        return b;
    }

    public boolean insert(T x) {
        try {
            root = insert(x, root);

            countInsertions++;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

   protected AvlNode<T> insert(T x, AvlNode<T> t) throws Exception {
        if (t == null)
//            插入
            t = new AvlNode<T>(x);
//        在左边
        else if (x.compareTo(t.element) < 0) {
//            递归调用 直到要插入的结点时空的
            t.left = insert(x, t.left);

//            插入成功
            if (height(t.left) - height(t.right) == 2) {
//                插入的节点和上一个节点对比，进行旋转
                if (x.compareTo(t.left.element) < 0) {
                    t = rotateWithLeftChild(t);
                    countSingleRotations++;
                } else {
                    t = doubleWithLeftChild(t);
                    countDoubleRotations++;
                }
            }
        } else if (x.compareTo(t.element) > 0) {
            t.right = insert(x, t.right);

            if (height(t.right) - height(t.left) == 2)
                if (x.compareTo(t.right.element) > 0) {
                    t = rotateWithRightChild(t);
                    countSingleRotations++;
                } else {
                    t = doubleWithRightChild(t);
                    countDoubleRotations++;
                }
        }else {

        }
//      高度等于左右最大的高度加一
        t.height = max(height(t.left), height(t.right)) + 1;
        return t;
    }

    protected AvlNode<T> rotateWithLeftChild(AvlNode<T> k2) {
        AvlNode<T> k1 = k2.left;

        k2.left = k1.right;
        k1.right = k2;

        k2.height = max(height(k2.left), height(k2.right)) + 1;
        k1.height = max(height(k1.left), k2.height) + 1;

        return (k1);
    }

    protected AvlNode<T> doubleWithLeftChild(AvlNode<T> k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    protected AvlNode<T> rotateWithRightChild(AvlNode<T> k1) {
        AvlNode<T> k2 = k1.right;

        k1.right = k2.left;
        k2.left = k1;

        k1.height = max(height(k1.left), height(k1.right)) + 1;
        k2.height = max(height(k2.right), k1.height) + 1;

        return (k2);
    }

    protected AvlNode<T> doubleWithRightChild(AvlNode<T> k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }















    private AvlNode<T> findMax(AvlNode<T> t) {
        if (t == null)
            return t;

        while (t.right != null)
            t = t.right;
        return t;
    }


    public void remove(T x) {
        root = remove(x, root);
    }

    public AvlNode<T> remove(T x, AvlNode<T> t) {
        if (t == null) {
            System.out.println("Sorry but you're mistaken, " + t + " doesn't exist in this tree :)\n");
            return null;
        }
        System.out.println("Remove starts... " + t.element + " and " + x);

        if (x.compareTo(t.element) < 0) {
            t.left = remove(x, t.left);
            int l = t.left != null ? t.left.height : 0;

            if ((t.right != null) && (t.right.height - l >= 2)) {
                int rightHeight = t.right.right != null ? t.right.right.height : 0;
                int leftHeight = t.right.left != null ? t.right.left.height : 0;

                if (rightHeight >= leftHeight)
                    t = rotateWithLeftChild(t);
                else
                    t = doubleWithRightChild(t);
            }
        } else if (x.compareTo(t.element) > 0) {
            t.right = remove(x, t.right);
            int r = t.right != null ? t.right.height : 0;
            if ((t.left != null) && (t.left.height - r >= 2)) {
                int leftHeight = t.left.left != null ? t.left.left.height : 0;
                int rightHeight = t.left.right != null ? t.left.right.height : 0;
                if (leftHeight >= rightHeight)
                    t = rotateWithRightChild(t);
                else
                    t = doubleWithLeftChild(t);
            }
        }
        else if (t.left != null) {
            t.element = findMax(t.left).element;
            remove(t.element, t.left);

            if ((t.right != null) && (t.right.height - t.left.height >= 2)) {
                int rightHeight = t.right.right != null ? t.right.right.height : 0;
                int leftHeight = t.right.left != null ? t.right.left.height : 0;

                if (rightHeight >= leftHeight)
                    t = rotateWithLeftChild(t);
                else
                    t = doubleWithRightChild(t);
            }
        } else
            t = (t.left != null) ? t.left : t.right;

        if (t != null) {
            int leftHeight = t.left != null ? t.left.height : 0;
            int rightHeight = t.right != null ? t.right.height : 0;
            t.height = Math.max(leftHeight, rightHeight) + 1;
        }
        return t;
    }

    public boolean contains(T x) {
        return contains(x, root);
    }

    protected boolean contains(T x, AvlNode<T> t) {
        if (t == null) {
            return false; // The node was not found

        } else if (x.compareTo(t.element) < 0) {
            return contains(x, t.left);
        } else if (x.compareTo(t.element) > 0) {
            return contains(x, t.right);
        }

        return true;
    }

    public boolean checkBalanceOfTree(AvlNode<Integer> current) {

        boolean balancedRight = true, balancedLeft = true;
        int leftHeight = 0, rightHeight = 0;

        if (current.right != null) {
            balancedRight = checkBalanceOfTree(current.right);
            rightHeight = getDepth(current.right);
        }

        if (current.left != null) {
            balancedLeft = checkBalanceOfTree(current.left);
            leftHeight = getDepth(current.left);
        }

        return balancedLeft && balancedRight && Math.abs(leftHeight - rightHeight) < 2;
    }

    public int getDepth(AvlNode<Integer> n) {
        int leftHeight = 0, rightHeight = 0;

        if (n.right != null)
            rightHeight = getDepth(n.right);
        if (n.left != null)
            leftHeight = getDepth(n.left);

        return Math.max(rightHeight, leftHeight) + 1;
    }

    public boolean checkOrderingOfTree(AvlNode<Integer> current) {
        if (current.left != null) {
            if (current.left.element.compareTo(current.element) > 0)
                return false;
            else
                return checkOrderingOfTree(current.left);
        } else if (current.right != null) {
            if (current.right.element.compareTo(current.element) < 0)
                return false;
            else
                return checkOrderingOfTree(current.right);
        } else if (current.left == null && current.right == null)
            return true;

        return true;
    }

    public SearchResult getNodeByName(String name) {
        return findNode(root, name, 1);
    }

    private SearchResult findNode(AvlNode<T> parent, String name, int height) {
        if (parent == null) {
            return null;
        } else if (parent.element instanceof Image) {

            if (((Image) (parent.element)).name.compareTo(name) == 0) {
                return new SearchResult(parent, height);
            } else if (((Image) parent.element).name.compareTo(name) > 0) {
                return findNode(parent.left, name, ++height);
            } else {
                return findNode(parent.right, name, ++height);
            }
        } else {
            System.out.println("Not support operation");
            return null;
        }
    }

    public SearchResult getNodeByHash(String hash) {
        return findNodeByHash(root, hash, 1);
    }

    private SearchResult findNodeByHash(AvlNode<T> parent, String hash, int height) {
        if (parent == null) {
            return null;
        } else if (parent.element instanceof HashImage) {

            if (((HashImage) (parent.element)).name.compareTo(hash) == 0) {
                return new SearchResult(parent, height);
            } else if (((HashImage) parent.element).name.compareTo(hash) > 0) {
                return findNode(parent.left, hash, ++height);
            } else {
                return findNode(parent.right, hash, ++height);
            }
        } else {
            System.out.println("Not support operation");
            return null;
        }
    }
}
