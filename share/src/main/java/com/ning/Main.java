package com.ning;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {


    }




    public static TreeNode createTree() {
        /**
         *     12
         *    3   9
         *  1 2  5   8
         *         22
         *        33
         */
        TreeNode one = new TreeNode(1);
        TreeNode two = new TreeNode(2);
        TreeNode three = new TreeNode(3, one, two);

        TreeNode one2 = new TreeNode(5);

        TreeNode one4 = new TreeNode(33);
        TreeNode one3 = new TreeNode(22, one4, null);

        TreeNode two2 = new TreeNode(8, one3, null);
        TreeNode three2 = new TreeNode(9, one2, two2);


        TreeNode root = new TreeNode(12, three, three2);
        return root;
    }

    public static void printLink(ListNode param) {
        while (param != null) {
            System.out.print(param.val + "|");
            param = param.next;
        }
        System.out.println();
    }

    public static ListNode createLinkBackInterpolation(int[] params, boolean flag) {
        ListNode result = new ListNode();
        if (params.length == 0) {
            return result;
        }
        result.val = params[0];

        ListNode loop = result;
        for (int i = 1; i < params.length; i++) {
            ListNode temp = new ListNode();
            temp.val = params[i];
            loop.next = temp;// 后插法
            loop = temp;
        }
        // 是否循环
        if (flag) {
            loop.next = result;
        }

        // for (int i = 1; i < params.length; i++) {
        // ListNode temp = new ListNode();
        // temp.val = params[i];
        // temp.next = loop;// 前插
        // loop = temp;
        // }

        return result;
    }

    public static void printTree(TreeNode root, int direction) {
        if (root == null) {
            return;
        }
        switch (direction) {
            case 1:
                System.out.println(root.val);
                printTree(root.left, 1);
                printTree(root.right, 1);
                break;
            case 2:
                printTree(root.left, 2);
                System.out.println(root.val);
                printTree(root.right, 2);
                break;
            case 3:
                printTree(root.left, 3);
                printTree(root.right, 3);
                System.out.println(root.val);
                break;
        }

    }

}

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode() {
    }

    TreeNode(int val) {
        this.val = val;
    }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}

class ListNode {
    int val;
    ListNode next;
    ListNode before;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}