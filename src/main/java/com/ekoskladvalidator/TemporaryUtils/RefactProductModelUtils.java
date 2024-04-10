package com.ekoskladvalidator.TemporaryUtils;

import com.ekoskladvalidator.CustomExceptions.ImpossibleEntitySaveUpdateException;
import com.ekoskladvalidator.Models.DTO.ProductDto;
import com.ekoskladvalidator.Models.ModelIdApiKeyLine;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.Models.PromApiKey;
import com.ekoskladvalidator.RestDao.ProductRestDao;
import com.ekoskladvalidator.RestServices.ProductRestService;
import com.ekoskladvalidator.Services.ModelIdApiKeyLineService;
import com.ekoskladvalidator.Services.ProductService;
import com.ekoskladvalidator.Services.PromApiKeyService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RefactProductModelUtils {

    private final ProductService productService;

    private final PromApiKeyService promApiKeyService;

    private final ModelIdApiKeyLineService modelIdApiKeyLineService;

    private final ProductRestDao productRestDao;

    private final ProductRestService productRestService;

    public RefactProductModelUtils(ProductService productService, PromApiKeyService promApiKeyService, ModelIdApiKeyLineService modelIdApiKeyLineService, ProductRestDao productRestDao, ProductRestService productRestService) {
        this.productService = productService;
        this.promApiKeyService = promApiKeyService;
        this.modelIdApiKeyLineService = modelIdApiKeyLineService;
        this.productRestDao = productRestDao;
        this.productRestService = productRestService;
    }

    @Transactional
    public void setEkoskladApiKey() throws ImpossibleEntitySaveUpdateException {

        Optional<PromApiKey> promApiKeyOpt = promApiKeyService.findById(1L);
        List<Product> productListForSave = new ArrayList<>();

        if (promApiKeyOpt.isPresent()) {
            PromApiKey promApiKey = promApiKeyOpt.get();

            List<Product> productList = productService.findAll();

            for (Product prod :
                    productList) {

                if (modelIdApiKeyLineService.findByProductIdAndPromApiKey(prod.getId(), promApiKey).isEmpty()) {

                    ModelIdApiKeyLine modelIdApiKeyLine = new ModelIdApiKeyLine();
                    modelIdApiKeyLine.setProduct(prod);
                    modelIdApiKeyLine.setPromApiKey(promApiKey);
                    modelIdApiKeyLine.setProductApiId(prod.getId());
                    ModelIdApiKeyLine modelIdApiKeyLinePersistent = modelIdApiKeyLineService.save(modelIdApiKeyLine);
                    prod.getModelIdApiKeyLines().add(modelIdApiKeyLinePersistent);

                    productListForSave.add(prod);
                }
            }
            List<Product> savedProducts = productService.save(productListForSave);


            int i = 0;
        }


    }

    @Transactional
    public void tieRelatedProducts() {

        Optional<PromApiKey> promApiKeyOpt = promApiKeyService.findById(2L);

        List<Product> productList = productService.findAll();
        List<Product> productListForSave = new ArrayList<>();

        if (promApiKeyOpt.isPresent()) {
            for (Product prod : productList) {
                Optional<ProductDto> exProdOpt = productRestDao.getProductByExternalIdAndApiToken(prod.getId(), promApiKeyOpt.get());
                if (exProdOpt.isPresent()) {
                    ModelIdApiKeyLine modelIdApiKeyLine = new ModelIdApiKeyLine();
                    modelIdApiKeyLine.setProduct(prod);
                    modelIdApiKeyLine.setPromApiKey(promApiKeyOpt.get());
                    modelIdApiKeyLine.setProductApiId(exProdOpt.get().getId());
                    ModelIdApiKeyLine modelIdApiKeyLinePersistent = modelIdApiKeyLineService.save(modelIdApiKeyLine);
                    prod.getModelIdApiKeyLines().add(modelIdApiKeyLinePersistent);

                    productListForSave.add(prod);
                }
            }
            List<Product> savedProducts = productService.save(productListForSave);
        }
    }
}
