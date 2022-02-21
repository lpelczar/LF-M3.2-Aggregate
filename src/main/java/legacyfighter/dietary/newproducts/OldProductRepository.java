package legacyfighter.dietary.newproducts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class OldProductRepository {

    private final OldProductEntityRepository oldProductEntityRepository;
    private final ProductDescriptionRepository productDescriptionRepository;

    public OldProductRepository(OldProductEntityRepository oldProductEntityRepository,
                                ProductDescriptionRepository productDescriptionRepository) {
        this.oldProductEntityRepository = oldProductEntityRepository;
        this.productDescriptionRepository = productDescriptionRepository;
    }

    OldProduct save(OldProduct oldProduct, Description description) {
        productDescriptionRepository.save(ProductDescription.from(description, oldProduct.serialNumber()));
        return oldProductEntityRepository.save(oldProduct);
    }

    ProductDescription getOneByProductId(UUID productId) {
        return productDescriptionRepository.getOneByProductId(productId);
    }

    public OldProduct getOne(UUID productId) {
        return oldProductEntityRepository.getOne(productId);
    }

    List<ProductDescription> findAllDescriptions() {
        return productDescriptionRepository.findAll();
    }

    public void deleteAll() {
        productDescriptionRepository.deleteAll();
        oldProductEntityRepository.deleteAll();
    }
}

interface OldProductEntityRepository extends JpaRepository<OldProduct, UUID> {
}

interface ProductDescriptionRepository extends JpaRepository<ProductDescription, UUID> {

    ProductDescription getOneByProductId(UUID productId);
}
