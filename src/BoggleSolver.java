// import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoggleSolver {

    private TST<Integer> searchTries;
    private List<String> foundWords;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        int n = dictionary.length;

        searchTries = new TST<>();
        // laad alle woorden uit dictionary in de searchTries
        for (int i = 0; i < n; i++) {
            String word = dictionary[i];
            searchTries.put(word, i);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    // keysWithPrefix return Iterable or IllegalArgumentException if null
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        BoggleBoard b = board;
        foundWords = new ArrayList<>();
        int rows = b.rows();
        int cols = b.cols();
        // start at every posible position
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                boolean[][] visited = new boolean[rows][cols];
                for (int i = 0; i < rows; i++) {
                    Arrays.fill(visited[i], Boolean.FALSE); // initialize all to false
                }
                checkNextField(b, visited, "", x, y);
            }
        }
        return foundWords;
    }

    private void checkNextField(BoggleBoard board, boolean[][] visited, String prefix, int x, int y) {
        // if already visited, return
        if (visited[x][y]) return;

        char letter = board.getLetter(x, y);
        String word = prefix;

        if (letter == 'Q') {
            word += "QU";
        } else {
            word += letter;
        }
        // if not a prefix is not in the Trie, return
        if (!searchTries.keysWithPrefix(word).iterator().hasNext()) {
            return;
        }
        // is word and scoring points?
        if (word.length() > 2 && searchTries.contains(word) && !foundWords.contains(word)) {
            foundWords.add(word);
        }
        visited[x][y] = true;

        // move to all adjecent squares
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if ((x + i >= 0) && (x + i < board.rows()) && (y + j >= 0) && (y + j < board.cols())) {
                    checkNextField(board, visited, word, x + i, y+ j);
                }
            }
        }
        visited[x][y] = false;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (searchTries.contains(word)) {
            switch (word.length()) {
                case 0:
                case 1:
                case 2: return 0;
                case 3:
                case 4: return 1;
                case 5: return 2;
                case 6: return 3;
                case 7: return 5;
                default: return 11;
            }
        } else {
            return 0;
        }
    }

//    public static void main(String[] args) {
//        In in = new In("dictionary-yawl.txt");
//        String[] dictionary = in.readAllStrings();
//        BoggleSolver solver = new BoggleSolver(dictionary);
//        BoggleBoard board = new BoggleBoard("board-points100.txt");
//        int score = 0;
//        for (String word : solver.getAllValidWords(board)) {
//            StdOut.println(word);
//            score += solver.scoreOf(word);
//        }
//        StdOut.println("Score = " + score);
//    }
}