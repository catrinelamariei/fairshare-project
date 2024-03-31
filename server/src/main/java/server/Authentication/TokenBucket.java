package server.Authentication;

import java.util.concurrent.atomic.AtomicLong;

public class TokenBucket {
    private final long capacity;
    private final long refillTimeMs;
    private final AtomicLong tokens;
    private volatile long lastRefillTimestamp;

    public TokenBucket(long capacity, long refillTimeMs) {
        this.capacity = capacity;
        this.refillTimeMs = refillTimeMs;
        this.tokens = new AtomicLong(capacity);
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    public synchronized boolean tryConsume() {
        refill();
        return tokens.getAndUpdate(current -> current > 0 ? current - 1 : 0) > 0;
    }

    private synchronized void refill() {
        long now = System.currentTimeMillis();
        long elapsedTime = Math.max(0, now - lastRefillTimestamp);
        long tokensToAdd = elapsedTime * capacity / refillTimeMs;
        if (tokensToAdd > 0) {
            tokens.set(Math.min(capacity, tokens.get() + tokensToAdd));
            lastRefillTimestamp = now;
        }
    }
}
