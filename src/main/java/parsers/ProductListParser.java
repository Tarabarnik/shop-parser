package parsers;

import entity.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ProductListParser {
    private String mainPageUrl;
    private String hrefBoxId;
    private String request;

    private List<Product> products = new ArrayList<>();

    public ProductListParser(String url, String id, String request)
    {
        this.mainPageUrl = url;
        this.hrefBoxId = id;
        this.request = request;
    }

    private ProductListParser() {}


    public void parseMainPage(){
        Document document = null;
        try {
            document = Jsoup.connect(mainPageUrl + request).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements links = null;
        if (document != null) {
            links = document.select("a[data-test-id=ProductTile]");
        }
        for (Element link : links) {
            Product product = new Product();
            product.setUrl(mainPageUrl + link.attr("href"));
            product.setBrand(link.select("p[data-test-id=BrandName]").text());
            products.add(product);
        }
    }

    public String getMainPageUrl() {
        return mainPageUrl;
    }

    public String getHrefBoxId() {
        return hrefBoxId;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public List<Product> getProducts() {
        return products;
    }
}
