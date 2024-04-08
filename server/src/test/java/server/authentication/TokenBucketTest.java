package server.authentication;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.Authentication.TokenBucket;

import static org.junit.jupiter.api.Assertions.*;

public class TokenBucketTest {

    @Test
    public void testTryConsumeWithTokensAvailable() {
        TokenBucket tokenBucket = new TokenBucket(10, 1000); // Capacity of 10 tokens, refill every 1 second
        assertTrue(tokenBucket.tryConsume()); // Initial consume, should return true
    }

    @Test
    public void testTryConsumeWithNoTokensAvailable() {
        TokenBucket tokenBucket = new TokenBucket(0, 1000); // Capacity of 0 tokens, refill every 1 second
        assertFalse(tokenBucket.tryConsume()); // Initial consume, should return false
    }

    @Test
    public void testRefill() {
        TokenBucket tokenBucket = new TokenBucket(10, 1000); // Capacity of 10 tokens, refill every 1 second
        assertTrue(tokenBucket.tryConsume()); // Consume one token
        tokenBucket.refill(); // Manually refill
        assertTrue(tokenBucket.tryConsume()); // Try to consume again, should return true
    }

    @Test
    public void testRefillWithElapsedTime() {
        TokenBucket tokenBucket = new TokenBucket(10, 1000); // Capacity of 10 tokens, refill every 1 second
        tokenBucket.tryConsume(); // Consume one token
        // Mock the last refill timestamp to simulate elapsed time
        tokenBucket = Mockito.spy(tokenBucket);
        Mockito.when(tokenBucket.getLastRefillTimestamp()).thenReturn(System.currentTimeMillis() - 2000);
        tokenBucket.refill(); // Manually refill with elapsed time of 2 seconds
        assertTrue(tokenBucket.tryConsume()); // Try to consume again, should return true
    }

    @Test
    public void testBlockingAfterTooManyRequests() {
        // Create a TokenBucket with a capacity of 5 tokens and refill every 1000 milliseconds (1 second)
        TokenBucket tokenBucket = new TokenBucket(5, 1000);

        // Simulate the token bucket being empty
        tokenBucket.tryConsume();
        tokenBucket.tryConsume();
        tokenBucket.tryConsume();
        tokenBucket.tryConsume();
        tokenBucket.tryConsume(); // Consume all tokens

        // Spy on the token bucket to mock the last refill timestamp
        tokenBucket = Mockito.spy(tokenBucket);
        // Mock the last refill timestamp to a time before the current time
        Mockito.when(tokenBucket.getLastRefillTimestamp()).thenReturn(System.currentTimeMillis() - 3000);

        // Attempt to consume tokens beyond the capacity
        boolean canConsume = tokenBucket.tryConsume();

        // Assert that the token consumption is blocked
        assertFalse(canConsume);
    }
}