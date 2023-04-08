package com.nikita23830.ewitchery.common.recipes;

public enum RecipeSize {
    MINI(12, new int[][]{{0,2},{1,2},{2,1},{2,0},{2,-1},{1,-2},{0,-2},{-1,-2},{-2,-1},{-2,0},{-2,1},{-1,2}}, new int[]{-2,-2,2,2}),
    NORMAL(24, new int[][]{{0,4},{1,4},{2,4},{3,3},{4,2},{4,1},{4,0},{4,-1},{4,-2},{3,-3},{2,-4},{1,-4},{0,-4},{-1,-4},{-2,-4},{-3,-3},{-4,-2},{-4,-1},{-4,0},{-4,1},{-4,2},{-3,3},{-2,4},{-1,4}}, new int[]{-4,-4,4,4});

    public final int size;
    public final int[][] offset;
    public final int[] minMaxAxis;
    private RecipeSize(int size, int[][] offset, int[] axis) {
        this.size = size;
        this.offset = offset;
        this.minMaxAxis = axis;
    }
}
