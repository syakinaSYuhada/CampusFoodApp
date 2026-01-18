# CampusFoodApp - Database Schema

## Overview
Local SQLite database (`campus_food_database`) with 2 tables for menu items and orders.

**Database Location:** `/data/data/com.campus.foodorder/databases/campus_food_database`

## Tables

### 1. `menu_items` Table
Stores available food items offered by vendors.

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| `id` | INTEGER | PRIMARY KEY, AUTO_INCREMENT | Unique menu item ID |
| `vendorId` | INTEGER | NOT NULL | Vendor who owns this item |
| `name` | TEXT | NOT NULL | Item name (e.g., "Nasi Lemak") |
| `description` | TEXT | | Detailed description |
| `price` | REAL | NOT NULL | Price in RM |
| `category` | TEXT | | Food category (e.g., "Rice", "Noodles") |
| `imageUrl` | TEXT | | URL/path to image |
| `isAvailable` | INTEGER | DEFAULT 1 | 1=available, 0=unavailable |
| `preparationTime` | INTEGER | | Minutes to prepare |

**Example Data:**
```sql
INSERT INTO menu_items VALUES
(1, 1, 'Nasi Lemak', 'Aromatic coconut rice with sambal', 4.50, 'Rice', '', 1, 15),
(2, 1, 'Roti Canai', 'Crispy Indian flat bread', 2.00, 'Bread', '', 1, 10),
(3, 2, 'Char Kuey Teow', 'Stir-fried noodles with seafood', 6.00, 'Noodles', '', 1, 12);
```

### 2. `orders` Table
Tracks student orders and their status throughout the workflow.

| Column | Type | Constraints | Description |
|--------|------|-----------|-------------|
| `id` | INTEGER | PRIMARY KEY, AUTO_INCREMENT | Unique order ID |
| `menuItemId` | INTEGER | NOT NULL | FK to menu_items.id |
| `studentId` | TEXT | NOT NULL | Student identifier (email/ID) |
| `vendorId` | INTEGER | NOT NULL | Vendor who will fulfill |
| `quantity` | INTEGER | NOT NULL | Quantity ordered |
| `totalPrice` | REAL | NOT NULL | quantity  item_price |
| `status` | TEXT | DEFAULT 'PENDING' | PENDING, ACCEPTED, REJECTED, COMPLETED |
| `notes` | TEXT | | Special instructions |
| `createdAt` | INTEGER | NOT NULL | Timestamp (milliseconds) |
| `completedAt` | INTEGER | | When order was completed |

**Example Data:**
```sql
INSERT INTO orders VALUES
(1, 1, 'student@utem.edu.my', 1, 2, 9.00, 'PENDING', 'Extra sambal', 1705564800000, NULL),
(2, 2, 'student@utem.edu.my', 1, 1, 2.00, 'ACCEPTED', '', 1705564900000, NULL),
(3, 3, 'student2@utem.edu.my', 2, 1, 6.00, 'COMPLETED', 'No spicy', 1705564700000, 1705565400000);
```

## Order Workflow

```
Student                          Database                        Vendor
                                                                
   Create Order  INSERT orders                
    (status=PENDING)                 (studentId, menuItemId)    
                                                                
                                    Query PENDING 
                                     WHERE vendorId=X           
                                                                
                                                           Accept/Reject
                                                                
     UPDATE status=ACCEPTED  UPDATE orders 
    (OR status=REJECTED)             WHERE id=X                 
                                                                
   View order status via StateFlow (Real-time updates)
```

## Key Relationships

### MenuItem  Order
- **1 MenuItem : Many Orders** (one menu item can be ordered multiple times)
- Foreign Key: `orders.menuItemId`  `menu_items.id`

### Vendor  MenuItem  Order
- Vendor owns multiple menu items
- When order is created, system queries `orders WHERE vendorId = item.vendorId`

### Student  Order
- One student can have multiple orders
- Query: `SELECT * FROM orders WHERE studentId = 'student@email.com'`

## Queries Used in App

### StudentDashboardFragment - Browse Menu
```kotlin
// MenuItemDao
@Query("SELECT * FROM menu_items")
fun getAllMenuItems(): Flow<List<MenuItem>>
```

### Student - Create Order
```kotlin
// OrderRepository
suspend fun createOrder(order: Order) {
    // INSERT INTO orders VALUES(...)
    orderDao.insert(order)
}
```

### Student - View Own Orders
```kotlin
// OrderViewModel
fun getStudentOrders(studentId: String): StateFlow<List<Order>> =
    orderRepository.getStudentOrders(studentId)
    // SELECT * FROM orders WHERE studentId = ? ORDER BY createdAt DESC
```

### Vendor - View Pending Orders
```kotlin
// VendorDashboardFragment
fun getVendorOrders(vendorId: Int) {
    // SELECT * FROM orders WHERE vendorId = ? ORDER BY createdAt DESC
    val pendingOrders = orderViewModel.getVendorOrders(vendorId)
}
```

### Vendor - Accept Order
```kotlin
// OrderRepository
suspend fun acceptOrder(orderId: Int) {
    // UPDATE orders SET status = 'ACCEPTED' WHERE id = ?
    orderDao.updateOrderStatus(orderId, OrderStatus.ACCEPTED)
}
```

### Real-Time Updates
All queries return `Flow<T>` which means:
- Student updates  Vendor sees changes instantly via StateFlow
- Vendor accepts order  Student sees status change immediately
- **No polling needed** - Room automatically notifies observers

## Data Storage Location

**Development (Emulator/Device):**
```
/data/data/com.campus.foodorder/databases/campus_food_database
```

**To inspect database:**
```bash
# Via Android Studio Device File Explorer
1. View  Tool Windows  Device File Explorer
2. Navigate to: data/data/com.campus.foodorder/databases/
3. Pull file to local machine
4. Open with SQLite Browser

# Or via adb:
adb shell
sqlite3 /data/data/com.campus.foodorder/databases/campus_food_database
.tables
.schema orders
SELECT * FROM orders;
```

## Migration (Room Schema Evolution)

Currently at **Version 2**:
- v1: MenuItem only
- v2: Added Order table

If schema changes (e.g., add new column):
1. Update entity class
2. Increment version number in `@Database(version = 3)`
3. Room will handle migration automatically (destructive, drops old data)

```kotlin
@Database(entities = [MenuItem::class, Order::class], version = 2)
```

---

**Note:** This is a local-first architecture. All data lives on device. For production, would integrate with backend API (Firebase, REST server, GraphQL) to sync across devices.
