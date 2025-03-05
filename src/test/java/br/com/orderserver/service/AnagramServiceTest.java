
package br.com.orderserver.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AnagramServiceTest {

    @Autowired
    private AnagramService anagramService;

    @Test
    void testGenerateAnagrams_NormalCase() {
        List<String> anagrams = anagramService.generateAnagrams("abc");
        assertEquals(6, anagrams.size());
        assertTrue(anagrams.containsAll(List.of("abc", "acb", "bac", "bca", "cab", "cba")));
    }

    @Test
    void testGenerateAnagrams_SingleLetter() {
        List<String> anagrams = anagramService.generateAnagrams("a");
        assertEquals(1, anagrams.size());
        assertEquals("a", anagrams.get(0));
    }

    @Test
    void testGenerateAnagrams_EmptyInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> anagramService.generateAnagrams(""));
        assertEquals("Input must be a non-empty string containing only letters.", exception.getMessage());
    }
}