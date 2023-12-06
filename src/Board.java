import java.util.LinkedList;

public class Board {

    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = tiles;
    }

    // string representation of this board
    public String toString() {

        StringBuilder s = new StringBuilder(dimension() + "\n ");

        for (int[] tile : tiles) {
            for (int i : tile) {
                s.append(i);
                s.append((i / 10 > 0) ? " " : "  ");
            }
            s.append("\n ");
        }

        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }
    // number of tiles out of place
    public int hamming() {
        int count = 1;
        int correctTiles = 0;
        for(int i = 0; i < dimension(); i++) {
            for(int j = 0; j < dimension(); j++) {
                if(tiles[i][j] == count) {
                    correctTiles++;
                }
                count++;
                if(count == dimension()*dimension()) break;
            }
        }
        return (dimension()*dimension()) - (correctTiles + 1);
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        for(int i = 0; i < dimension() ; i++) {
            for(int j = 0; j < dimension() ; j++) {
                int num = tiles[i][j];
                if(num != 0) {
                    int numRow = (num - 1) / dimension();
                    int numColumn = (num - 1) % dimension();
                    manhattan += Math.abs(numRow - i);
                    manhattan += Math.abs(numColumn - j);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        int count = 1;
        for(int i = 0; i < dimension(); i++) {
            for(int j = 0; j < dimension(); j++) {

                if(tiles[i][j] != count) {
                    return false;
                }
                count++;
                if(count == dimension() * dimension()) break;
            }
        }
        return true;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {

        if(y == null) return false;

        Board thatBoard = (Board) y;

        if(dimension() != thatBoard.dimension()) return false;

        for(int i = 0; i < dimension(); i++) {
            for(int j = 0; j < dimension(); j++) {
                if(tiles[i][j] != thatBoard.tiles[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        int i0 = 0,j0 = 0;

        for(int i = 0; i < dimension(); i++) {
            for(int j = 0; j < dimension(); j++) {
                if(tiles[i][j] == 0) {
                    i0 = i;
                    j0 = j;
                    break;
                }
            }
        }

        LinkedList<Board> bl = new LinkedList<>();

        if (j0 > 0) {
            newNeighbor(i0, j0, i0, j0 - 1, bl);
        }
        if (j0 < dimension() - 1) {
            newNeighbor(i0, j0, i0, j0 + 1, bl);
        }
        if (i0 > 0) {
            newNeighbor(i0, j0, i0 - 1, j0, bl);
        }
        if (i0 < dimension() - 1) {
            newNeighbor(i0, j0, i0 + 1, j0, bl);
        }

        return bl;
    }

    private void newNeighbor(int i, int j, int x, int y, LinkedList<Board> neighborsList) {
        int[][] newTiles = copyTiles();
        swapTiles(newTiles, i, j, x, y);
        neighborsList.add(new Board(newTiles));
    }

    private int[][] copyTiles() {
        int n = tiles.length;
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(tiles[i], 0, copy[i], 0, n);
        }
        return copy;
    }

    private void swapTiles(int[][] arr , int i , int j , int x , int y) {
        int temp = arr[i][j];
        arr[i][j] = arr[x][y];
        arr[x][y] = temp;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] copy = copyTiles();

        int row1 = 0, row2 = 1;

        if (tiles[0][0] == 0) {
            row1 = 1;
            row2 = 2;
        }

        // Exchange the first two elements of the selected rows
        swapTiles(copy , row1 , 0, row2 , 0);

        return new Board(copy);
    }

    // testing
    public static void main(String[] args) {

        int[][] initialTiles = {
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12},
                {13,14,15,16}
        };

        Board initialBoard = new Board(initialTiles);

        // Display the initial board
        System.out.println("Initial Board:");
        System.out.println(initialBoard);

        // Test the twin() method
        Board twinBoard = initialBoard.twin();
        System.out.println("Twin Board:");
        System.out.println(twinBoard);

        // Test other methods
        System.out.println("Is the board goal? " + initialBoard.isGoal());
        System.out.println("Hamming priority: " + initialBoard.hamming());
        System.out.println("Manhattan priority: " + initialBoard.manhattan());

        // Test neighbors
        System.out.println("Neighbors:");
        for (Board neighbor : initialBoard.neighbors()) {
            System.out.println(neighbor);
        }
    }

}