package trade.wayruha.mexc.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copy-paster from Huobi API SDK
 */
public class IdGenerator {
  private static AtomicInteger COUNTER = new AtomicInteger();

  private static Set<Long> TIME_SET = new HashSet<>();

  public static int getNextId(){
    final Long time = System.currentTimeMillis();

    if (!TIME_SET.contains(time)) {
      COUNTER.set(0);
      TIME_SET.clear();
      TIME_SET.add(time);
    }

    int id = COUNTER.addAndGet(1);
    return id;
  }
}
