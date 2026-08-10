# Category Slug Fix - Task Steps

## Steps
- [x] Analyze existing implementation (Category, AdminServiceImpl, CategoryRepository, CategorySlugGenerator, DTO, Mapper, Controller, tests)
- [x] Implement `CategorySlugGenerator` utility (generateSlug + generateUniqueSlug)
- [x] Update `AdminServiceImpl.createCategory()` to generate & set slug
- [x] Update `AdminServiceImpl.updateCategory()` to regenerate slug when name changes
- [x] Verify `CategoryRepository` already has `existsBySlug` (confirmed present)
- [x] Compile / run build to validate (BUILD SUCCESS)
