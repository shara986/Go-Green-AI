package com.gogreen.ai.service.impl;

import com.gogreen.ai.dto.request.CategoryRequestDto;
import com.gogreen.ai.dto.response.CategoryResponseDto;
import com.gogreen.ai.entity.Category;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.CategoryMapper;
import com.gogreen.ai.repository.CategoryRepository;
import com.gogreen.ai.service.CategoryService;
import com.gogreen.ai.util.CategorySlugGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toResponseDtoList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category not found with id: " + id));
        return categoryMapper.toResponseDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category not found with slug: " + slug));
        return categoryMapper.toResponseDto(category);
    }

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        if (categoryRepository.existsByName(categoryRequestDto.getName())) {
            throw new APIException(HttpStatus.CONFLICT, "Category with name '" + categoryRequestDto.getName() + "' already exists");
        }

        Category category = categoryMapper.toEntity(categoryRequestDto);
        String uniqueSlug = CategorySlugGenerator.generateUniqueSlug(categoryRequestDto.getName(), categoryRepository::existsBySlug);
        category.setSlug(uniqueSlug);

        if (categoryRequestDto.getParentCategoryId() != null) {
            Category parent = categoryRepository.findById(categoryRequestDto.getParentCategoryId())
                    .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Parent category not found with id: " + categoryRequestDto.getParentCategoryId()));
            category.setParentCategory(parent);
        }

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponseDto(savedCategory);
    }

    @Override
    public CategoryResponseDto updateCategory(UUID id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category not found with id: " + id));

        if (!category.getName().equalsIgnoreCase(categoryRequestDto.getName()) && categoryRepository.existsByName(categoryRequestDto.getName())) {
            throw new APIException(HttpStatus.CONFLICT, "Category with name '" + categoryRequestDto.getName() + "' already exists");
        }

        boolean nameChanged = !category.getName().equalsIgnoreCase(categoryRequestDto.getName());
        category.setName(categoryRequestDto.getName());
        category.setDescription(categoryRequestDto.getDescription());
        category.setImageUrl(categoryRequestDto.getImageUrl());

        if (nameChanged) {
            String updatedSlug = CategorySlugGenerator.generateUniqueSlug(categoryRequestDto.getName(), categoryRepository::existsBySlug);
            category.setSlug(updatedSlug);
        }

        if (categoryRequestDto.getParentCategoryId() != null) {
            Category parent = categoryRepository.findById(categoryRequestDto.getParentCategoryId())
                    .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Parent category not found with id: " + categoryRequestDto.getParentCategoryId()));
            category.setParentCategory(parent);
        } else {
            category.setParentCategory(null);
        }

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponseDto(updatedCategory);
    }

    @Override
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new APIException(HttpStatus.NOT_FOUND, "Category not found with id: " + id));
        categoryRepository.delete(category);
    }
}
