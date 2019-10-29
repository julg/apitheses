package fr.abes.apitheses.controller;

import fr.abes.apitheses.builder.SearchQueryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/theses/v1")
public class SearchController {

    @Autowired
    private SearchQueryBuilder searchQueryBuilder;

    @GetMapping(value = "/search/{chaine}")
    public String getThesesByAbstractAndTitle(@PathVariable final String chaine) {
        try {
            return searchQueryBuilder.getThesesByTitleAndAbstract(chaine).toString();
        } catch (IOException e) {
            log.error(e.toString());
            return null;
        }
    }
    @GetMapping(value = "/search/{etab}/{chaine}")
    public String getThesesByAbstractAndTitleAndEtab(@PathVariable final String etab, @PathVariable final String chaine) {
        try {
            return searchQueryBuilder.getThesesByTitleAndAbstractAndEtab(chaine, etab).toString();
        } catch (IOException e) {
            log.error(e.toString());
            return null;
        }
    }
}


