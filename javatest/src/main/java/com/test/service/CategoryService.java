package com.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.test.pojo.Category;
import com.test.pojo.Product;

import java.util.List;

public interface CategoryService extends IService<Category> {
    Boolean batchAddCategory(List<Category> categoryList);
}
