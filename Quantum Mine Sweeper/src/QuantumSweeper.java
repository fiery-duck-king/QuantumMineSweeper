import javax.swing.*;
import javax.swing.text.Position;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class QuantumSweeper extends JFrame{

    private static int HP = 3;
    private static int Tile = 0;
    private static int SelectedSpace = 0;

    QuantumSweeper(int dif) {
        //Randomizer random = new Randomizer();
        JButton[][] button = new JButton[dif][dif];
        int[][] bomb = new int[dif][dif];

        JButton flag = new JButton();
        int[][] flagged = new int[dif][dif];
        AtomicBoolean flagger = new AtomicBoolean(false);
        String[][] num = new String[dif][dif];

        for (int x = 0; x < dif; x++) {
            for (int y = 0; y < dif; y++) {
                button[x][y] = new JButton();
            }
        }

        flag.setBounds(dif * 45 + 20, 20, 45, 45);
        flag.setBackground(Color.black);

        for (int x = 0; x < dif; x++) {
            for (int y = 0; y < dif; y++) {
                button[y][x].setBounds(y * 45 + 20, x * 45 + 20, 45, 45);
            }
        }

        int bombCount = 0;
        for (int x = 0; x < dif; x++) {
            for (int y = 0; y < dif; y++) {
                int random = (int) (Math.random() * (dif / 3 + 20 / 3));
                /* 10 : 20%
                   15 : 17%
                   20 : 15%
                   30 : 12% */
                if (random < 2) { // 20% chance of Bomb
                    bomb[x][y] = 1;
                    button[x][y].setText(" "); // Bomb
                    bombCount ++;
                } else {
                    button[x][y].setText(" ");
                    bomb[x][y] = 0;
                }
            }
        }
        SelectedSpace = (dif * dif) - bombCount;
        System.out.println("There are " + bombCount + " Bombs");
        //button[x location] [y location].bla bla bla;

        int SafeCount = 1;
        for (int x = 0; x < dif; x++) {
            for (int y = 0; y < dif; y++) {
                int count = 0;
                if (bomb[x][y] == 0) {
                    for (int a = Math.max(x - 1, 0); a <= Math.min(x + 1, dif - 1); a++) {
                        for (int b = Math.max(y - 1, 0); b <= Math.min(y + 1, dif - 1); b++) {
                            if (bomb[a][b] == 1) {
                                count ++;
                            }
                        }
                    }
                    num[x][y] = String.valueOf(count);
                    if (count == 0 && SafeCount > 0) {
                        button[x][y].setText("0");
                        SafeCount--;
                    }
                    //button[x][y].setText(String.valueOf(count));
                    //button[x][y].setBackground(Color.GRAY);
                } else {
                    num[x][y] = "Bomb";
                    //button[x][y].setText("Bomb");
                    //button[x][y].setBackground(Color.RED);
                }
            }
        }

        int finalBombCount = bombCount;
        for (int x = 0; x < dif; x++) {
            for (int y = 0; y < dif; y++) {
                int finalX = x;
                int finalY = y;
                button[x][y].addActionListener(e -> QuantumSweeper.close(button, num, bomb, finalX, finalY, dif, flagger.get(), flagged));
            }
        }

        flag.addActionListener(e -> flagger.set(QuantumSweeper.switcher(flagger.get(), flag)));

        //Lives.setEnabled(false);

        //JOptionPane.showMessageDialog(null, "Their are " + bombCount + " Bombs");

        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(800, 800);
        this.setVisible(true);
        this.add(flag);
        for (int x = 0; x < dif; x++) {
            for (int y = 0; y < dif; y++) {
                //button[x][y].setFocusable(true);
                this.add(button[x][y]);
            }
        }
    }

    /*
    for (int x = 0; x < 9; x++) {
        for (int y = 0; y < 9; y++) {

        }
    }
    */

    public static boolean switcher(boolean flag, JButton eh) {
        if (!flag) {
            eh.setBackground(Color.red);
            return true;
        } else {
            eh.setBackground(Color.black);
            return false;
        }
    }

    public static void MoveBomb(int dif, int[][] bomb, JButton[][] button, String[][] num, int[][] flag) {
        for (int x = 0; x < dif; x++) {
            for (int y = 0; y < dif; y++) {
                if (bomb[x][y] == 1 && flag[x][y] == 0) {
                    ArrayList<Integer> MoveList = new ArrayList<>();
                    MoveList.add(0);
                    MoveList.add(1);
                    MoveList.add(2);
                    MoveList.add(3);
                    MoveList.add(4);
                    MoveList.add(5);
                    MoveList.add(6);
                    MoveList.add(7);
                    MoveList.add(8);
                    MoveList.add(9);

                    for (int z = 0; z < MoveList.size() / 2; z++) {
                        int RandomOrder = (int) (Math.random() * 9);
                        int RandomPlacement = (int) (Math.random() * 4);

                        int Hold = MoveList.get(RandomPlacement);
                        MoveList.set(RandomPlacement, MoveList.get(RandomOrder));
                        MoveList.set(RandomOrder, Hold);
                    }

                    while (MoveList.size() > 0) {
                        int Value = MoveList.remove(0);
                        switch (Value) {
                            case 0 -> {
                                try {
                                    if (bomb[x][y - 1] != 0) {
                                        throw new NegativeArraySizeException("Bomb cannot move to");
                                    }
                                    SwapBomb(bomb, new int[]{x, y}, new int[]{x, y - 1}, dif, button, num);
                                    MoveList = new ArrayList<>();
                                } catch (Exception ignore){}
                            }
                            case 1 -> {
                                try {
                                    if (bomb[x][y + 1] != 0) {
                                        throw new NegativeArraySizeException("Bomb cannot move to");
                                    }
                                    SwapBomb(bomb, new int[]{x, y}, new int[]{x, y + 1}, dif, button, num);
                                    MoveList = new ArrayList<>();
                                } catch (Exception ignore) {}
                            }
                            case 2 -> {
                                try {
                                    if (bomb[x - 1][y] != 0) {
                                        throw new NegativeArraySizeException("Bomb cannot move to");
                                    }
                                    SwapBomb(bomb, new int[]{x, y}, new int[]{x - 1, y}, dif, button, num);
                                    MoveList = new ArrayList<>();
                                } catch (Exception ignore){}
                            }
                            case 3 -> {
                                try {
                                    if (bomb[x + 1][y] != 0) {
                                        throw new NegativeArraySizeException("Bomb cannot move to");
                                    }
                                    SwapBomb(bomb, new int[]{x, y}, new int[]{x + 1, y}, dif, button, num);
                                    MoveList = new ArrayList<>();
                                } catch (Exception ignore){}
                            }
                            case 4 -> {
                                try {
                                    if (bomb[x + 1][y + 1] != 0) {
                                        throw new NegativeArraySizeException("Bomb cannot move to");
                                    }
                                    SwapBomb(bomb, new int[]{x, y}, new int[]{x + 1, y + 1}, dif, button, num);
                                    MoveList = new ArrayList<>();
                                } catch (Exception ignore){}
                            }
                            case 5 -> {
                                try {
                                    if (bomb[x + 1][y - 1] != 0) {
                                        throw new NegativeArraySizeException("Bomb cannot move to");
                                    }
                                    SwapBomb(bomb, new int[]{x, y}, new int[]{x + 1, y - 1}, dif, button, num);
                                    MoveList = new ArrayList<>();
                                } catch (Exception ignore){}
                            }
                            case 6 -> {
                                try {
                                    if (bomb[x - 1][y + 1] != 0) {
                                        throw new NegativeArraySizeException("Bomb cannot move to");
                                    }
                                    SwapBomb(bomb, new int[]{x, y}, new int[]{x - 1, y + 1}, dif, button, num);
                                    MoveList = new ArrayList<>();
                                } catch (Exception ignore){}
                            }
                            case 7 -> {
                                try {
                                    if (bomb[x - 1][y - 1] != 0) {
                                        throw new NegativeArraySizeException("Bomb cannot move to");
                                    }
                                    SwapBomb(bomb, new int[]{x, y}, new int[]{x - 1, y - 1}, dif, button, num);
                                    MoveList = new ArrayList<>();
                                } catch (Exception ignore){}
                            }
                            case 8 -> {
                                MoveList = new ArrayList<>();
                            }
                            case 9 -> {
                                // TP
                            }
                        }
                    }
                }
            }
        }
    }

    public static void SwapBomb(int[][] bomb, int[] OldPos, int[] NewPos, int dif, JButton[][] button, String[][] num) {
        int Hold = bomb[OldPos[0]][OldPos[1]];
        bomb[OldPos[0]][OldPos[1]] = bomb[NewPos[0]][NewPos[1]];
        bomb[NewPos[0]][NewPos[1]] = Hold;

        for (int x = 0; x < dif; x++) {
            for (int y = 0; y < dif; y++) {
                int count = 0;
                if (bomb[x][y] == 0 || bomb[x][y] == 2) {
                    for (int a = Math.max(x - 1, 0); a <= Math.min(x + 1, dif - 1); a++) {
                        for (int b = Math.max(y - 1, 0); b <= Math.min(y + 1, dif - 1); b++) {
                            if (bomb[a][b] == 1) {
                                count ++;
                            }
                        }
                    }

                    num[x][y] = String.valueOf(count);
                    if (bomb[x][y] == 2) {
                        button[x][y].setText(num[x][y]);
                    }
                    //button[x][y].setText(String.valueOf(count));
                    button[x][y].setBackground(Color.GRAY);
                } else {
                    num[x][y] = "Bomb";
                    //button[x][y].setText("Bomb");
                    button[x][y].setBackground(Color.PINK);
                }
            }
        }
    }

    public static void close(JButton[][] button, String[][] num, int[][] bomb, int a, int b, int dif, boolean flag, int[][] flagged) {
        if (!flag) {
            if (flagged[a][b] == 1) {
                return;
            }
            if (bomb[a][b] == 0) {
                button[a][b].setEnabled(false);
                button[a][b].setBackground(Color.white);
                button[a][b].setText(num[a][b]);
                bomb[a][b] = 2;
                Tile++;
            } else {
                HP--;
                JOptionPane.showMessageDialog(null, "You stuipid");
            }

            if (HP <= 0) {
                JOptionPane.showMessageDialog(null, "You Lost");
                for (int x = 0; x < dif; x++) {
                    for (int y = 0; y < dif; y++) {
                        button[x][y].setEnabled(false);
                        button[x][y].setText(num[x][y]);
                        button[x][y].setBackground(Color.lightGray);
                        if (bomb[x][y] == 1) {
                            button[x][y].setBackground(Color.RED);
                        }
                    }
                }
                return;
            }

            if (SelectedSpace - Tile == 0) {
                JOptionPane.showMessageDialog(null, "You found all the bombs");
                for (int x = 0; x < dif; x++) {
                    for (int y = 0; y < dif; y++) {
                        button[x][y].setBackground(Color.yellow);
                        if (bomb[x][y] == 1) {
                            button[x][y].setBackground(Color.ORANGE);
                        }
                    }
                }
            } else { // Do Quantum Mine Stuff
                PinBomb(dif, bomb, flagged);
                // Start at a flagged bomb, and automatically pin bombs that are touching either a pin or flagged bomb
                MoveBomb(dif, bomb, button, num, flagged);
                // If too clumped, scatter the bombs of flagged == 2. AKA fucking yeet them
                YeetBomb(dif, bomb, flagged, button);
            }
        } else {
            if (flagged[a][b] == 1) {
                button[a][b].setBackground(Color.lightGray);
                flagged[a][b] = 0;
            } else {
                button[a][b].setBackground(Color.red);
                flagged[a][b] = 1;
            }
        }
    }

    public static void TPRandom(int dif, int[][] bomb, int[] Before, JButton[][] button) {
        for (int x = 0; x < 10; x++) {
            int RandomX = (int) (Math.random() * dif);
            int RandomY = (int) (Math.random() * dif);

            if (bomb[RandomX][RandomY] == 0) {
                bomb[Before[0]][Before[1]] = 0;
                bomb[RandomX][RandomY] = 1;
                //button[RandomX][RandomY].setBackground(Color.RED);
                return;
            }
        }
    }


    public static void YeetBomb(int dif, int[][] bomb, int[][] flagged, JButton[][] button) {
        for (int x = 0; x < dif; x++) {
            for (int y = 0; y < dif; y++) {
                if ((flagged[x][y] == 1 || flagged[x][y] == 2) && (bomb[x][y] == 1)) {
                    if (LimitReached(x, y, flagged, bomb, new ArrayList<>())) {
                        ArrayList<int[]> Positions = new ArrayList<>();
                        FindAll(x, y, flagged, bomb, Positions);

                        while (Positions.size() > 0) {
                            int[] Current = Positions.remove(0);
                            if (button[Current[0]][Current[1]].getBackground() == Color.RED) {
                                button[Current[0]][Current[1]].setBackground(Color.lightGray);
                            }
                            flagged[Current[0]][Current[1]] = 0;
                            TPRandom(dif, bomb, Current, button);
                        }
                        // Do Stuff
                    }
                }
            }
        }
    }

    static boolean Search(ArrayList<int[]> Positions, int[] Target) {
        for (int[] position : Positions) {
            if (position[0] == Target[0] && position[1] == Target[1]) {
                return true;
            }
        }
        return false;
    }

    static void FindAll(int xPos, int yPos, int[][] flagged, int[][] bomb, ArrayList<int[]> Positions) {
        if (Search(Positions, new int[]{xPos, yPos})) {
            return;
        }
        Positions.add(new int[]{xPos, yPos});

        try {
            if ((flagged[xPos + 1][yPos] == 1 || flagged[xPos + 1][yPos] == 2) && (bomb[xPos + 1][yPos] == 1)) {
                if (!Search(Positions, new int[]{xPos + 1, yPos})) {
                    FindAll(xPos + 1, yPos, flagged, bomb, Positions);
                }
            }
        } catch (Exception ignore) {}

        try {
            if ((flagged[xPos - 1][yPos] == 1 || flagged[xPos - 1][yPos] == 2) && (bomb[xPos - 1][yPos] == 1)) {
                if (!Search(Positions, new int[]{xPos - 1, yPos})) {
                    FindAll(xPos - 1, yPos, flagged, bomb, Positions);
                }
            }
        } catch (Exception ignore) {}

        try {
            if ((flagged[xPos][yPos + 1] == 1 || flagged[xPos][yPos + 1] == 2) && (bomb[xPos][yPos + 1] == 1)) {
                if (!Search(Positions, new int[]{xPos, yPos + 1})) {
                    FindAll(xPos, yPos + 1, flagged, bomb, Positions);
                }
            }
        } catch (Exception ignore) {}

        try {
            if ((flagged[xPos][yPos - 1] == 1 || flagged[xPos][yPos - 1] == 2) && (bomb[xPos][yPos - 1] == 1)) {
                if (!Search(Positions, new int[]{xPos, yPos - 1})) {
                    FindAll(xPos, yPos - 1, flagged, bomb, Positions);
                }
            }
        } catch (Exception ignore) {}
    }


    static boolean LimitReached(int xPos, int yPos, int[][] flagged, int[][] bomb, ArrayList<int[]> Pos) {
        if (Search(Pos, new int[]{xPos, yPos})) {
            return false;
        }
        Pos.add(new int[]{xPos, yPos});

        if (Pos.size() >= 10) {
            return true;
        }

        try {
            if ((flagged[xPos + 1][yPos] == 1 || flagged[xPos + 1][yPos] == 2) && (bomb[xPos + 1][yPos] == 1)) {
                if (!Search(Pos, new int[]{xPos + 1, yPos})) {
                    if (LimitReached(xPos + 1, yPos, flagged, bomb, Pos)) {
                        return true;
                    }
                }
            }
        } catch (Exception ignore) {}

        try {
            if ((flagged[xPos - 1][yPos] == 1 || flagged[xPos - 1][yPos] == 2) && (bomb[xPos - 1][yPos] == 1)) {
                if (!Search(Pos, new int[]{xPos - 1, yPos})) {
                    if (LimitReached(xPos - 1, yPos, flagged, bomb, Pos)) {
                        return true;
                    }
                }
            }
        } catch (Exception ignore) {}

        try {
            if ((flagged[xPos][yPos + 1] == 1 || flagged[xPos][yPos + 1] == 2) && (bomb[xPos][yPos + 1] == 1)) {
                if (!Search(Pos, new int[]{xPos, yPos + 1})) {
                    if (LimitReached(xPos, yPos + 1, flagged, bomb, Pos)) {
                        return true;
                    }
                }
            }
        } catch (Exception ignore) {}

        try {
            if ((flagged[xPos][yPos - 1] == 1 || flagged[xPos][yPos - 1] == 2) && (bomb[xPos][yPos - 1] == 1)) {
                if (!Search(Pos, new int[]{xPos, yPos - 1})) {
                    if (LimitReached(xPos, yPos - 1, flagged, bomb, Pos)) {
                        return true;
                    }
                }
            }
        } catch (Exception ignore) {}

        return false;
    }

    public static void PinBomb(int dif, int[][] bomb, int[][] flagged) {
        for (int x = 0; x < dif; x++) {
            for (int y = 0; y < dif; y++) {
                if (flagged[x][y] == 1 || flagged[x][y] == 2) {
                    try {
                        if (bomb[x + 1][y] == 1) {
                            flagged[x + 1][y] = 2;
                        }
                    } catch (Exception ignore) {}
                    try {
                        if (bomb[x - 1][y] == 1) {
                            flagged[x - 1][y] = 2;
                        }
                    } catch (Exception ignore) {}
                    try {
                        if (bomb[x][y + 1] == 1) {
                            flagged[x][y + 1] = 2;
                        }
                    } catch (Exception ignore) {}
                    try {
                        if (bomb[x][y - 1] == 1) {
                            flagged[x][y - 1] = 2;
                        }
                    } catch (Exception ignore) {}
                }
            }
        }
        for (int x = dif - 1; x >= 0; x--) {
            for (int y = dif - 1; y >= 0; y--) {
                if (flagged[x][y] == 1 || flagged[x][y] == 2) {
                    if (flagged[x][y] == 1 || flagged[x][y] == 2) {
                        try {
                            if (bomb[x + 1][y] == 1) {
                                flagged[x + 1][y] = 2;
                            }
                        } catch (Exception ignore) {}
                        try {
                            if (bomb[x - 1][y] == 1) {
                                flagged[x - 1][y] = 2;
                            }
                        } catch (Exception ignore) {}
                        try {
                            if (bomb[x][y + 1] == 1) {
                                flagged[x][y + 1] = 2;
                            }
                        } catch (Exception ignore) {}
                        try {
                            if (bomb[x][y - 1] == 1) {
                                flagged[x][y - 1] = 2;
                            }
                        } catch (Exception ignore) {}
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        System.out.println("Select a difficulty\nEasy\nMedium \\ Normal\nHard");
        int x;
        String dif = kb.nextLine();
        if (dif.equalsIgnoreCase("easy")) {
            x = 10;
        } else if (dif.equalsIgnoreCase("medium") || dif.equalsIgnoreCase("normal")) {
            x = 15;
        } else if (dif.equalsIgnoreCase("hard")) {
            x = 20;
        } else if (dif.equalsIgnoreCase("custom")) {
            try {
                x = kb.nextInt();
            } catch (Exception e) {
                x = 30;
            }
        } else {
            System.out.println("ERROR... EXPERT MODE");
            x = 30;
        }
        QuantumSweeper oof = new QuantumSweeper(x);
    }
}
