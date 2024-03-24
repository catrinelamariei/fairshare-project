package server.authentication;

import org.junit.jupiter.api.Test;
import server.Authentication.CodeGenerator;


import static org.junit.jupiter.api.Assertions.*;

public class CodeGeneratorTest {

    @Test
    public void testGenerateRandomStringWithPositiveLength() {
        int length = 10;

        String randomString = CodeGenerator.generateRandomString(length);

        assertNotNull(randomString);
        assertEquals(length, randomString.length());
        assertTrue(randomString.matches("[A-Z0-9]+"));
    }

    @Test
    public void testGenerateRandomStringWithZeroLength() {
        int length = 0;

        String randomString = CodeGenerator.generateRandomString(length);

        assertNotNull(randomString);
        assertEquals(0, randomString.length());
    }

    @Test
    public void testGenerateRandomStringWithNegativeLength() {
        int length = -5;
        try {
            CodeGenerator.generateRandomString(length);
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testUniqueRandomStrings() {
        // Generate multiple random strings
        int numStrings = 1000;
        String[] randomStrings = new String[numStrings];
        for (int i = 0; i < numStrings; i++) {
            randomStrings[i] = CodeGenerator.generateRandomString(10);
        }

        // Assert that all generated strings are unique
        for (int i = 0; i < numStrings - 1; i++) {
            for (int j = i + 1; j < numStrings; j++) {
                assertNotEquals(randomStrings[i], randomStrings[j]);
            }
        }
    }
}
