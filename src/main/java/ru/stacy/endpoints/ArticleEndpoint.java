package ru.stacy.endpoints;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.stacy.*;
import ru.stacy.entity.Article;
import ru.stacy.service.IArticleService;

import java.util.ArrayList;
import java.util.List;

/*
    Классы конечных точек принимают запросы веб-служб и возвращают ответы веб-служб.
    Здесь нам нужно использовать классы Java, сгенерированные XSD-файлами для запроса и ответа.

    @Endpoint: Класс становится конечной точкой веб-службы.

    @PayloadRoot: Метод становится методом конечной точки, который принимает входящий запрос и возвращает ответ. У него есть атрибуты localPartи namespace. Атрибут localPartобязателен и обозначает локальную часть корневого элемента полезной нагрузки. Атрибут namespaceявляется необязательным и обозначает пространство имен корневого элемента полезной нагрузки.

    @ResponsePayload: ограничивает ответ метода на полезную нагрузку ответа.

    URL: http://localhost:8080/soapws/articles.wsdl
 */
@Endpoint
public class ArticleEndpoint {
    private static final String NAMESPACE_URI = "http://www.concretepage.com/article-ws";

    private final IArticleService articleService;

    @Autowired
    public ArticleEndpoint(IArticleService articleService) {
        this.articleService = articleService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getArticleByIdRequest")
    @ResponsePayload
    public GetArticleByIdResponse getArticle(@RequestPayload GetArticleByIdRequest request) {
        GetArticleByIdResponse response = new GetArticleByIdResponse();
        ArticleInfo articleInfo = new ArticleInfo();

        Article article = articleService.getArticleById(request.getArticleId());
        articleInfo.setArticleId(article.getId());
        articleInfo.setTitle(article.getTitle());
        articleInfo.setCategory(article.getCategory());

        response.setArticleInfo(articleInfo);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllArticlesRequest")
    @ResponsePayload
    public GetAllArticlesResponse getAllArticles() {
        GetAllArticlesResponse response = new GetAllArticlesResponse();

        List<ArticleInfo> articleInfoList = new ArrayList<>();
        List<Article> articleList = articleService.getAllArticles();

        for (Article article : articleList) {
            ArticleInfo ob = new ArticleInfo();
            ob.setArticleId(article.getId());
            ob.setTitle(article.getTitle());
            ob.setCategory(article.getCategory());

            articleInfoList.add(ob);
        }

        response.getArticleInfo().addAll(articleInfoList);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addArticleRequest")
    @ResponsePayload
    public AddArticleResponse addArticle(@RequestPayload AddArticleRequest request) {
        AddArticleResponse response = new AddArticleResponse();
        ServiceStatus serviceStatus = new ServiceStatus();

        Article article = new Article();
        article.setTitle(request.getTitle());
        article.setCategory(request.getCategory());

        boolean flag = articleService.addArticle(article);

        if (!flag) {
            serviceStatus.setStatusCode("CONFLICT");
            serviceStatus.setMessage("Content Already Available");
            response.setServiceStatus(serviceStatus);
        } else {
            ArticleInfo articleInfo = new ArticleInfo();

            articleInfo.setArticleId(article.getId());
            articleInfo.setTitle(article.getTitle());
            articleInfo.setCategory(article.getCategory());

            response.setArticleInfo(articleInfo);

            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Added Successfully");
            response.setServiceStatus(serviceStatus);
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateArticleRequest")
    @ResponsePayload
    public UpdateArticleResponse updateArticle(@RequestPayload UpdateArticleRequest request) {
        Article article = new Article();
        BeanUtils.copyProperties(request.getArticleInfo(), article);

        articleService.updateArticle(article);
        ServiceStatus serviceStatus = new ServiceStatus();
        serviceStatus.setStatusCode("SUCCESS");
        serviceStatus.setMessage("Content Updated Successfully");

        UpdateArticleResponse response = new UpdateArticleResponse();
        response.setServiceStatus(serviceStatus);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteArticleRequest")
    @ResponsePayload
    public DeleteArticleResponse deleteArticle(@RequestPayload DeleteArticleRequest request) {
        Article article = articleService.getArticleById(request.getArticleId());
        ServiceStatus serviceStatus = new ServiceStatus();

        if (article == null) {
            serviceStatus.setStatusCode("FAIL");
            serviceStatus.setMessage("Content Not Available");
        } else {
            articleService.deleteArticle(article);
            serviceStatus.setStatusCode("SUCCESS");
            serviceStatus.setMessage("Content Deleted Successfully");
        }

        DeleteArticleResponse response = new DeleteArticleResponse();
        response.setServiceStatus(serviceStatus);

        return response;
    }
}
