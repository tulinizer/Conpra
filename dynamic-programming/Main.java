/*
 * @author : Tulin Izer
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


public class Main {

    static ArrayList<Case> allRotation;
    static int CASENO;
    static Case[] origCase;

    public static class Case implements Comparable<Case> {
        int width;
        int length;
        int height;

        public Case(int length, int width, int height) {
            this.length = length;
            this.width = width;
            this.height = height;
        }

        public Case() {
        }

        static Case createCase(int height, int x, int y) {
            Case c = new Case();
            c.height = height;
            if (x >= y) {
                c.length = x;
                c.width = y;
            } else {
                c.length = y;
                c.width = x;
            }
            return c;
        }

        @Override
        public int compareTo(Case c) {
            if (this.length * this.width >= c.length * c.width) {
                return -1;
            } else {
                return 1;
            }
        }

    }

    public static int createRotations() {
        int index = 0;
        for (int i = 0; i < CASENO; i++) {
            int[] sortArr = new int[3];
            sortArr[0] = origCase[i].length;
            sortArr[1] = origCase[i].width;
            sortArr[2] = origCase[i].height;

            Arrays.sort(sortArr);

            int height = sortArr[0];
            int width = sortArr[1];
            int length = sortArr[2];

            allRotation.add(new Case(length, width, height));
            allRotation.add(new Case(width, height, length));
            index += 2;
            if (width != height) {
                allRotation.add(new Case(length, height, width));
                index++;
            }
        }
        return index;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int N = Integer.parseInt(in.readLine());

        for (int cIndex = 1; cIndex <= N; cIndex++) {
            sb.append("Case #" + cIndex + ": ");

            int FARMERS = Integer.parseInt(in.readLine());

            String[] nm = in.readLine().split(" ");

            int NEEDEDHEIGHT = Integer.parseInt(nm[0]);
            CASENO = Integer.parseInt(nm[1]);

            origCase = new Case[CASENO + 1];
            allRotation = new ArrayList<Case>();


            for (int i = 0; i < CASENO; i++) {
                String[] tmp = in.readLine().split(" ");
                int x = Integer.parseInt(tmp[0]);
                int y = Integer.parseInt(tmp[1]);
                int z = Integer.parseInt(tmp[2]);

                origCase[i] = new Case(x, y, z);
            }

            int ALLROTCASE = createRotations();

            int[] maxBox = new int[ALLROTCASE + 1];
            int[] result = new int[ALLROTCASE + 1];
            


            /* sort in descending order according to their bases */
            Collections.sort(allRotation);


            for (int i = 0; i < ALLROTCASE; i++) {
                maxBox[i] = allRotation.get(i).height;
            }
            for (int i = 0; i < ALLROTCASE; i++) {
                result[i] = i;
            }


            int i = 1;
            int j = 0;
            while (i < ALLROTCASE - 1 || j < ALLROTCASE - 1) {

                if (allRotation.get(i).width < allRotation.get(j).width &&
                        allRotation.get(i).length < allRotation.get(j).length) {
                    maxBox[i] = Math.max(maxBox[i], allRotation.get(i).height + maxBox[j]);
                    result[i] = j;
                }
                if (i - j == 1 && i != ALLROTCASE - 1) {
                    j = 0;
                    i++;
                } else {
                    j++;
                }
            }

            boolean possible = false;
            for (int p = 0; p < ALLROTCASE; p++) {
                if (maxBox[p] >= NEEDEDHEIGHT)
                    possible = true;
            }

            if (possible)
                sb.append("yes");
            else
                sb.append("no");

            in.readLine();
            sb.append("\n");
        }
        System.out.println(sb);
    }
}
