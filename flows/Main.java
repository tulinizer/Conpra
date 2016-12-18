/*
 * @author : Tulin Izer
 *
 */

import java.lang.StringBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;


public class Main {
    static int[][] capacity;
    static int[][] flow;
    static int[] dist;
    static int[] exess;
    static int TOTALNODE;
    static int SINK;
    static Queue<Integer> queue;

    public static void relabel(int node) {
        int min = Integer.MAX_VALUE / 2;

        for (int i = 0; i < TOTALNODE; i++) {
            if (capacity[node][i] != 0) {
                if (dist[i] >= dist[node] && min > dist[i])
                    min = dist[i];
            }
        }
        dist[node] = min + 1;

        queue.add(node);
    }

    public static void push(int node) {
        int push = 0;
        int neigh = -1;

        while (exess[node] > 0 && (neigh = legalNeigh(node)) != -1) {
            int residual = capacity[node][neigh] - flow[node][neigh];
            push = Math.min(exess[node], residual);

            exess[neigh] += push;
            exess[node] -= push;

            flow[node][neigh] += push;
            flow[neigh][node] -= push;


            if (neigh != 0 && neigh != SINK && !queue.contains(neigh)) {
                queue.add(neigh);
            }
        }

        if (exess[node] > 0)
            relabel(node);
    }

    public static int legalNeigh(int node) {
        for (int i = 0; i < TOTALNODE; i++) {
            if (capacity[node][i] != 0) {
                int residual = capacity[node][i] - flow[node][i];
                if (dist[i] < dist[node] && residual > 0)
                    return i;
            }
        }

        return -1;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int N = Integer.parseInt(in.readLine());

        for (int cIndex = 1; cIndex <= N; cIndex++) {
            sb.append("Case #" + cIndex + ": ");

            String[] nkml = in.readLine().split(" ");

            int FRIENDS = Integer.parseInt(nkml[0]);
            int PRESENTS = Integer.parseInt(nkml[1]);

            TOTALNODE = FRIENDS + PRESENTS + 2;
            SINK = TOTALNODE - 1;

            capacity = new int[TOTALNODE][TOTALNODE];
            flow = new int[TOTALNODE][TOTALNODE];
            exess = new int[TOTALNODE];
            dist = new int[TOTALNODE];

            queue = new LinkedList();

            boolean impossible = false;

            for (int i = 1; i <= FRIENDS; i++) {
                String[] tmp = in.readLine().split(",");

                if (tmp[0].equals("")) {
                    impossible = true;
                    for (int rest = i + 1; rest <= FRIENDS; rest++)
                        in.readLine();
                    break;
                }

                for (int j = 0; j < tmp.length; j++) {
                    if (tmp[j].contains("-")) {

                        String[] tmp2 = tmp[j].split("-");
                        int start = Integer.parseInt(tmp2[0]);
                        int end = Integer.parseInt(tmp2[1]);

                        for (int t = start; t <= end; t++) {
                            capacity[i][FRIENDS + t] = 1;
                            capacity[FRIENDS + t][i] = 1;
                        }
                    } else {

                        capacity[i][FRIENDS + Integer.parseInt(tmp[j])] = 1;
                        capacity[FRIENDS + Integer.parseInt(tmp[j])][i] = 1;
                    }
                }
            }

            if (!impossible) {

                dist[0] = SINK + 1;

                // create sink
                for (int i = FRIENDS + 1; i < SINK; i++) {
                    capacity[i][SINK] = 1;
                }

                // initialize the preflow
                for (int i = 1; i <= FRIENDS; i++) {
                    exess[0] += capacity[0][i];
                    exess[i] = 1;

                    capacity[0][i] = 1;
                    capacity[i][0] = 1;

                    flow[0][i] = 1;
                    flow[i][0] = -1;

                    queue.add(i);
                }

                // Main Loop
                while (!queue.isEmpty()) {
                    int node = queue.poll();

                    push(node);
                }

                if (exess[SINK] != FRIENDS)
                    sb.append("no");
                else
                    sb.append("yes");
            } else
                sb.append("no");

            in.readLine();
            sb.append("\n");
        }
        System.out.println(sb);
    }
}
