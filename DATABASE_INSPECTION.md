# Database Inspection Guide

## View Database in Android Studio
1. Run your app on emulator/device
2. Go to: **View → Tool Windows → App Inspection**
3. Select **Database Inspector** tab
4. You'll see `campus_food_database` with tables:
   - `menu_items` - Food items with vendorId
   - `orders` - Orders with studentId, vendorId, status

## Command Line Inspection

### Check Orders
```bash
adb shell "run-as com.campus.foodorder sqlite3 /data/data/com.campus.foodorder/databases/campus_food_database \"SELECT id, menuItemId, studentId, vendorId, quantity, status FROM orders;\""
```

### Check Menu Items
```bash
adb shell "run-as com.campus.foodorder sqlite3 /data/data/com.campus.foodorder/databases/campus_food_database \"SELECT id, name, vendorId FROM menu_items;\""
```

### Clear Database (start fresh)
```bash
adb shell "run-as com.campus.foodorder rm /data/data/com.campus.foodorder/databases/campus_food_database"
```

## Current Setup
- **Sample menu items**: vendorId = 1 (Nasi Lemak, Roti Canai) and vendorId = 2 (Char Kuey Teow)
- **VendorOrdersFragment**: Queries for vendorId = 1
- **StudentId**: "student@campus.edu"

## Troubleshooting

### Issue: Vendor sees no orders
**Check:**
1. Did you create an order as student first?
2. Is the order's vendorId = 1? (Orders from Nasi Lemak or Roti Canai)
3. Run: `adb shell "run-as com.campus.foodorder sqlite3 /data/data/com.campus.foodorder/databases/campus_food_database \"SELECT vendorId, status, COUNT(*) FROM orders GROUP BY vendorId, status;\""

### Issue: Database locked
```bash
# Kill the app
adb shell am force-stop com.campus.foodorder
```

## Using DB Browser for SQLite (GUI)
1. Download from: https://sqlitebrowser.org/
2. Pull database:
   ```bash
   adb pull /data/data/com.campus.foodorder/databases/campus_food_database C:\temp\
   ```
3. Open the .db file in DB Browser
