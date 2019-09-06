package ru.stacy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stacy.entity.Article;
import ru.stacy.repository.ArticleRepository;

import java.util.List;

@Service
@Transactional
public class ArticleService implements IArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @Override
    public Article getArticleById(long articleId) {
        return articleRepository.getOne(articleId);
    }

    @Override
    public boolean addArticle(Article article) {
        List<Article> articles = articleRepository.findByTitleAndCategory(article.getTitle(), article.getCategory());

        if(articles.size() > 0) {
            return false;
        } else {
            articleRepository.save(article);
            return true;
        }
    }

    @Override
    public void updateArticle(Article article) {
        articleRepository.save(article);
    }

    @Override
    public void deleteArticle(Article article) {
        articleRepository.delete(article);
    }
}
