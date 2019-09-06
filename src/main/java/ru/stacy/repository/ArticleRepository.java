package ru.stacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stacy.entity.Article;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findById(long articleId);
    List<Article> findByTitleAndCategory(String title, String category);
}
