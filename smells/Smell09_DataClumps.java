/**
 * SMELL i: DATA CLUMPS
 *
 * The same group of values (latitude, longitude) travels together everywhere. If you deleted one of them,
 * the others would stop making sense.
 *
 * TASK
 *  1. Spot the clump(s).
 *  2. Introduce a small class/record for it (Extract Class + Introduce Parameter Object) and give it behaviour
 *     if it deserves some (e.g. distanceTo).
 *
 * DONE WHEN: demo() output is unchanged AND no method has more than 3 parameters.
 */
public class Smell09_DataClumps {

    static double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = (lat1 - lat2) * 111;
        double dLon = (lon1 - lon2) * 111 * Math.cos(Math.toRadians((lat1 + lat2) / 2));
        return Math.hypot(dLat, dLon);
    }

    static boolean isInside(double lat, double lon, double centerLat, double centerLon, double radiusKm) {
        return distanceKm(lat, lon, centerLat, centerLon) <= radiusKm;
    }

    static String label(String name, double lat, double lon) {
        return name + "@" + lat + "," + lon;
    }

    static String demo() {
        return Math.round(distanceKm(0, 0, 0, 1)) + ";" + isInside(0, 0.5, 0, 0, 100) + ";" + label("HQ", 1.5, 2.5);
    }
}
