package com.nowcoder.community;


import org.junit.Test;

public class leetcodeTests {

        @Test
        public void constructMaximumBinaryTree() {
            int[] nums = {3,2,1,6,0,5};
            System.out.println(constructMaximumBinaryTre1(nums,0,nums.length));
        }

        TreeNode constructMaximumBinaryTre1(int[] nums,int left,int right){
            if(right - left < 1)
                return null;
            if(right - left == 1)
                return new TreeNode(nums[left]);
            int maxIndex = left;
            int maxValue = nums[left];
            for(int i = left + 1; i < right;i++){
                if(nums[i] > maxValue){
                    maxIndex = i;
                    maxValue = nums[maxValue];
                }
            }
            TreeNode node =  new TreeNode(maxValue);
            node.left = constructMaximumBinaryTre1(nums,left,maxIndex);
            node.right = constructMaximumBinaryTre1(nums,maxIndex + 1, right);

            return node;
        }

}
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
      TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
