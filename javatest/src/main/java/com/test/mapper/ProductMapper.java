package com.test.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.test.pojo.Product;
import com.test.pojo.response.ResponseProduct;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMapper extends BaseMapper<Product> {

//    @Select("SELECT * FROM product WHERE category_id in (SELECT category_id FROM category)")

    @Select("SELECT product.price, product.`name` as product_name,  product.desc , category.category_name FROM  product JOIN category ON product.category_id = category.category_id ")
    Page<ResponseProduct> selectAllProduct(Page<ResponseProduct> page);

    @Select("SELECT product.price, product.`name` as product_name, product.desc , category.category_name FROM  product JOIN category ON product.category_id = category.category_id WHERE id = #{ProductId}")
    ResponseProduct selectByRespId(Long ProductId);

    @Select("SELECT product.price, product.`name` as product_name, product.desc , category.category_name FROM  product JOIN category ON product.category_id = category.category_id WHERE id = #{ProductId}")
    Product findProductById(Long ProductId);

}
