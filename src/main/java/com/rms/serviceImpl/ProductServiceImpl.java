package com.rms.serviceImpl;

import com.rms.JWT.JwtFilter;
import com.rms.constants.RestoConstants;
import com.rms.dao.ProductDao;
import com.rms.model.Category;
import com.rms.model.Product;
import com.rms.service.ProductService;
import com.rms.utils.RestoUtils;
import com.rms.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap, false)) {
                    productDao.save(getProductFromMap(requestMap, false));
                    return RestoUtils.getResponseEntity("Product was added successfully", HttpStatus.OK);
                }
                return RestoUtils.getResponseEntity(RestoConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestoUtils.getResponseEntity(RestoConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Product product = new Product();
        Category category = new Category();

        category.setId(Integer.parseInt(requestMap.get("categoryId")));
        if (isAdd) {
            product.setId(Integer.parseInt(requestMap.get("id")));
        } else {
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setPrice(Double.parseDouble(requestMap.get("price")));
        product.setDescription(requestMap.get("description"));

        return product;
    }

    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateId) {
                return true;
            } else return !validateId;
        }
        return false;
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProducts() {
        try {
            return new ResponseEntity<>(productDao.getAllProducts(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductMap(requestMap, true)) {
                    Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
                    if (!optional.isEmpty()) {
                        Product product = getProductFromMap(requestMap, true);
                        product.setStatus(optional.get().getStatus());
                        productDao.save(product);
                        return RestoUtils.getResponseEntity("Product was updated successfully", HttpStatus.OK);
                    } else {
                        return RestoUtils.getResponseEntity("Product Id doesn't exist!", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return RestoUtils.getResponseEntity(RestoConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            } else {
                return RestoUtils.getResponseEntity(RestoConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestoUtils.getResponseEntity(RestoConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Product> optional = productDao.findById(id);
                if (optional.isPresent()) {
                    productDao.deleteById(id);
                    return RestoUtils.getResponseEntity("Product was deleted successfully", HttpStatus.OK);
                } else {
                    return RestoUtils.getResponseEntity("Product Id doesn't exist!", HttpStatus.BAD_REQUEST);
                }
            } else {
                return RestoUtils.getResponseEntity(RestoConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestoUtils.getResponseEntity(RestoConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProductStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (requestMap.containsKey("id") && requestMap.containsKey("status")) {
                    Optional<Product> optional = productDao.findById(Integer.parseInt(requestMap.get("id")));
                    if (!optional.isEmpty()) {
                        productDao.updateProductStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                        return RestoUtils.getResponseEntity("Product status was updated successfully", HttpStatus.OK);
                    } else {
                        return RestoUtils.getResponseEntity("Product Id doesn't exist!", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return RestoUtils.getResponseEntity(RestoConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            } else {
                return RestoUtils.getResponseEntity(RestoConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestoUtils.getResponseEntity(RestoConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
