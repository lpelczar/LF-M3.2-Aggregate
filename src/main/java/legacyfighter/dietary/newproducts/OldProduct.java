package legacyfighter.dietary.newproducts;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class OldProduct {

    @Id
    private UUID serialNumber = UUID.randomUUID();

    @Embedded
    private Price price;

    @Embedded
    private Counter counter;

    public OldProduct() {}

    public OldProduct(BigDecimal price, Integer counter) {
        this.price = Price.of(price);
        this.counter = new Counter(counter);
    }

    void decrementCounter() {
        if (price.isNotZero()) {
            this.counter = counter.decrement();
        } else {
            throw new IllegalStateException("price is zero");
        }
    }

    void incrementCounter() {
        if (price.isNotZero()) {
            this.counter = counter.increment();
        } else {
            throw new IllegalStateException("price is zero");
        }
    }

    void changePriceTo(BigDecimal price) {
        if (counter.hasAny()) {
            this.price = Price.of(price);
        }
    }

    BigDecimal getPrice() {
        return price.getAsBigDecimal();
    }

    int getCounter() {
        return counter.getIntValue();
    }

    public UUID serialNumber() {
        return serialNumber;
    }
}

@Embeddable
class Price {


    static Price of(BigDecimal value) {
        return new Price(value);
    }


    private  BigDecimal price;

    private Price() { }

    private Price(BigDecimal price) {
        if (price == null || price.signum() < 0) {
            throw new IllegalStateException("Cannot have negative price: " + price);
        }
        this.price = price;
    }


    boolean isNotZero() {
        return price.signum() != 0;
    }

    BigDecimal getAsBigDecimal() {
        return price;
    }
}

@Embeddable
class Counter {

    static Counter zero() {
        return new Counter(0);
    }

    private Counter() {
    }

    private int counter;

    Counter(int counter) {
        if (counter < 0) {
            throw new IllegalStateException("Cannot have negative counter: " + counter);
        }
        this.counter = counter;
    }

    int getIntValue() {
        return counter;
    }

    Counter increment() {
        return new Counter(counter + 1);
    }

    Counter decrement() {
        return new Counter(counter - 1);
    }

    boolean hasAny() {
        return counter > 0;
    }
}