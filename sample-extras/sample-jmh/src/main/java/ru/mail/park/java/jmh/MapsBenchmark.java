package ru.mail.park.java.jmh;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.koloboke.collect.map.IntObjMap;
import com.koloboke.collect.map.hash.HashIntObjMaps;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(3)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class MapsBenchmark {

    @Param({"1000", "100000"})
    public int size;

    private final MutableIntObjectMap<Object> eclipseMap = new IntObjectHashMap<>();
    private final Map<Integer, Object> hashMap = new HashMap<>();
    private final IntObjMap<Object> koloboke = HashIntObjMaps.newMutableMap();
    private final Random r = new Random();

    @Setup
    public void setup() {
        for (int i = 0; i < size; i++) {
            Object value = new Object();
            eclipseMap.put(i, value);
            hashMap.put(i, value);
            koloboke.put(i, value);
        }
    }

    @Benchmark
    public Object eclipse() {
        return eclipseMap.get(r.nextInt(size));
    }

    @Benchmark
    public Object hashMap() {
        return hashMap.get(r.nextInt(size));
    }

    @Benchmark
    public Object koloboke() {
        return koloboke.get(r.nextInt(size));
    }
}
