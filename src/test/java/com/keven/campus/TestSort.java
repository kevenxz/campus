package com.keven.campus;

import com.keven.campus.common.utils.R;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Keven
 * @version 1.0
 */
public class TestSort {

    @Test
    void testR() {
        String str = "abbbbxc";
        String regex = "a[b]*c";
        boolean matches = str.matches(regex);
        System.out.println(matches);
        String importFileRole = "(import)\\s*[a-zA-Z0-9_<>.]+\\;";//正则表达式
        Pattern p = Pattern.compile(importFileRole);//获取正则表达式中的分组，每一组小括号为一组
    }

    @Test
    void test() {
        // 冒泡排序
        int[] nums = getNums(10);
        for (int i = 1; i < nums.length; i++) {
            boolean flag = false;
            for (int j = 0; j < nums.length - i; j++) {
                if (nums[j] < nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                    flag = true;
                }
            }
            System.out.println(Arrays.toString(nums));
            if (!flag) {
                break;
            }
        }
    }

    @Test
    void test22() {
        String x = "3.70\n" +
                "3.80\n" +
                "2.70\n" +
                "3.10\n" +
                "4.40\n" +
                "3.40\n" +
                "4.30\n" +
                "4.00\n" +
                "4.00\n" +
                "4.40\n" +
                "3.70\n" +
                "2.80\n" +
                "3.80\n" +
                "3.30\n" +
                "3.80\n" +
                "3.80\n" +
                "4.90\n" +
                "3.10\n" +
                "2.60\n" +
                "3.10\n" +
                "3.70\n" +
                "2.40\n" +
                "3.70\n" +
                "4.80\n" +
                "3.80\n" +
                "2.30\n" +
                "2.60\n" +
                "3.90\n" +
                "3.70\n" +
                "3.00\n" +
                "2.40\n" +
                "3.40\n" +
                "3.00\n" +
                "3.70\n" +
                "4.00\n" +
                "3.80\n" +
                "3.30\n" +
                "3.80\n" +
                "3.50\n" +
                "3.10\n" +
                "2.40\n" +
                "2.90\n" +
                "3.70\n" +
                "3.70\n" +
                "3.20\n" +
                "3.80\n" +
                "4.30\n" +
                "4.40\n" +
                "4.20\n" +
                "3.80\n" +
                "2.80\n" +
                "3.50\n" +
                "4.60\n" +
                "4.30\n" +
                "4.00\n";
        String[] split = x.split("\n");
        double sum = 0;
        for (String s : split) {
            sum += Double.valueOf(s);
        }
        System.out.println(split.length);
        System.out.println(sum);
        System.out.println(sum / split.length);
    }

