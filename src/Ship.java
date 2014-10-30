import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Ship extends SpaceObject {
    Planet destination;
    Boolean arrived;
    List<Point> points = new LinkedList<Point>();
    List<Point> helpPoints = new LinkedList<Point>();


    public Ship(Planet destination) {
        arrived = false;
        this.destination = destination;
    }

    public void findWay(Gameplay gameplay, Planet from, Planet to) {

        Waypoint start = new Waypoint(pos); //начальная точка
        Tree<Waypoint> waypoints = new Tree<Waypoint>(start); //дерево возможных путей, узлы - промежуточные точки

        double minDistToPlanet = 10; //атмосфера планеты, а мы в атмосфере не летаем

        //последняя ближайщая планета, чтобы решить, искать ли новые точки касания или продолжать облёт текущей
        Planet lastNearest = null;

        //угол в радианах, на который будем сдвигать точку за одну итерацию
        double delta = Math.PI / 90;
        //направление этого самого сдвига. при кажом новом облёте планеты определяется большим куском кода
        Integer deltaSign = null;
        Double degree = null; //текущий угол точки (когда облетам планету)

        //
        Double minDistance = null;

        while (true) { // стоставляем дерево и сразу же ищем точку с минимальным путём

            //если есть дети, и детей ещё не смотрели - к первому ребёнку
            //если нет, и если есть братан - к братану
            //если нет, и не корень дерева, то мы можем подняться повыше
            //если корень, который мы уже рассматривали - выходим
            if (waypoints.haveChildren() && !waypoints.wasIterated()) {
                waypoints.goToFirstChild();
            } else if (waypoints.isNext()) {
                waypoints.goToNext();
            } else if (!waypoints.isRoot()) {
                waypoints.goToParent();
                continue; // родителя мы полюбому рассматривали
            } else if (waypoints.wasIterated()) {
                break;
            }

            Waypoint currentPos = waypoints.get();


            //TODO: вынести поиск в отдельный метод класса Gameplay
            /*Planet nearest = null;
            double distToNearest = Geometry.distance(to.pos, currentPos);
            for (Planet planet : gameplay.planets) {
                if (planet != fromList && planet != to &&
                        Geometry.distance(planet.pos, to.pos) < Geometry.distance(to.pos, currentPos) &&
                        Geometry.isLineAndCircleIntersect(currentPos, to.pos, planet.pos, planet.radius + minDistToPlanet)
                        ) {
                    if (Geometry.distance(planet.pos, currentPos) < distToNearest) {
                        distToNearest = Geometry.distance(planet.pos, currentPos);
                        nearest = planet;
                    }
                }
            }*/
            //находим близжайшую планету на пути к цели
            Set<Planet> exclude = new HashSet<Planet>();
            exclude.add(from);
            exclude.add(to);
            Planet nearest = gameplay.findNearestPlanetOnTheWayTo(currentPos, to.pos, minDistToPlanet, exclude);


            if (nearest != null) { //блин, на пути встретилась ещё одна планета
                if (currentPos.tangency) {
                    //текущая точка получена, когда искали касательные, то есть их было две
                    //и когда мы возвращаемся снизу дерева ко второй касательной, нужно вернуть её условия
                    //условия - ближайщую планету
                    //чтобы было понятно, что надо делать её облёт, а не искать новые касательные
                    lastNearest = currentPos.nearest;
                    deltaSign = null;
                }

                if (lastNearest == nearest) { // если тру, то делаем облёт

                    //определяем знак дэльты и текущий угол
                    if (deltaSign == null || degree == null) {
                        Point[] points = Geometry.getLineAndCircleIntersection(currentPos, to.pos, nearest.pos, nearest.radius + minDistToPlanet);

                        Point firstPoint = points[0].equals(currentPos) ? points[0] : points[1]; //текущая точка
                        Point secondPoint = points[0].equals(currentPos) ? points[1] : points[0];

                        firstPoint = Geometry.divideBy(Geometry.subtract(firstPoint, nearest.pos), nearest.radius + minDistToPlanet);
                        secondPoint = Geometry.divideBy(Geometry.subtract(secondPoint, nearest.pos), nearest.radius + minDistToPlanet);

                        degree = Math.acos(firstPoint.x);
                        if (Math.asin(firstPoint.y) < 0) {
                            //на самом деле угол отрицательный, но я хочу работать с углами [0;2*pi]
                            degree = degree * -1 + Math.PI * 2;
                        }

                        double degree2 = Math.acos(secondPoint.x);
                        if (Math.asin(secondPoint.y) < 0) {
                            //на самом деле угол отрицательный, но я хочу работать с углами [0;2*pi]
                            degree2 = degree2 * -1 + Math.PI * 2;
                        }

                        if (degree - degree2 < 0) { //хвала тому, что углы [0;2*pi] !
                            deltaSign = 1;
                        } else {
                            deltaSign = -1;
                        }
                    }

                    degree += delta * deltaSign;

                    Point point = new Point(Math.cos(degree) * (nearest.radius + minDistToPlanet) + nearest.pos.x,
                            Math.sin(degree) * (nearest.radius + minDistToPlanet) + nearest.pos.y);
                    waypoints.add(new Waypoint(point, currentPos.distance + Geometry.distance(point, currentPos)));
                } else {
                    deltaSign = null; //всё, облёт кончен, нужно будет заново искать
                    Point[] points = Geometry.getTangencyPoints(currentPos,
                            nearest.pos, nearest.radius + minDistToPlanet);
                    if (points == null) {
                        System.out.println("Не найдено точек пересечения, чё за нах");
                        break;
                    }
                    if (points.length < 2) {
                        System.out.println("Чё-то мало точек нашли");
                        break;
                    }
                    waypoints.add(new Waypoint(points[0],
                                    currentPos.distance + Geometry.distance(points[0], currentPos), true, nearest)
                    );
                    waypoints.add(new Waypoint(points[1],
                                    currentPos.distance + Geometry.distance(points[1], currentPos), true, nearest)
                    );


                    lastNearest = nearest;
                }
            } else {
                deltaSign = null; //всё, облёт кончен, нужно будет заново искать
                currentPos.last = true;
                lastNearest = null;

                //вычисление наименьшего расстояния
                if (minDistance == null || currentPos.distance < minDistance) {
                    waypoints.rememberElement();
                    minDistance = currentPos.distance;
                }

                if (waypoints.size() == 1) { //а это если не встретилось планет на пути, ура
                    break;
                }
            }
        }
        //теперь либо всё конечные точки запомним ещё том цикле, и сразу среди них выберем с минимальным расстоянием
        //либо пройдёмся по всем точкам дерева, и среди них рассмотрим те, которые last=true, но фак, это же тупо, нах это надо
        //потом от конечной точки по дереву пройдёмся вверх
        //и захерачим все точки в this.points, в обратном порядке

        //сейчас вроде всё правильно, баги только если планеты накладываются

        waypoints.startIteration();
        waypoints.goToMemory();
        this.points.add(to.pos);
        while (!waypoints.isRoot()){
            this.points.add(0, waypoints.get());
            waypoints.goToParent();
        }
    }
}
