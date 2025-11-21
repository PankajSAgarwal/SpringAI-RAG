# Implementing RAG using Spring AI and Qdrant vector store

- Embedding model - OpenAI
- Query model - OpenAI

## Connecting to existing docker qdrant vector store

```properties
spring.ai.vectorstore.qdrant.initialize-schema=true
spring.ai.vectorstore.qdrant.host=localhost
spring.ai.vectorstore.qdrant.port=6334
spring.ai.vectorstore.qdrant.collection-name=board-game-buddy
```