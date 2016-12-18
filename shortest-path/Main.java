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
    static int n;
    static int[][] adjMatrix;

    public static String print(int time) {
        String hour = (time / 60) + ":";
        int min = time % 60;

        if (min < 10)
            return hour += "0" + min + "\n";
        else
            return hour += min + "\n";
    }

    public static void dijkstra(int start, final int[] dist) {

        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(11, new Comparator<Integer>() {
            @Override
            public int compare(Integer p, Integer q) {
                return (dist[p] - dist[q]);
            }
        });

        for (int i = 0; i <= n; i++) {
            if (adjMatrix[start][i] != 0) {
                dist[i] = adjMatrix[start][i];
                queue.offer(i);
            }
        }

        while (queue.size() > 0) {
            int index = queue.poll();

            for (int a = 0; a <= n; a++) {
                if (adjMatrix[index][a] != 0) {
                    // decrease key
                    if (dist[a] > dist[index] + adjMatrix[index][a]) {
                        queue.remove(a);
                        dist[a] = dist[index] + adjMatrix[index][a];
                        queue.offer(a);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int N = Integer.parseInt(in.readLine());
        int INF = Integer.MAX_VALUE / 2;

        for (int cIndex = 1; cIndex <= N; cIndex++) {
            sb.append("Case #" + cIndex + ": ");

            HashMap<Integer, Integer> supermarket = new HashMap<Integer, Integer>();
            int leaCity;
            int peterCity;
            int marketNo;

            String[] nmsab = in.readLine().split(" ");

            int n = Integer.parseInt(nmsab[0]) - 1;
            int m = Integer.parseInt(nmsab[1]);
            marketNo = Integer.parseInt(nmsab[2]);
            leaCity = Integer.parseInt(nmsab[3]) - 1;
            peterCity = Integer.parseInt(nmsab[4]) - 1;

            adjMatrix = new int[n + 1][n + 1];
            final int[] distLea = new int[n + 1];
            final int[] distPeter = new int[n + 1];

            for (int i = 0; i < m; i++) {
                String[] uwc = in.readLine().split(" ");
                int u = Integer.parseInt(uwc[0]) - 1;
                int v = Integer.parseInt(uwc[1]) - 1;
                int c = Integer.parseInt(uwc[2]);

                if (adjMatrix[u][v] != 0) {
                    adjMatrix[u][v] = Math.min(c, adjMatrix[u][v]);
                    adjMatrix[v][u] = Math.min(c, adjMatrix[v][u]);
                } else {
                    adjMatrix[u][v] = c;
                    adjMatrix[v][u] = c;
                }
            }

            for (int i = 0; i < marketNo; i++) {
                String[] tmp = in.readLine().split(" ");
                int city = Integer.parseInt(tmp[0]) - 1;
                int smarket = Integer.parseInt(tmp[1]);

                if (supermarket.get(city) != null) {
                    if (supermarket.get(city) > smarket)
                        supermarket.put(city, smarket);
                } else
                    supermarket.put(city, smarket);
            }


            for (int i = 0; i <= n; i++) {
                distLea[i] = INF;
                distPeter[i] = INF;
            }

            distLea[leaCity] = 0;
            distPeter[peterCity] = 0;

            dijkstra(leaCity, distLea);
            dijkstra(peterCity, distPeter);


            int min = INF;
            for (int i = 0; i <= n; i++) {
                if (supermarket.get(i) != null) {
                    System.out.println("Lea: " + distLea[i]);
                    if (distLea[i] != INF && distPeter[i] != INF) {

                        min = Math.min(min, distLea[i] + supermarket.get(i) + distPeter[i]);
                    }
                }

            }

            sb.append(min);

            in.readLine();
            sb.append("\n");
        }
        System.out.println(sb);
    }
}
