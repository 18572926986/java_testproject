package com.test.service.serviceimpl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.mapper.ProductMapper;
import com.test.pojo.Category;
import com.test.pojo.Product;
import com.test.pojo.response.ResponseProduct;
import com.test.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${product.image.upload.path}")
    private String uploadPath;
    @Override
    public ResponseProduct findProductById(Long ProductId) {
        ResponseProduct product = productMapper.selectByRespId(ProductId);
        return product;
    }

//    @Override
//    public List<ResponseProduct> selectAllProduct(){
//        return productMapper.selectAllProduct();
//    }

    @Override
    public Boolean batchAddProduct(List<Product> productList) {
        return this.saveBatch(productList);
    }

    /**
     * Page<Product> page = new Page<>(1, 3);// 2表示当前页，3表示每页显示条数
     *         productMapper.selectPage(page,null);
     *         List<Product> productList = page.getRecords();
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public Page<ResponseProduct> getProductByPage(int currentPage, int pageSize) {
        Page<ResponseProduct> page = new Page<>(currentPage, pageSize);
        return productMapper.selectAllProduct(page);
    }

    @Override
    public void uploadProductImage(MultipartFile file, Long productId) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadPath, filename);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        updateProductImagePath(productId, path.toString());
    }

    @Override
    public Boolean addProductComment(Long productId, String comment) {
        Product product = productMapper.selectById(productId);
        if (product != null) {
            product.setComment(comment);
            productMapper.updateById(product);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean addProductLevel(Long productId, Double level) {
        Product product = productMapper.selectById(productId);
        if (product != null) {
            product.setLevel(level);
            productMapper.updateById(product);
            return true;
        }else {
            return false;
        }
    }

    private void updateProductImagePath(Long productId, String path) {
        Product product = productMapper.selectById(productId);
        if (product != null) {
            product.setImagePath(path);
            productMapper.updateById(product);
        }
    }

    private ResponseProduct convertToResponse(Product product) {
        ResponseProduct responseProduct = new ResponseProduct();
        BeanUtils.copyProperties(product, responseProduct);
        return responseProduct;
    }

    private ResponseProduct convertToResponse(Category category) {
        ResponseProduct responseProduct = new ResponseProduct();
        BeanUtils.copyProperties(category, responseProduct);
        return responseProduct;
    }
}
