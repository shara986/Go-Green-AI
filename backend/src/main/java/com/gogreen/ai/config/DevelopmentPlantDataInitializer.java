package com.gogreen.ai.config;

import com.gogreen.ai.entity.Category;
import com.gogreen.ai.entity.Nursery;
import com.gogreen.ai.entity.Plant;
import com.gogreen.ai.entity.Role;
import com.gogreen.ai.entity.User;
import com.gogreen.ai.entity.enums.NurseryApprovalStatus;
import com.gogreen.ai.entity.enums.PlantType;
import com.gogreen.ai.entity.enums.UserApprovalStatus;
import com.gogreen.ai.entity.enums.UserRole;
import com.gogreen.ai.repository.CategoryRepository;
import com.gogreen.ai.repository.NurseryRepository;
import com.gogreen.ai.repository.PlantRepository;
import com.gogreen.ai.repository.RoleRepository;
import com.gogreen.ai.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Provides predictable plant data for local Postman testing only. Activate it
 * with the Spring {@code dev} profile; it never runs in the default profile.
 */
@Component
@Profile("dev")
public class DevelopmentPlantDataInitializer implements CommandLineRunner {

    private static final String OWNER_USERNAME = "devplantowner";
    private static final String OWNER_EMAIL = "devplantowner@gogreen.ai";
    private static final String CATEGORY_NAME = "Development Test Plants";
    private static final String PLANT_SKU = "DEV-MONSTERA-001";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final NurseryRepository nurseryRepository;
    private final CategoryRepository categoryRepository;
    private final PlantRepository plantRepository;
    private final PasswordEncoder passwordEncoder;

    public DevelopmentPlantDataInitializer(UserRepository userRepository,
                                           RoleRepository roleRepository,
                                           NurseryRepository nurseryRepository,
                                           CategoryRepository categoryRepository,
                                           PlantRepository plantRepository,
                                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.nurseryRepository = nurseryRepository;
        this.categoryRepository = categoryRepository;
        this.plantRepository = plantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        User owner = userRepository.findByUsername(OWNER_USERNAME).orElseGet(this::createOwner);
        Nursery nursery = nurseryRepository.findByUserId(owner.getId()).orElseGet(() -> createNursery(owner));
        Category category = categoryRepository.findByName(CATEGORY_NAME).orElseGet(this::createCategory);

        if (!plantRepository.existsByNurseryIdAndSku(nursery.getId(), PLANT_SKU)) {
            Plant plant = new Plant();
            plant.setNursery(nursery);
            plant.setCategory(category);
            plant.setName("Development Monstera");
            plant.setScientificName("Monstera deliciosa");
            plant.setSku(PLANT_SKU);
            plant.setDescription("Safe sample plant for local Admin API testing.");
            plant.setCareInstructions("Keep in bright, indirect light and water when the top soil is dry.");
            plant.setPrice(799.00);
            plant.setStock(12);
            plant.setPlantType(PlantType.INDOOR);
            plant.setImageUrl("https://example.com/dev-monstera.jpg");
            plant.setActive(true);
            plantRepository.save(plant);
        }
    }

    private User createOwner() {
        Role ownerRole = roleRepository.findByName(UserRole.ROLE_NURSERY_OWNER)
                .orElseGet(() -> roleRepository.save(new Role(UserRole.ROLE_NURSERY_OWNER)));

        User owner = new User();
        owner.setName("Development Plant Owner");
        owner.setUsername(OWNER_USERNAME);
        owner.setEmail(OWNER_EMAIL);
        owner.setPassword(passwordEncoder.encode("Password123!"));
        owner.setPhoneNumber("+91 00000 00001");
        owner.setEnabled(true);
        owner.setDeleted(false);
        owner.setApprovalStatus(UserApprovalStatus.APPROVED);
        owner.setRoles(Set.of(ownerRole));
        return userRepository.save(owner);
    }

    private Nursery createNursery(User owner) {
        Nursery nursery = new Nursery();
        nursery.setUser(owner);
        nursery.setName("Development Plant Nursery");
        nursery.setDescription("Local development seed nursery.");
        nursery.setAddress("1 Development Lane");
        nursery.setCity("Development City");
        nursery.setPostalCode("000000");
        nursery.setContactEmail(OWNER_EMAIL);
        nursery.setContactPhone("+91 00000 00001");
        nursery.setRating(5.0);
        nursery.setVerified(true);
        nursery.setApprovalStatus(NurseryApprovalStatus.APPROVED);
        return nurseryRepository.save(nursery);
    }

    private Category createCategory() {
        Category category = new Category();
        category.setName(CATEGORY_NAME);
        category.setSlug("development-test-plants");
        category.setDescription("Local development seed category.");
        category.setIcon("leaf");
        category.setActive(true);
        return categoryRepository.save(category);
    }
}
