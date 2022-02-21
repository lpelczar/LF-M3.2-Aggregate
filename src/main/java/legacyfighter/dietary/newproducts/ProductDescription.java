package legacyfighter.dietary.newproducts;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
class ProductDescription {

    private ProductDescription() {}

    @Id
    private UUID id = UUID.randomUUID();

    private UUID productId;

    @Embedded
    private Description desc;

    ProductDescription(Description description, UUID productId) {
        this.desc = description;
        this.productId = productId;
    }

    static ProductDescription from(Description description, UUID productId) {
        return new ProductDescription(description, productId);
    }

    String formatDesc() {
        return desc.formatted();
    }

    void replaceCharFromDesc(char charToReplace, char replaceWith) {
        desc = desc.replace(charToReplace, replaceWith);
    }
}

@Embeddable
class Description {

    private String desc;
    private String longDesc;

    private Description() { }

    Description(String desc, String longDesc) {
        if (desc == null) {
            throw new IllegalStateException("Cannot have a null description");
        }
        if (longDesc == null) {
            throw new IllegalStateException("Cannot have null long description");
        }
        this.desc = desc;
        this.longDesc = longDesc;
    }

    String formatted() {
        if (desc.isEmpty() || longDesc.isEmpty()) {
            return "";
        }
        return desc + " *** " + longDesc;
    }

    public Description replace(char charToReplace, char replaceWith) {
        return new Description(desc.replace(charToReplace, replaceWith), longDesc.replace(charToReplace, replaceWith));
    }
}
