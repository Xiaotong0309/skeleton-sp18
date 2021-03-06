package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int size;
    private boolean[][] sites;
    private WeightedQuickUnionUF percSets;
    private WeightedQuickUnionUF fullSets;
    private int openNum;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        this.size = N;

        this.sites = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.sites[i][j] = false;
            }
        }
        this.percSets = new WeightedQuickUnionUF(N * N + 2);
        this.fullSets = new WeightedQuickUnionUF(N * N + 1);
        this.openNum = 0;
    }

    public void open(int row, int col) {
        if (row < 0 || row >= this.size || col < 0 || col >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        if (this.sites[row][col]) {
            return;
        }
        this.openNum++;
        this.sites[row][col] = true;

        if (row > 0 && this.sites[row - 1][col]) {
            this.percSets.union(row * this.size + col, (row - 1) * this.size + col);
            this.fullSets.union(row * this.size + col, (row - 1) * this.size + col);
        }
        if (row < this.size - 1 && this.sites[row + 1][col]) {
            this.percSets.union(row * this.size + col, (row + 1) * this.size + col);
            this.fullSets.union(row * this.size + col, (row + 1) * this.size + col);
        }
        if (col > 0 && this.sites[row][col - 1]) {
            this.percSets.union(row * this.size + col, row * this.size + col - 1);
            this.fullSets.union(row * this.size + col, row * this.size + col - 1);
        }
        if (col < this.size - 1 && this.sites[row][col + 1]) {
            this.percSets.union(row * this.size + col, row * this.size + col + 1);
            this.fullSets.union(row * this.size + col, row * this.size + col + 1);
        }

        if (row == 0) {
            this.percSets.union(this.size * this.size, row * this.size + col);
            this.fullSets.union(this.size * this.size, row * this.size + col);
        }

        if (row == this.size - 1) {
            this.percSets.union(this.size * this.size + 1, row * this.size + col);
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 0 || row >= this.size || col < 0 || col >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        return this.sites[row][col];
    }

    public boolean isFull(int row, int col) {
        if (row < 0 || row >= this.size || col < 0 || col >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        return this.fullSets.connected(row * this.size + col, this.size * this.size);
    }

    public int numberOfOpenSites() {
        return this.openNum;
    }

    public boolean percolates() {
        return this.percSets.connected(this.size * this.size, this.size * this.size + 1);
    }

    public static void main(String[] args) {
        int test1 = 10;
        Percolation p = new Percolation(test1);
        for (int i = 0; i < test1; i++) {
            p.open(i, i);
        }
        for (int i = 0; i < test1; i++) {
            p.open(i, test1 - i - 1);
        }
        for (int i = 0; i < test1; i++) {
            p.open(i, 4);
        }
        PercolationVisualizer.draw(p, test1);
    }
}
