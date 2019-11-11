import entity.Product;
import parsers.ProductInfoParser;
import parsers.ProductListParser;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class Main {

    private static int triggeredHttpRequests = 0;
    private static final String FILE_NAME = "products.xml";

    public static void main(String[] args) {
        String url = "https://www.aboutyou.de";
        String request = "/maenner/bekleidung";
        String id = "a[data-test-id=ProductTile]";

        ProductListParser listParser = new ProductListParser(url, id, request);
        listParser.parseMainPage();
        triggeredHttpRequests++;
        ProductInfoParser infoParser = new ProductInfoParser(listParser.getProducts());
        infoParser.parse();
        List<Product> products = infoParser.getProducts();
        triggeredHttpRequests += infoParser.getTriggeredHttpRequests();
        System.out.println("Triggered " + triggeredHttpRequests + " requests");
        System.out.println("Got " + products.size() + " products");
        serialize(products);
    }

    private static void serialize(List<Product> products) {
        XMLEncoder encoder = null;
        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(FILE_NAME)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (encoder != null) {
            encoder.writeObject(products);
            encoder.close();
        }
    }
}
