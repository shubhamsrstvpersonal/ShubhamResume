package com.js.sudoko;

import java.util.*;

public class SudukoMain {

    Random random = new Random();

    int userGrid[][] = new int[][]{
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0}
    };

    int grid[][] = new int[9][9];
    int finalGrid[][] = new int[9][9];

    public void main(){

        Set<Integer> randList = new LinkedHashSet<>();
        Integer[] array = new Integer[9];
        int index = 0;

        while (randList.size() < 9){
            int x = random.nextInt(10);
            if (x == 0){
                x = x+1;
            }
            randList.add(x);
        }
        randList.toArray(array);

        for (int j=0; j<9;){
            userGrid[0][j] = array[index];
            j=j+2;
            index++;
        }
        for (int i=2; i<9;){
            userGrid[i][0] = array[index];
            i=i+2;
            index++;
        }

        for (int i=0; i<9; i++){
            for (int j=0; j<9; j++){
                grid[i][j]=userGrid[i][j];
            }
        }
        finalGrid = loop(0, 0, grid);

//        for (int i=0; i<9; i++){
//            for (int j=0; j<9; j++){
//                System.out.print(finalGrid[i][j]);
//                if ((j-2)%3 == 0){
//                    System.out.print(" ");
//                }
//            }
//            System.out.println();
//            if ((i-2)%3 == 0){
//                System.out.println();
//            }
//        }
    }

    private int[][] loop(int y, int x, int[][] grid) {
        while (!validity(8, 8, grid) || grid[8][8] == 0){
            if (userGrid[y][x]!=0){
                int yy, xx;
                if (x == 8) {
                    yy = y + 1;
                    xx = 0;
                } else {
                    yy = y;
                    xx = x + 1;
                }
                loop(yy, xx, grid);
            }
            else {
                if (grid[y][x] < 9) {
                    grid[y][x]++;
                    if (validity(y, x, grid)) {
                        int yy, xx;
                        if (x == 8) {
                            yy = y + 1;
                            xx = 0;
                        } else {
                            yy = y;
                            xx = x + 1;
                        }
                        loop(yy, xx, grid);
                    }
                } else {
                    grid[y][x] = 0;
                    break;
                }
            }
        }
        return grid;
    }

    public boolean validity(int x, int y, int[][] grid){
        String temp="";
        for (int i=0; i<9; i++){
            temp+=Integer.toString(grid[i][y]);
            temp+=Integer.toString(grid[x][i]);
            temp+=Integer.toString(grid[(x/3)*3+i/3][(y/3)*3+i%3]);
        }
        int count=0, idx=0;
        while ((idx=temp.indexOf(Integer.toString(grid[x][y]), idx))!=-1){
            idx++;
            count++;
        }
        return count==3;
    }
}
