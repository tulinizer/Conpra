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
    static class Line {
        Point p1;
        Point p2;
        Point lineRep;
        int id;
        int waterPassedLower;
        int waterPassedUpper;
        int horizontalLedge;

        Line(Point p1, Point p2, int id, int waterPassedLower, int waterPassedUpper, int horizontalLedge) {
            this.p1 = p1;
            this.p2 = p2;
            this.lineRep = crossProduct(p1, p2);
            this.id = id;
            this.waterPassedLower = waterPassedLower;
            this.waterPassedUpper = waterPassedUpper;
            this.horizontalLedge = horizontalLedge;
        }
    }

    static class Point {
        double x;
        double y;
        double z;

        Point(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static Point crossProduct(Point a, Point b) {
        double x1 = (a.y * b.z) - (a.z * b.y);
        double y1 = (a.z * b.x) - (a.x * b.z);
        double z1 = (a.x * b.y) - (a.y * b.x);

        return new Point(x1, y1, z1);
    }

    public static Point dehomogenization(Point a) {
        return new Point(a.x / a.z, a.y / a.z, 1);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int N = Integer.parseInt(in.readLine());

        for (int cIndex = 1; cIndex <= N; cIndex++) {
            sb.append("Case #" + cIndex + ": ");

            String[] nxy = in.readLine().split(" ");
            int n = Integer.parseInt(nxy[0]);
            int x_source = Integer.parseInt(nxy[1]);
            int y_source = Integer.parseInt(nxy[2]);

            Point sourcePoint = new Point(x_source, y_source, 1);
            ArrayList<Integer> resultArr = new ArrayList<Integer>();
            Queue<Point> queue = new LinkedList();

            queue.add(sourcePoint);

            Line[] ledges = new Line[n + 1];

            for (int i = 0; i < n; i++) {
                String[] xy = in.readLine().split(" ");

                int x1 = Integer.parseInt(xy[0]);
                int y1 = Integer.parseInt(xy[1]);
                int x2 = Integer.parseInt(xy[2]);
                int y2 = Integer.parseInt(xy[3]);

                Point x = new Point(x1, y1, 1);
                Point y = new Point(x2, y2, 1);

                ledges[i] = new Line(x, y, i, 0, 0, 0);
            }

            ledges[n] = new Line(new Point(0, 0, 1), new Point(101, 0, 1), n, 0, 0, 0);
            Point next = new Point(0, 0, 1);

            while (!queue.isEmpty()) {

                Point waterSource = queue.poll();
                Point sourceLine = crossProduct(waterSource, new Point(waterSource.x, 0, 1));


                double higher_ledge_y = -1.0;
                int currentLedgeId = -1;

                for (Line l : ledges) {
                    double minY_edge = Math.min(l.p1.y, l.p2.y);
                    double maxY_edge = Math.max(l.p1.y, l.p2.y);
                    double maxX_edge = Math.max(l.p1.x, l.p2.x);
                    double minX_edge = Math.min(l.p1.x, l.p2.x);

                    if (waterSource.y <= minY_edge) {
                        continue;
                    }

                    Point tmp = dehomogenization(crossProduct(sourceLine, l.lineRep));


                    if (!((tmp.y >= minY_edge) && (tmp.y <= maxY_edge)) ||
                            !((tmp.x >= minX_edge) && (tmp.x <= maxX_edge)) ||
                            (tmp.y > waterSource.y) || (tmp.y == waterSource.y && tmp.x == waterSource.x))
                        continue;

                    if (tmp.y > higher_ledge_y) {
                        currentLedgeId = l.id;
                        higher_ledge_y = tmp.y;
                    }
                }

                // update source point
                Point lowerPoint;
                Point upperPoint;

                if (ledges[currentLedgeId].p1.y > ledges[currentLedgeId].p2.y) {
                    lowerPoint = new Point(ledges[currentLedgeId].p2.x, ledges[currentLedgeId].p2.y, 1);
                    upperPoint = new Point(ledges[currentLedgeId].p1.x, ledges[currentLedgeId].p1.y, 1);
                } else {
                    lowerPoint = new Point(ledges[currentLedgeId].p1.x, ledges[currentLedgeId].p1.y, 1);
                    upperPoint = new Point(ledges[currentLedgeId].p2.x, ledges[currentLedgeId].p2.y, 1);
                }

                next = dehomogenization(crossProduct(sourceLine, ledges[currentLedgeId].lineRep));

                if (ledges[currentLedgeId].waterPassedLower == 1) {

                    if (ledges[currentLedgeId].horizontalLedge == 0 ||
                            (ledges[currentLedgeId].horizontalLedge == 1 && ledges[currentLedgeId].waterPassedUpper == 1)) {
                        // check wheter we are at a vertex

                        if (next.y == upperPoint.y && next.x == upperPoint.x &&
                                ledges[currentLedgeId].waterPassedUpper == 0) {
                            ledges[currentLedgeId].waterPassedUpper = 1;

                        } else {
                            continue;
                        }
                    }
                }

                // water source made it to the ground?
                if (currentLedgeId == -1 || currentLedgeId == n || higher_ledge_y == 0) {
                    Double d = waterSource.x;
                    int i = d.intValue();
                    if (!resultArr.contains(i))
                        resultArr.add(i);

                } else {
                    // if ledge is horizontal
                    if (upperPoint.y == lowerPoint.y) {
                        queue.add(new Point(lowerPoint.x, lowerPoint.y, 1));
                        waterSource = new Point(upperPoint.x, upperPoint.y, 1);

                        // water hit a vertex, some will go down, some will continue through ledge
                    } else if (next.y == upperPoint.y && next.x == upperPoint.x) {
                        if (ledges[currentLedgeId].waterPassedUpper == 1) {
                            waterSource = new Point(next.x, next.y, 1);

                        } else {
                            queue.add(upperPoint);
                            waterSource = new Point(lowerPoint.x, lowerPoint.y, 1);
                            ledges[currentLedgeId].waterPassedUpper = 1;
                        }

                    } else {
                        waterSource = new Point(lowerPoint.x, lowerPoint.y, 1);
                    }

                    queue.add(waterSource);
                    ledges[currentLedgeId].waterPassedLower = 1;

                }
            }

            Collections.sort(resultArr);
            for (int p : resultArr) {
                sb.append(p + " ");
            }


            in.readLine();
            sb.append("\n");
        }
        System.out.println(sb);
    }
}
