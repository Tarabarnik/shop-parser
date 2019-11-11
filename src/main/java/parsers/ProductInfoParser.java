package parsers;

import entity.Product;

import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProductInfoParser {
    private List<Product> products;
    private int triggeredHttpRequests = 0;

    public ProductInfoParser(List<Product> products) {
        this.products = products;
    }

    private ProductInfoParser() {}

    public void parse() {
        for (Product product : products)
            try {
                Document document = Jsoup.connect(product.getUrl()).get();
                triggeredHttpRequests++;
                product.setProductName(document.select("div[data-test-id=ProductName]").text());
                product.setPrice(document.select("span[data-test-id=ProductPriceFormattedBasePrice]")
                        .text());
                String articleId = product.getUrl().replaceAll("[^0-9]", "");
                product.setArticleId(Long.valueOf(articleId));
                Elements colors = document.select("span[data-test-id=ColorVariantColorInfo]");
                for (Element element : colors) {
                    product.getColors().add(element.text());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public List<Product> getProducts() {
        return products;
    }

    public int getTriggeredHttpRequests() {
        return triggeredHttpRequests;
    }
}