    @Test
    void testSelectSort() {
        Stack<Integer> stack = new Stack<>();
        // 选择排序
        int[] nums = getNums(10);
        for (int i = 0; i < nums.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < nums.length; j++) {
                // 每次确定第一个位置
                if (nums[j] < nums[min]) {
                    min = j;
                }
            }
            // 将找到的最小值和i位置所在的值进行交换
            if (min != i) {
                int temp = nums[i];
                nums[i] = nums[min];
                nums[min] = temp;
            }
            System.out.println(Arrays.toString(nums));
        }
    }

    @Test
    void testInsertSort() {
        // 插入排序 (从后面选一个数出来，插入到前面)
        // 默认第二个数开始，遍历前面，比前面小的就插入（前面位置的数字都是有序的）
        int[] nums = getNums(10);
//        for (int i = 1; i < nums.length; i++) {
//            // 记录要插入的值
//            int temp = nums[i];
//            int j = i;
//            // 从前面已经有序的数组最右边开始比较找插入的位置
//            while (j > 0 && temp < nums[j - 1]) {
//                nums[j] = nums[j - 1];
//                j--;
//            }
//            //存在比其小的数 ，插入
//            if (j != i) {
//                nums[j] = temp;
//            }
//            System.out.println(Arrays.toString(nums));
//        }
        for (int i = 1; i < nums.length; i++) {
            int temp = nums[i];
            int j = i; // 记录有序的最右位置
            while (j > 0 && temp < nums[j - 1]) {
                nums[j] = nums[j - 1];
                j--;
            }
            if (j != i) {
                nums[j] = temp;
            }
        }
    }

    @Test
    void testMerge() {
        Merge merge = new Merge();
        int[] nums = getNums(8);
        System.out.println(Arrays.toString(nums));
        merge.sort(nums);
    }

    @Test
    void testQuick() {
        Quick quick = new Quick();
        int[] nums = getNums(10);
        quick.sort(nums);
    }

    private int[] getNums(int n) {
        int nums[] = new int[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            nums[i] = random.nextInt(20);
        }
        return nums;
    }

    class Merge {
        // 用于辅助有序数组
        public int[] temp;

        public void sort(int[] nums) {
            // 先给辅助数组开辟空间
            temp = new int[nums.length];
            // 排序
            mergeSort(nums, 0, nums.length - 1);
        }


        // 将字数组 nums[l... r]进行排序
        private void mergeSort(int[] nums, int l, int r) {
            if (l == r) return;
            int mid = l + (r - l) / 2;
            mergeSort(nums, l, mid);
            mergeSort(nums, mid + 1, r);
            // 后序位置
            // 将两部分有序数组合并成一个有序数组
            merge(nums, l, mid, r);
            System.out.println(Arrays.toString(nums));
        }


        //将 nums[l.mid] 和 nums[mid+1..r] 这两个有序数组合并成一个有序数组
        private void merge(int[] nums, int l, int mid, int r) {
            // 先把 nums[l..r] 复制到辅助数组中
            // 以便合并后的结果能够直接存入 nums
            for (int i = l; i <= r; i++) {
                temp[i] = nums[i];
            }
            // 数组双指针技巧，合并两个有序数组
            int i = l, j = mid + 1;
            for (int k = l; k <= r; k++) {
                if (i == mid + 1) {
                    // 左半边数组已经完全合并了
                    nums[k] = temp[j++];
                } else if (j == r + 1) {
                    // 右半边的数组已经全部合并
                    nums[k] = temp[i++];
                } else if (temp[i] > temp[j]) {
                    nums[k] = temp[j++];
                } else {
                    nums[k] = temp[i++];
                }
            }
        }
    }

    class Quick {
        // 前序遍历 (快排相当于二叉搜索树-- > 会导致一边倒，变成链表(内存占用多))
        // 加入随机性
        // 思路: 先对一个数确定位置，然后再对左右的子数组的数进行确定
        void sort(int[] nums) {
            // 洗牌
            shuffle(nums);
            System.out.println(Arrays.toString(nums));
            sort(nums, 0, nums.length - 1);
        }

        private void shuffle(int[] nums) {
            Random random = new Random();
            int n = nums.length;
            for (int i = 0; i < n; i++) {
                // 生成 i .. n-1 的随机数
                int r = i + random.nextInt(n - i);
                swap(nums, i, r);
            }
        }

        private void swap(int[] nums, int i, int r) {
            int temp = nums[i];
            nums[i] = nums[r];
            nums[r] = temp;
        }

        void sort(int[] nums, int l, int r) {
            if (l >= r) return;
            // 对 nums[l..r] 进行切分
            // 使得 nums[l..p-1] <= nums[p] < nums[p+1..r]
            int p = partition(nums, l, r); // 已经确定元素位置的索引
            System.out.println(Arrays.toString(nums));
            sort(nums, l, p - 1);
            sort(nums, p + 1, r);

        }

        // 返回一个已经排序好的索引位置
        private int partition(int[] nums, int l, int r) {
            int num = nums[l];
            int i = l + 1, j = r;
            while (i <= j) {
                while (i < r && nums[i] <= num) {
                    i++;
                }
                while (j > l && nums[j] > num) {
                    j--;
                }
                if (i >= j) { // 外层只判断i<=j  内层nums[i]<= num (i会多加)
                    break;
                }
                swap(nums, i, j);
            }
            swap(nums, l, j);
            return j;
        }
    }
}
