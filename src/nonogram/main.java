package nonogram;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class main {

    static int mistakes = 0;
    static int points = 0;
    static int marked = 0;
    static ArrayList<ArrayList<Integer>> game;
    static ArrayList<ArrayList<Integer>> verticalHints;
    static ArrayList<ArrayList<Integer>> horizontalHints;

    /*
        Main Function
        A nonogram must be provided. If a file is provided
        and it can be read, it will be be sent to
        boardRead(file). If not, it will print "No file 
        found."
     */
    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            System.out.println("No nonogram provided.");
            System.out.println("Goodbye");
        } else {
            try {
                File file = new File(args[0]);
                boardRead(file);
            } catch (IOException e) {
                System.out.println("No file found");
            }
        }
    }

    /*
        boardRead() reads in a file and puts the values into
        an ArrayList of Integer ArrayLists. It's like a 2d
        array. It then executes verifyBoard() to see if the
        given file works as a nonogram. If it works, it
        counts the number of colored-in boxes and gets
        hints for the rows and columns. It then builds the
        GUI.
     */
    public static void boardRead(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp;
        game = new ArrayList<>();
        while ((temp = br.readLine()) != null) {
            ArrayList<Integer> line = new ArrayList<>();
            String[] currentLine = temp.split(" ");
            for (int i = 0; i < currentLine.length; i++) {
                line.add(Integer.parseInt(currentLine[i]));
            }
            game.add(line);
        }

        if (verifyBoard()) {
            System.out.println("It is verified.");
            count1s();
            getHorizontalHints();
            getVerticalHints();
            GUI();

        } else {
            System.out.println("It is not verified.");
        }
    }

    /*
        verifyBoard() checks to see if the nonogram is
        uniform. It must have a consistent width and
        height throughout.
     */
    public static boolean verifyBoard() {
        int size = game.get(0).size();
        for (int i = 1; i < game.size(); i++) {
            if (game.get(i).size() != size) {
                System.out.println("Size of nonogram is not uniform.");
                return false;
            }
        }
        return true;
    }

    /*
        count1s() counts all the marked boxes in the
        nonagram. This is used to determine if you
        have won.
     */
    public static void count1s() {
        for (int i = 0; i < game.size(); i++) {
            for (int j = 0; j < game.get(0).size(); j++) {
                if (game.get(i).get(j) == 1) {
                    points++;
                }
            }
        }
    }
    
    /*
        getVerticalHints() gets all the consecutive
        boxes in a column and stores them in an
        ArrayList of Integer ArrayLists.
    */
    public static void getVerticalHints() {
        int count = 0;
        boolean zero = false;
        ArrayList<Integer> column;
        verticalHints = new ArrayList<>();
        for (int i = 0; i < game.get(0).size(); i++) {
            column = new ArrayList<>();
            for (int j = 0; j < game.size(); j++) {
                if (game.get(j).get(i) == 0) {
                    if (count != 0) {
                        column.add(count);
                    }
                    count = 0;
                } else {
                    count++;
                    zero = false;
                }
            }
            if (count != 0) {
                column.add(count);
            }
            if (zero == true) {
                column.add(count);
            }
            count = 0;
            zero = false;
            verticalHints.add(column);
        }
    }
    
    /*
        getHorizontalHints() gets all the consecutive
        boxes in a row and stores them in an 
        ArrayList of Integer ArrayLists.
    */
    public static void getHorizontalHints() {
        int count = 0;
        boolean zero = false;
        horizontalHints = new ArrayList<>();
        ArrayList<Integer> row;
        for (int i = 0; i < game.size(); i++) {
            row = new ArrayList<>();
            for (int j = 0; j < game.get(0).size(); j++) {
                if (game.get(i).get(j) == 0) {
                    if (count != 0) {
                        row.add(count);
                    }
                    count = 0;
                } else {
                    count++;
                    zero = false;
                }
            }
            if (count != 0) {
                row.add(count);
            }
            if (zero == true) {
                row.add(count);
            }
            count = 0;
            zero = false;
            horizontalHints.add(row);
        }
    }

    /*
        GUI() displays all the information from the input
        file, and has buttons to represent the rows and 
        columns of the nonogram. If you rightclick, you
        make an 'X' display on the Button, and if you
        left click, you fill in the box like you would
        with a pencil and paper nonogram.
     */
    public static void GUI() {
        JFrame frame = new JFrame();

        JPanel panel = new JPanel();
        JPanel vertical = new JPanel();
        JPanel padding = new JPanel();
        vertical.add(padding, BorderLayout.WEST);
        JLabel verticalLabels[] = new JLabel[verticalHints.size()];
        GridLayout vertGrid = new GridLayout(1, verticalHints.size() + 1);
        vertical.setLayout(vertGrid);

        JPanel horizontal = new JPanel();
        JLabel horizontalLabels[] = new JLabel[horizontalHints.size()];
        GridLayout horiGrid = new GridLayout(horizontalHints.size(), 1);
        horizontal.setLayout(horiGrid);

        for (int i = 0; i < horizontalHints.size(); i++) {
            String hintRow = "";
            for (int j = 0; j < horizontalHints.get(i).size(); j++) {
                hintRow += horizontalHints.get(i).get(j) + " ";
            }
            horizontalLabels[i] = new JLabel(hintRow + "");
            horizontal.add(horizontalLabels[i]);
        }

        for (int i = 0; i < verticalHints.size(); i++) {
            String hintColumn = "";
            for (int j = 0; j < verticalHints.get(i).size(); j++) {
                hintColumn += verticalHints.get(i).get(j) + " \n";
            }
            verticalLabels[i] = new JLabel(hintColumn);
            vertical.add(verticalLabels[i], BorderLayout.CENTER);
        }

        GridLayout grid = new GridLayout(game.size(), game.get(0).size());
        JButton[][] buttons = new JButton[game.size()][game.get(0).size()];
        panel.setLayout(grid);
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[0].length; j++) {
                buttons[i][j] = new JButton("-");
                buttons[i][j].setPreferredSize(new Dimension(60, 60));
                buttons[i][j].addMouseListener(new rightClick());
                buttons[i][j].addActionListener(new leftClick(game.get(i).get(j)));
                panel.add(buttons[i][j]);
            }
        }
        frame.add(horizontal, BorderLayout.WEST);
        frame.add(vertical, BorderLayout.NORTH);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    
    /*
        mistakePopUp() is a small pop up that appears when
        you make a mistake. It displays the number of
        mistakes you have made so far.
    */
    public static void mistakePopUp() {
        JOptionPane.showMessageDialog(null, "Wrong Number of mistakes: " + mistakes, "Oops!", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /*
        win() displays a picture of what the nonagram
        was, congratulates you on winning, and shows
        the number of mistakes you have made.
    */
    public static void win() {
        drawPicture pic = new drawPicture();
        JFrame box = new JFrame("Congrats!");
        JLabel mistakeCount = new JLabel("Number of mistakes: " + mistakes);
        GridLayout grid = new GridLayout(2, 1);
        mistakeCount.setHorizontalAlignment(JLabel.CENTER);
        box.setLayout(grid);
        box.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        box.setSize(game.size() * 10, game.get(0).size() * 14);
        box.setPreferredSize(new Dimension(game.size() * 20, game.get(0).size() * 20));
        box.add(mistakeCount);
        box.add(pic);
        box.pack();
        box.setVisible(true);
    }
    
    /*
        drawPicture draws what the end result of the
        nonogram is supposed to be.
    */
    private static class drawPicture extends JPanel {

        Color color = Color.BLACK;

        @Override
        protected void paintComponent(Graphics g) {
            for (int i = 0; i < game.size(); i++) {
                for (int j = 0; j < game.size(); j++) {
                    if (game.get(i).get(j) == 1) {
                        g.setColor(color);
                    }
                    if (game.get(i).get(j) == 0) {
                        g.setColor(Color.WHITE);
                    }
                    g.fillRect(j * 10, i * 10, 10, 10);
                }
            }
        }

        public void animate() throws InterruptedException {
            repaint();
        }

    }
    
    /*
        rightClick is used when you right click a button
        on rhe GUI. It is used as a way of marking the 
        nonogram without making a mistake. It is a tool
        to help the player.
    */
    private static class rightClick extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
                Object source = e.getSource();
                JButton button = (JButton) source;
                if (button.getText().equals("-")) {
                    button.setText("X");
                } else if (button.getText().equals("X")) {
                    button.setText("-");
                }
            }
        }
    }
    
    /*
        leftClick is used to fill in the button with a "1".
        A "1" is marking the grid as a space to fill in.
        If you marked every mark in the game, you win. If
        you incorrectly marked the grid, a mistake count
        increases and a popup shows you what happened.
    */
    public static class leftClick implements ActionListener {

        int val;

        public leftClick(int val) {
            this.val = val;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            JButton button = (JButton) source;
            if (val == 0) {
                mistakes++;
                mistakePopUp();
            } else {
                if (!button.getText().equals("1")) {
                    marked++;
                }
                button.setText(val + "");
                if (marked == points) {
                    win();
                }
            }
        }
    }
}
