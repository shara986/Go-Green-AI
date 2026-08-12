package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.CategoryRequestDto;
import com.gogreen.ai.dto.response.CategoryResponseDto;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<CategoryResponseDto> getAllCategories();

    CategoryResponseDto getCategoryById(UUID id);

    CategoryResponseDto getCategoryBySlug(String slug);

    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto updateCategory(UUID id, CategoryRequestDto categoryRequestDto);

    void deleteCategory(UUID id);
}
