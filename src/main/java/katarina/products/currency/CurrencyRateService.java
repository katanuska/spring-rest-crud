package katarina.products.currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class CurrencyRateService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final DecimalFormat decimalFormat =
            (DecimalFormat) NumberFormat.getNumberInstance(Locale.of("hr", "HR"));

    private final WebClient hnbWebClient;

    public CurrencyRateService(@Value("${hnb.url}") String hnbUrl) {
        this.hnbWebClient = WebClient.builder().baseUrl(hnbUrl).build();
    }

    public Optional<BigDecimal> getUsdExchangeRate() {
        Optional<HnbExchangeRateDTO> currencyRate = loadUSDExchangeRate();
        return currencyRate.map(cr -> {
            try {
                decimalFormat.setParseBigDecimal(true);
                return (BigDecimal) decimalFormat.parse(cr.srednji_tecaj);
            } catch (ParseException e) {
                log.error("'Srednji tecaj' is not valid HR-hr locale number.");
                return null;
            }
        });
    }

    private Optional<HnbExchangeRateDTO> loadUSDExchangeRate() {
        List<HnbExchangeRateDTO> response = hnbWebClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<HnbExchangeRateDTO>>() {
                })
                .block();
        return Optional.ofNullable(response != null ? response.get(0) : null);
    }
}
