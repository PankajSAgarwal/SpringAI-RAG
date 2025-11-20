package com.ragexample.boardgamebuddy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class GameRulesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameRulesService.class);
    private final VectorStore vectorStore;

    public GameRulesService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }
    public String getRulesFor(String gameName, String question) {
        var searchRequest = SearchRequest.builder()
                .query(question)
//                .topK(6)            // By default, the similarity search will return up to four documents that are the most similar to the question
                /**
                 * By default, the search request’s threshold is 0.0, meaning that any document, even if it is remotely similar
                 * If you find that the similarity search is returning results that are not similar enough to be useful as context for answering the questions, you can tune the similarity threshold by calling withSimilarityThreshold()
                 */
//                .similarityThreshold(0.5) //
                .filterExpression(new FilterExpressionBuilder().eq("gameTitle", normalizeGameTitle(gameName)).build())
                .build();
        LOGGER.warn("Search request: {}", searchRequest);

        var similarDocs = vectorStore.similaritySearch(searchRequest);
        if (similarDocs.isEmpty()){
            return "The rules for "+ gameName + " are not available.";
        }
        return similarDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String normalizeGameTitle(String gameTitle) {
        return gameTitle.toLowerCase().replace(" ","_");
    }
}
