package com.gogreen.ai.service;

import com.gogreen.ai.dto.request.CategoryRequestDto;
import com.gogreen.ai.dto.response.CategoryResponseDto;
import com.gogreen.ai.entity.Category;
import com.gogreen.ai.exception.APIException;
import com.gogreen.ai.mapper.CategoryMapper;
import com.gogreen.ai.repository.CategoryRepository;
import com.gogreen.ai.service.impl.CategoryServiceImpl;
import com.gogreen.ai.util.CategorySlugGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void shouldGetAllCategories() {
        Category category = new Category();
        category.setName("Indoor Plants");

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setName("Indoor Plants");

        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(categoryMapper.toResponseDtoList(List.of(category))).thenReturn(List.of(responseDto));

        List<CategoryResponseDto> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals("Indoor Plants", result.get(0).getName());
    }

    @Test
    void shouldGetCategoryById() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        category.setId(id);
        category.setName("Outdoor Plants");

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setId(id);
        responseDto.setName("Outdoor Plants");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.getCategoryById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Outdoor Plants", result.getName());
    }

    @Test
    void shouldThrowNotFoundWhenCategoryDoesNotExistById() {
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        APIException exception = assertThrows(APIException.class, () -> categoryService.getCategoryById(id));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void shouldCreateCategorySuccessfully() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Succulents");
        requestDto.setSlug("succulents");

        Category category = new Category();
        category.setName("Succulents");

        CategoryResponseDto responseDto = new CategoryResponseDto();
        responseDto.setName("Succulents");
        responseDto.setSlug("succulents");

        when(categoryRepository.existsByName("Succulents")).thenReturn(false);
        when(categoryRepository.existsBySlug("succulents")).thenReturn(false);
        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponseDto(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.createCategory(requestDto);

        assertNotNull(result);
        assertEquals("Succulents", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void shouldThrowConflictWhenCreatingDuplicateCategoryName() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Succulents");

        when(categoryRepository.existsByName("Succulents")).thenReturn(true);

        APIException exception = assertThrows(APIException.class, () -> categoryService.createCategory(requestDto));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    void shouldDeleteCategory() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        category.setId(id);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(id);

        verify(categoryRepository).delete(category);
    }
}
