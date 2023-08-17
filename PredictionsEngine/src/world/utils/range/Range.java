package world.utils.range;

import java.util.Objects;

public class Range<T extends Number & Comparable<T>> {
    private T from;
    private T to;

    public Range(T from, T to){
        this.from = from;
        this.to = to;
    }
    public T getFrom() {
        return from;
    }

    public Double getFromDouble (){
        return from.doubleValue();
    }

    public Double getToDouble(){
        return to.doubleValue();
    }

    public T getTo() {
        return to;
    }

    public double getRange () {
        double fromValue = from.doubleValue();
        double toValue = to.doubleValue();
        return (fromValue - toValue);
    }

    public void setFrom(T from) {
        this.from = from;
    }

    public void setTo(T to) {
        this.to = to;
    }

    public void setRange (T from, T to){
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Range<?> range = (Range<?>) o;
        return Objects.equals(from, range.from) && Objects.equals(to, range.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "Range{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
