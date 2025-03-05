package br.com.anagram.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnagramService {
    public List<String> generateAnagrams(String input) {
        if (input == null || input.isEmpty() || !input.matches("[a-zA-Z]+")) {
            throw new IllegalArgumentException("Input must be a non-empty string containing only letters.");
        }
        List<String> result = new ArrayList<>();
        permute(input.toCharArray(), 0, result);
        return result;
    }

    private void permute(char[] chars, int index, List<String> result) {
        if (index == chars.length - 1) {
            result.add(new String(chars));
            return;
        }
        for (int i = index; i < chars.length; i++) {
            swap(chars, i, index);
            permute(chars, index + 1, result);
            swap(chars, i, index);
        }
    }

    private void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }
}