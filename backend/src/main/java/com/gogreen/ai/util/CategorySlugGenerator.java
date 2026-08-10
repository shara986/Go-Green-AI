package com.gogreen.ai.util;

import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.function.Predicate;

/**
 * Utility for generating URL-friendly slugs from category names.
 *
 * <p>Slug generation rules:</p>
 * <ul>
 *   <li>Trim leading/trailing whitespace.</li>
 *   <li>Convert to lowercase.</li>
 *   <li>Replace spaces and special characters with hyphens.</li>
 *   <li>Remove duplicate consecutive hyphens.</li>
 *   <li>Remove leading/trailing hyphens.</li>
 * </ul>
 *
 * <p>This class is intentionally a stateless utility (no Spring dependency) so it
 * can be reused anywhere in the layered architecture, including unit tests.</p>
 */
public final class CategorySlugGenerator {

    private CategorySlugGenerator() {
        // prevent instantiation
    }

    /**
     * Generates a base slug from a category name without checking uniqueness.
     *
     * @param name the category name (may be null or blank)
     * @return the normalized slug, or an empty string if the input is blank
     */
    public static String generateSlug(String name) {
        if (!StringUtils.hasText(name)) {
            return "";
        }
        String slug = name.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", "-")
                .replaceAll("[\\s-]+", "-")
                .replaceAll("^-+|-+$", "");
        return slug;
    }

    /**
     * Generates a unique slug for a category name, appending a numeric suffix
     * (e.g. {@code -2}, {@code -3}) when the base slug already exists.
     *
     * @param name   the category name
     * @param exists a predicate that returns {@code true} if the given slug is
     *               already taken (should delegate to the repository)
     * @return a unique slug
     * @throws IllegalArgumentException if the name is blank (no slug can be generated)
     */
    public static String generateUniqueSlug(String name, Predicate<String> exists) {
        String base = generateSlug(name);
        if (base.isEmpty()) {
            throw new IllegalArgumentException("Cannot generate a slug from an empty category name");
        }
        if (!exists.test(base)) {
            return base;
        }
        int suffix = 2;
        String candidate;
        do {
            candidate = base + "-" + suffix;
            suffix++;
        } while (exists.test(candidate));
        return candidate;
    }
}
