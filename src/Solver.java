import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private SearchNode minNode;
    private boolean solvable = true;

    private static class SearchNode implements Comparable<SearchNode> {

        private final Board board;
        private final int moves;
        private final int manhattan;
        private final SearchNode  prevSearchNode;

        public SearchNode(Board board, int moves, SearchNode prevSearchNode) {
            this.board = board;
            this.moves = moves;
            this.prevSearchNode = prevSearchNode;
            this.manhattan = board.manhattan();
        }

        @Override
        public int compareTo(SearchNode o) {
            return Integer.compare(manhattan + moves , o.manhattan + o.moves);
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if(initial == null) {
            throw new IllegalArgumentException();
        }

        Board initialTwin = initial.twin();

        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();

        minNode = new SearchNode(initial , 0 , null);
        SearchNode minNodeTwin = new SearchNode(initialTwin, 0, null);

        pq.insert(minNode);
        pqTwin.insert(minNodeTwin);

        while (!minNode.board.isGoal()) {

            SearchNode min = pq.delMin();
            SearchNode minTwin = pqTwin.delMin();

            for (Board board : min.board.neighbors()) {
                if(min.prevSearchNode != null ) {
                    if(!board.equals(min.prevSearchNode.board)) {
                        pq.insert(new SearchNode(board, moves() + 1, min));
                    }
                } else {
                    pq.insert(new SearchNode(board, moves() + 1, min));
                }
            }
            for (Board board : minTwin.board.neighbors()) {
                if(minTwin.prevSearchNode != null ) {
                    if(!board.equals(minTwin.prevSearchNode.board)) {
                        pqTwin.insert(new SearchNode(board, moves() + 1, minTwin));
                    }
                } else {
                    pqTwin.insert(new SearchNode(board, moves() + 1, minTwin));
                }
            }


            minNode = pq.min();
            minNodeTwin = pqTwin.min();

            if(minNodeTwin.board.isGoal()) {
                solvable = false;
                break;
            }
        }



    }

    public boolean isUnsolvable() {
        return !solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {

        if(isUnsolvable()) {
            return -1;
        } else {
            return minNode.moves;
        }
    }

    // sequence of boards in the shortest solution; null if unsolvable
    public Iterable<Board> solution() {

        if(isUnsolvable()) {
            return null;
        }

        Stack<Board> bs = new Stack<>();

        SearchNode temp = minNode;
        while(temp != null) {
            bs.push(temp.board);
            temp = temp.prevSearchNode;
        }
        return bs;

    }

    // test client
    public static void main(String[] args) {

        int[][] tiles = {
                {6,3,7,4},
                {2,9,10,8},
                {1,5,12,15},
                {13,0,14,11}};

        Board board = new Board(tiles);

        Solver solver = new Solver(board);

        if(solver.isUnsolvable()) {
            System.out.println("Cannot solve");
        } else {
            System.out.println("min moves to solve : " + solver.moves());
            for(Board b : solver.solution()) {
                System.out.println(b);
            }
        }

    }

}