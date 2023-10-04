package katarina.products.product;

import katarina.products.ProductsApplicationTests;
import katarina.products.currency.CurrencyRateService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

public class ProductControllerIntegrationTests extends ProductsApplicationTests {
    public static MockWebServer hnbWebServer;
    String hnbCorrectResponse = "[{" +
            "\"broj_tecajnice\":\"194\"," +
            "\"datum_primjene\":\"2023-10-03\"," +
            "\"drzava\":\"SAD\",\"drzava_iso\":\"USA\"," +
            "\"sifra_valute\":\"840\",\"valuta\":\"USD\"," +
            "\"kupovni_tecaj\":\"1,0546\"," +
            "\"srednji_tecaj\":\"1,0530\"," +
            "\"prodajni_tecaj\":\"1,0514\"}]";
    String hnbIncorrectResponse = "[{\"srednji_tecaj\":\"incorrect\"}]";
    @MockBean
    private ProductRepository productRepository;
    private WebTestClient client;

    @BeforeAll
    static void setUp() throws IOException {
        hnbWebServer = new MockWebServer();
        hnbWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        hnbWebServer.shutdown();
    }

    @BeforeEach
    public void setup() {
        String url = String.format("http://localhost:%s", hnbWebServer.getPort());
        ProductService service = new ProductService(productRepository, new CurrencyRateService(url));
        ProductController controller = new ProductController(service);
        client = MockMvcWebTestClient.bindToController(controller).build();
    }

    @Test
    void shouldNotReturnPriceUsdForInvalidHnbResponse() throws Exception {
        Product savedProduct = new Product(1L, "code", "Prod name", BigDecimal.ONE, "", false);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(savedProduct));

        hnbWebServer.enqueue(new MockResponse()
                .setBody(hnbIncorrectResponse)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        );

        client.get().uri("/products/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .consumeWith(productEntityExchangeResult -> {
                    Product product = productEntityExchangeResult.getResponseBody();
                    Assertions.assertNotNull(product);
                    Assertions.assertNull(product.getPriceUsd());
                });
    }

    @Test
    void shouldNotReturnPriceUsdForEmptyHnbResponse() throws Exception {
        Product savedProduct = new Product(1L, "code", "Prod name", BigDecimal.ONE, "", false);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(savedProduct));

        hnbWebServer.enqueue(new MockResponse()
                .setBody("")
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        );

        client.get().uri("/products/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .consumeWith(productEntityExchangeResult -> {
                    Product product = productEntityExchangeResult.getResponseBody();
                    Assertions.assertNotNull(product);
                    Assertions.assertNull(product.getPriceUsd());
                });

    }

    @Test
    void shouldReturnPriceUsd() throws Exception {
        Product savedProduct = new Product(1L, "code", "Prod name", BigDecimal.ONE, "", false);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(savedProduct));

        hnbWebServer.enqueue(new MockResponse()
                .setBody(hnbCorrectResponse)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
        );

        client.get().uri("/products/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Product.class)
                .consumeWith(productEntityExchangeResult -> {
                    Product product = productEntityExchangeResult.getResponseBody();
                    Assertions.assertNotNull(product);
                    Assertions.assertEquals(new BigDecimal("1.0530"), product.getPriceUsd());
                });

    }
}
