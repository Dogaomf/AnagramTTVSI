package br.com.anagram.controller;

import br.com.anagram.service.AnagramService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/anagrams")
public class AnagramController {
    private final AnagramService anagramService;

    public AnagramController(AnagramService anagramService) {
        this.anagramService = anagramService;
    }

    @GetMapping("/{input}")
    public List<String> generateAnagrams(@PathVariable String input) {
        return anagramService.generateAnagrams(input);
    }
}
