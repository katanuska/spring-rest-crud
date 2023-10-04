package katarina.products.product;

import katarina.products.currency.CurrencyRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CurrencyRateService currencyConverterService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ProductService(ProductRepository productRepository, CurrencyRateService currencyConverterService) {
        this.productRepository = productRepository;
        this.currencyConverterService = currencyConverterService;
    }

    public Iterable<Product> getAll() {
        Iterable<Product> products = productRepository.findAll();
        setPriceUsd(products);
        return products;
    }

    public Product save(Product product) {
        Product savedProduct = productRepository.save(product);
        setPriceUsd(savedProduct);
        return savedProduct;
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public Optional<Product> getProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(this::setPriceUsd);
        return optionalProduct;
    }

    private void setPriceUsd(Product product) {
        setPriceUsd(List.of(product));
    }

    private void setPriceUsd(Iterable<Product> products) {
        Optional<BigDecimal> usdExchangeRate = currencyConverterService.getUsdExchangeRate();
        usdExchangeRate.ifPresentOrElse(
                (exchangeRate) ->
                        products.forEach(product -> {
                            BigDecimal productPriceUsd = exchangeRate.multiply(product.getPriceEur());
                            product.setPriceUsd(productPriceUsd);
                        }),
                () -> log.error("Couldn't load USD exchange rate.")
        );
    }
}
