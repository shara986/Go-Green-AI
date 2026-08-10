# API Notes

## Swagger UI
- Open http://localhost:8080/swagger-ui/index.html after starting the backend.
- The auth and admin endpoints are grouped under Authentication and Admin.

## Postman

### Files
- **Collection:** [GoGreen-AI-Postman-Collection.json](GoGreen-AI-Postman-Collection.json)
- **Environment:** [GoGreen-AI-Postman-Environment.json](GoGreen-AI-Postman-Environment.json)

### Importing
1. Open Postman.
2. **Import** → drag & drop (or browse to) `GoGreen-AI-Postman-Collection.json`.
3. **Import** → drag & drop (or browse to) `GoGreen-AI-Postman-Environment.json`.
4. In the top-right environment selector, choose **GoGreen AI - Local**.

### Environment Variables
| Variable | Purpose |
|----------|---------|
| `baseUrl` | Default `http://localhost:8080` |
| `adminToken` | Populated automatically after admin login |
| `customerToken` | Populated automatically after customer login |
| `nurseryOwnerToken` | Populated automatically after nursery owner login |
| `expertToken` | Populated automatically after expert login |
| `deliveryPartnerToken` | Populated automatically after delivery partner login |
| `userId`, `nurseryId`, `expertId`, `categoryId`, `plantId`, `orderId`, `paymentId`, `reviewId`, `announcementId`, `deliveryPartnerId` | Set manually from prior responses |

### Authentication Flow
1. Run `Authentication → Register ...` requests to create accounts, or use existing accounts.
2. Run the matching `Login (...)` request — the test script automatically stores the returned `accessToken` into the corresponding token variable.
3. Protected requests use `Authorization: Bearer {{tokenVariable}}`.

### Running Admin API Tests
1. Ensure the backend is running (`mvn spring-boot:run`).
2. Run `Authentication → Login (Admin)` to populate `adminToken`.
3. Open **Admin** folder and run any request (e.g. `Admin Dashboard → Get Admin Dashboard`).
4. For security tests, open **Admin → Security Tests**:
   - `Dashboard Without Token` → expect **401**
   - `Dashboard With Customer Token` → expect **403**
   - `Dashboard With Admin Token` → expect **200**
   - `Invalid JWT Token` → expect **401**
   - `Non-Existing User` → expect **404**
   - `Duplicate Category` → expect **400**
5. For create APIs, verify expected status (e.g. Create Category → **201**).

### Sample Test Data
- Customer: `Asha Kumar` / `ashakumar` / `asha@example.com`
- Nursery Owner: `Green Valley Nursery` / `greenvalley` / `owner@greenvalley.com`
- Expert: `Dr. Ravi Sharma` / `ravisharma` / `ravi.expert@example.com`
- Delivery Partner: `Quick Green Delivery` / `quickdelivery` / `delivery@quickgreen.com`
- Admin login: `admin` / `Admin@123` (create an admin account in the DB first if not seeded)

> Note: The collection only includes modules that currently exist in the backend (Authentication, User profile, and Admin). Modules not yet implemented (Nursery Owner CRUD, Customer marketplace, Cart, Orders, Payments, Gardening Expert, Delivery Partner, Plant Diary, AI detection, Weather, standalone Notifications/Analytics) are intentionally excluded to match the backend exactly.
