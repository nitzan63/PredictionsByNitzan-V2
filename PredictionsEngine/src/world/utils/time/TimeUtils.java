package world.utils.time;

public class TimeUtils {
    public static int getElapsedSeconds (long startTimeMillis){
        long currentTimeMillis = System.currentTimeMillis();
        long elapsedMillis = currentTimeMillis - startTimeMillis;
        return (int) (elapsedMillis / 1000);
    }
}
