package com.test.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.pojo.Product;
import com.test.pojo.Result;
import com.test.pojo.response.ResponseProduct;
import com.test.pojo.response.ResponseUser;
import com.test.service.ProductService;
import com.test.service.serviceimpl.RedisService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductCotroller {
    @Autowired
    private ProductService productService;
    @Autowired
    private RedisService redisService;

    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/all")
    public Result<?> productQueryById(@RequestParam Long productId) {
        ResponseProduct product = productService.findProductById(productId);
        if (product != null) {
            return Result.success("商品信息", product);
        } else {
            return Result.error("商品不存在");
        }
    }


    @GetMapping("/pages")
    public Result<?> selectAllProduct(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "1") int pageSize) {
        Page<ResponseProduct> responseProductIPage = productService.getProductByPage(currentPage, pageSize);
        return Result.success("商品信息",responseProductIPage);
    }

    @PostMapping("/updateProduct")
    public Result<?> editById(@RequestBody Product product, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();

                    ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
                        if (responseUser.getStatus() == 1 || responseUser.getRoleType() == 1) {
                            product.setUpdatedBy(responseUser.getUserName());
                            boolean updateStatus=productService.saveOrUpdate(product);
                            if(updateStatus){
                                return Result.success("修改成功");
                            }
                            return Result.error("商品不存在");
                        }
                        return Result.error("用户已被禁用或无管理员权限");

    }


    @DeleteMapping("/")
    public Result<?> deleteById(@RequestParam Long productId, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
                    ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
                    if (responseUser != null)
                        if (responseUser.getStatus() == 1 || responseUser.getRoleType() == 1) {
                            boolean updateStatus=productService.removeById(productId);
                            if(updateStatus){
                                return Result.success("删除成功");
                            }
                            return Result.error("商品不存在");
                        }
                        return Result.error("用户已被禁用或无管理员权限");
    }


    @PostMapping("/insertProduct")
    public Result<?> insertProduct(@RequestBody List<Product> product, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
                    ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());

                        if (responseUser.getStatus() == 1 || responseUser.getRoleType() == 1) {
                            product.forEach(productInsert -> {
                                productInsert.setCreatedBy(responseUser.getUserName());
                                productInsert.setUpdatedBy(responseUser.getUserName());
                            });
                            boolean insertStatus=productService.batchAddProduct(product);
                            if(insertStatus){
                                return Result.success("插入成功");
                            }
                        }
                        return Result.error("用户已被禁用或无管理员权限");

    }

    @PostMapping("/{productId}/uploadImage")
    public Result<?> uploadProductImage(@PathVariable Long productId, @RequestParam("file") MultipartFile file,HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
        if (responseUser != null)
            if (responseUser.getStatus() == 1 || responseUser.getRoleType() == 1) {
                try {
                    productService.uploadProductImage(file, productId);
                    return Result.success("Product image uploaded successfully");
                    } catch (Exception e) {
                    return Result.error("Failed to upload product image: " + e.getMessage());
                    }
            }
        return Result.error("用户已被禁用或无管理员权限");
    }

    @PostMapping("/{id}/comment")
    public Result<?> addProductComment(@PathVariable Long id, @RequestBody String comment) {
        Boolean status= productService.addProductComment(id, comment);
        if (!status) {
            return Result.error("商品不存在");
        }
        return Result.success("添加评论成功");
    }

    @GetMapping("/comment")
    public Result<?> getProductComment(@RequestParam Long productId) {
        Product product = productService.getById(productId);
        if ( product== null) {
            return Result.error("商品不存在");
        }
        return Result.success("获取评论成功",product.getComment());
    }

    @PostMapping("/{id}/level")
    public Result<?> addProductLevel(@PathVariable Long id, @RequestBody Double level) {
        Boolean status= productService.addProductLevel(id, level);
        if (!status) {
            return Result.error("商品不存在");
        }
        return Result.success("添加评级成功");
    }

    @GetMapping("/level")
    public Result<?> getProductLevel(@RequestParam Long productId) {
        Product product = productService.getById(productId);
        if (product == null) {
            return Result.error("商品不存在");
        }else{
            return Result.success("获取评级成功",product.getLevel());
        }
    }

    @PostMapping("/updateProductStock")
    public Result<?> UpdateProductStock(@RequestParam Long productId, HttpServletRequest request) throws JsonProcessingException {
        Cookie[] cookies = request.getCookies();
        ResponseUser responseUser = redisService.getUserByToken(cookies[0].getValue());
        if (responseUser.getStatus() == 1 || responseUser.getRoleType() == 1) {
            Product product = productService.getById(productId);
            product.setUpdatedBy(responseUser.getUserName());
            product.setStock(0);
            boolean updateStatus=productService.saveOrUpdate(product);
            if(updateStatus){
                return Result.success("修改成功");
            }
            return Result.error("商品不存在");
        }
        return Result.error("用户已被禁用或无管理员权限");
    }
}