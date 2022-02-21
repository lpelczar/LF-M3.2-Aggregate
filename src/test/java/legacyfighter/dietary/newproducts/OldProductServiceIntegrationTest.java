package legacyfighter.dietary.newproducts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OldProductServiceIntegrationTest {

    @Autowired
    OldProductService oldProductService;

    @Autowired
    OldProductRepository oldProductRepository;

    @BeforeEach
    void setup() {
        oldProductRepository.deleteAll();
    }

    @Test
    void shouldGetDescriptions() {
        //given
        existingProduct(BigDecimal.TEN, "coffee", "coffee beans", 10);
        existingProduct(BigDecimal.ONE, "tea", "nice tea", 5);
        existingProduct(BigDecimal.ONE, "apple", "", 5);
        existingProduct(BigDecimal.ONE, "", "nice peach", 5);

        //when
        List<String> allDescriptions = oldProductService.findAllDescriptions();

        //then
        assertEquals(allDescriptions, List.of("coffee *** coffee beans", "tea *** nice tea", "", ""));
    }

    @Test
    void shouldReplaceInDescription() {
        //given
        OldProduct product = existingProduct(BigDecimal.TEN, "coffee", "coffee beans", 10);

        //when
        oldProductService.replaceCharInDesc(product.serialNumber(), 'b', 'd');

        //then
        assertEquals(oldProductService.findAllDescriptions(), List.of("coffee *** coffee deans"));
    }

    @Test
    void shouldGetCounter() {
        //given
        OldProduct product = existingProduct(BigDecimal.TEN, "coffee", "coffee beans", 10);

        //when
        int counter = oldProductService.getCounterOf(product.serialNumber());

        //then
        assertEquals(10, counter);
    }

    @Test
    void shouldIncrementCounter() {
        //given
        OldProduct product = existingProduct(BigDecimal.TEN, "coffee", "coffee beans", 10);

        //when
        oldProductService.incrementCounter(product.serialNumber());

        //then
        assertEquals(11, oldProductService.getCounterOf(product.serialNumber()));
    }

    @Test
    void shouldFailToIncrementCounter() {
        //given
        OldProduct product = existingProduct(BigDecimal.ZERO, "coffee", "coffee beans", 10);

        //expect
        assertThrows(IllegalStateException.class, () -> oldProductService.incrementCounter(product.serialNumber()));
    }

    @Test
    void shouldDecrementCounter() {
        //given
        OldProduct product = existingProduct(BigDecimal.TEN, "coffee", "coffee beans", 10);

        //when
        oldProductService.decrementCounter(product.serialNumber());

        //then
        assertEquals(9, oldProductService.getCounterOf(product.serialNumber()));
    }

    @Test
    void shouldFailToDecrementCounter() {
        //given
        OldProduct product = existingProduct(BigDecimal.ZERO, "coffee", "coffee beans", 10);

        //expect
        assertThrows(IllegalStateException.class, () -> oldProductService.decrementCounter(product.serialNumber()));
    }

    @Test
    void shouldChangePrice() {
        //given
        OldProduct product = existingProduct(BigDecimal.TEN, "coffee", "coffee beans", 10);

        //when
        oldProductService.changePriceOf(product.serialNumber(), BigDecimal.ONE);

        //then
        assertEquals(BigDecimal.ONE.stripTrailingZeros(), oldProductService.getPriceOf(product.serialNumber()).stripTrailingZeros());
    }

    @Test
    void shouldNotChangePrice() {
        //given
        OldProduct product = existingProduct(BigDecimal.TEN, "coffee", "coffee beans", 0);

        //when
        oldProductService.changePriceOf(product.serialNumber(), BigDecimal.ONE);

        //then
        assertEquals(BigDecimal.TEN.stripTrailingZeros(), oldProductService.getPriceOf(product.serialNumber()).stripTrailingZeros());
    }

    OldProduct existingProduct(BigDecimal price, String desc, String longDesc, Integer counter) {
        return oldProductRepository.save(new OldProduct(price, counter), new Description(desc, longDesc));
    }

}