package com.test.service.serviceimpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.mapper.CategoryMapper;
import com.test.pojo.Category;
import com.test.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Override
    public Boolean batchAddCategory(List<Category> categoryList) {
        return this.saveBatch(categoryList);
    }
}
