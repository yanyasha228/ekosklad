package com.ekoskladvalidator.RestServices;

import com.ekoskladvalidator.Models.DTO.ProductDto;
import com.ekoskladvalidator.Models.ModelIdApiKeyLine;
import com.ekoskladvalidator.Models.Product;
import com.ekoskladvalidator.Models.PromApiKey;
import com.ekoskladvalidator.ObjectMappers.ProductMapper;
import com.ekoskladvalidator.RestDao.ProductRestDao;
import com.ekoskladvalidator.Services.ProductService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductRestServiceImpl implements ProductRestService {

    private final ProductRestDao productRestDao;

    private final ProductService productService;

    private final ProductMapper productMapper;

    public ProductRestServiceImpl(ProductRestDao productRestDao, ProductService productService, ProductMapper productMapper) {
        this.productRestDao = productRestDao;
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Override
    public Optional<Product> getProductById(long id) {
        Product product = productMapper.toEntity(productRestDao.getProductById(id).orElse(null));
        return Optional.ofNullable(product);
    }

    @Override
    public Optional<Product> getProductByIdAndApiToken(long id, PromApiKey promApiKey) {
        Product product = productMapper.toEntity(productRestDao.getProductByIdAndApiToken(id, promApiKey).orElse(null));
        return Optional.ofNullable(product);
    }

    @Override
    public Optional<Product> getProductByExternalIdAndApiToken(long external_id, PromApiKey promApiKey) {

        Product product = productMapper.toEntity(productRestDao.getProductByExternalIdAndApiToken(external_id, promApiKey).orElse(null));

        return Optional.ofNullable(product);
    }

    @Override
    public List<Product> getAll() throws InterruptedException {

        List<Product> productList = new ArrayList<>();

        for (Product pr : productService.findAll()) {

            Optional<Product> prodOpt = getProduct(pr);

            prodOpt.ifPresent(productList::add);

            Thread.sleep(150);

        }

        return productList;
    }

    @Override
    public List<Product> getProducts(List<Product> prodList) throws InterruptedException {
        List<Product> productList = new ArrayList<>();

        for (Product pr : prodList) {

            Optional<Product> prodOpt = getProduct(pr);

            prodOpt.ifPresent(productList::add);

            Thread.sleep(150);

        }

        return productList;
    }

    @Override
    public Optional<Product> getProduct(Product product) {
        Optional<ModelIdApiKeyLine> modelIdApiKeyLine = product.getModelIdApiKeyLines().stream()
                .filter(modLine -> modLine.getProductApiId() == product.getId()).findFirst();

        if (modelIdApiKeyLine.isPresent()) {

            Product productT = productMapper.toEntity(productRestDao.getProductByIdAndApiToken(modelIdApiKeyLine.get().getProductApiId(), modelIdApiKeyLine.get().getPromApiKey()).orElse(null));

            return Optional.ofNullable(productT);

        }

        return Optional.empty();

    }

    @Override
    public List<Product> getProductsByGroupId(long id) {
        return productRestDao.
                getProductsByGroupId(id).stream().
                map(productDto -> productMapper.toEntity(productDto)).collect(Collectors.toList());
    }


    @Override
    public List<Product> postProducts(List<Product> productList) throws InterruptedException {

//        List<Product> productListForRet = new ArrayList<>();
//
//        List<ProductDto> productListForRetDto = new ArrayList<>();

        HashMap<PromApiKey, ArrayList<ModelIdApiKeyLine>> tokenMap = new HashMap<>();

        PromApiKey promApiKeyMain = null;

        for (Product pr : productList) {
            for (ModelIdApiKeyLine modLine : pr.getModelIdApiKeyLines()) {
                if (Objects.isNull(promApiKeyMain)) promApiKeyMain = modLine.getPromApiKey();
                if (tokenMap.containsKey(modLine.getPromApiKey())) {
//                    tokenMap.get(modLine.getPromApiKey()).add(productMapper.toDto(pr, modLine.getProductApiId()));
                    tokenMap.get(modLine.getPromApiKey()).add(modLine);
                } else {
                    ArrayList<ModelIdApiKeyLine> list = new ArrayList<>();
//                    list.add(productMapper.toDto(pr, modLine.getProductApiId()));
                    list.add(modLine);
                    tokenMap.put(modLine.getPromApiKey(), list);
                }
            }


        }

//        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
//
//        try {
//            List<List<ProductDto>> descList = getSubArraysMultiple(tokenMap.get(promApiKeyMain), 100);
//
//            String json = objectWriter.writeValueAsString(descList.get(0));
//
//            int i = 0;
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }


        for (Map.Entry<PromApiKey, ArrayList<ModelIdApiKeyLine>> ent : tokenMap.entrySet()) {
            List<ProductDto> editedDtos = new ArrayList<>();
            List<ProductDto> prodDtoList = ent.getValue().stream()
                    .map(modelIdApiKeyLine -> productMapper.toDto(modelIdApiKeyLine.getProduct(), modelIdApiKeyLine.getProductApiId()))
                    .collect(Collectors.toList());
            List<List<ProductDto>> descList = getSubArraysMultiple(prodDtoList, 100);

            for (List<ProductDto> listToPost : descList) {
                editedDtos.addAll(productRestDao.postProductsByApiToken(ent.getKey(), listToPost));
                Thread.sleep(150);
            }
        }


        return productList;
    }

    private static <T> List<List<T>> getSubArraysMultiple(List<T> list, int mult) {
        if (mult <= 0 || list.isEmpty()) return Collections.emptyList();

        List<List<T>> listToRet = new ArrayList<>();
        if (mult >= list.size()) {
            listToRet.add(list);
            return listToRet;
        }
        for (int i = 0; i <= Math.ceil(list.size() / mult); i++) {
            int topPoint = ((i * mult) + mult);
            if (topPoint < (list.size())) {
                listToRet.add(list.subList((i * mult), topPoint));
            } else {
                if ((((i - 1) * mult) + mult) == list.size()) break;
                listToRet.add(list.subList((i * mult), list.size()));
            }
        }
        return listToRet;
    }
}
