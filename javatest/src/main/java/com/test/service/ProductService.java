package com.test.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.test.pojo.Product;
import com.test.pojo.response.ResponseProduct;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService extends IService<Product> {
    ResponseProduct findProductById(Long ProductId);

//    List<ResponseProduct> selectAllProduct();

    Boolean batchAddProduct(List<Product> productList);

    Page<ResponseProduct> getProductByPage(int currentPage, int pageSize);

    void uploadProductImage(MultipartFile file, Long productId) throws IOException;


    Boolean addProductComment(Long productId, String comment);

    Boolean addProductLevel(Long productId, Double level);

}
