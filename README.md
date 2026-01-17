# CampusFoodApp

A modern Android food ordering application built with Kotlin, demonstrating best practices in Android development including MVVM architecture, Room database, Kotlin Coroutines, and Jetpack Navigation.

## Project Overview

**CampusFoodApp** is an educational project showing a complete food ordering system with two user roles (Student & Vendor) and real-time notifications.

### Features Implemented

#### Phase 1: Project Setup & UI
-  Gradle configuration with AndroidX
-  Material Design 3 theming
-  Login UI with role selection (Student/Vendor)
-  AppCompat and constraint layouts

#### Phase 2: Data Layer & MVVM Architecture
-  Room database (MenuItem entity, MenuItemDao)
-  Repository pattern for data abstraction
-  ViewModel with StateFlow for reactive updates
-  RecyclerView for displaying menu items
-  Kotlin Coroutines with proper threading
  - DAO: Blocking functions (Kapt-compatible)
  - Repository: `withContext(Dispatchers.IO)` wraps database calls
  - ViewModel: Clean `viewModelScope.launch` without explicit dispatcher
  - This ensures clean separation of concerns and automatic cancellation

#### Phase 3: Notifications & Background Tasks
-  NotificationHelper with channel management
-  OrderNotificationReceiver (BroadcastReceiver)
-  Runtime permission handling (Android 13+)
-  High-priority notifications with vibration/lights
-  Notification on menu load

#### Phase 4: Modern Navigation Architecture
-  Single Activity + Fragments (Jetpack Navigation)
-  NavController with proper back stack management
-  Material Toolbar with visible back button
-  Fragment lifecycle-aware coroutines
-  AppBarConfiguration for automatic destination routing

## Project Structure

```
app/
 src/main/
    java/com/campus/foodorder/
       MainActivity.kt                    # Single activity host
       ui/
          auth/LoginFragment.kt         # Login with role selection
          student/
             StudentDashboardFragment.kt # Menu browsing
          vendor/
              VendorDashboardFragment.kt  # Vendor dashboard (stub)
       data/
          database/
             AppDatabase.kt            # Room database singleton
             MenuItemDao.kt            # Data access operations
          model/
              MenuItem.kt               # Room entity
       repository/
          MenuRepository.kt             # Data abstraction with threading
       viewmodel/
          MenuViewModel.kt              # State management
       adapter/
          MenuItemAdapter.kt            # RecyclerView adapter
       utils/
          NotificationHelper.kt         # Notification management
       receiver/
           OrderNotificationReceiver.kt  # Background notifications
    res/
       layout/
          activity_main.xml             # Material Toolbar + NavHost
          fragment_login.xml            # Login UI
          fragment_student_dashboard.xml # RecyclerView layout
          fragment_vendor_dashboard.xml  # Vendor stub
          item_menu.xml                 # Menu item row
       navigation/
          nav_graph.xml                 # Navigation destinations
       values/
          colors.xml
          strings.xml
          themes.xml
       drawable/
           ic_launcher_*.xml
    AndroidManifest.xml                   # Permissions, activities, receiver
 build.gradle.kts                          # Dependencies & build config
 settings.gradle.kts
 gradle.properties
```

## Architecture Pattern: MVVM + Repository

```

              Fragment (UI Layer)                     
   Observes StateFlow<List<MenuItem>>             
   Updates RecyclerView                           
   Uses lifecycleScope.launch for coroutines      

                         
                         

              ViewModel (Logic Layer)                 
   MenuViewModel extends AndroidViewModel          
   viewModelScope.launch { }                       
   Delegates to Repository                         

                         
                         

           Repository (Data Layer)                    
   withContext(Dispatchers.IO) { }                
   Calls MenuItemDao (non-suspend)                
   Ensures all DB ops run on IO thread            

                         
                         

           Room Database (Persistence)                
   MenuItemDao (interface with @Query, @Insert)   
   MenuItem (Entity)                              
   SQLite backend                                 

```

## Threading Model: Clean Coroutines

**Problem we solved:** Database operations were crashing on main thread.

**Solution:** Structured concurrency with proper dispatcher scoping:

```kotlin
// Activity/Fragment - Uses lifecycle scope
lifecycleScope.launch {
    viewModel.insertMenuItem(item)  // No explicit dispatcher needed
}

// ViewModel - Uses viewModel scope
fun insertMenuItem(item: MenuItem) = viewModelScope.launch {
    repository.insertMenuItem(item)  // Delegates to Repository
}

// Repository - Handles threading
suspend fun insertMenuItem(item: MenuItem) = withContext(Dispatchers.IO) {
    menuItemDao.insert(item)  // Blocking call safe on IO thread
}

// DAO - Simple blocking calls
fun insert(item: MenuItem)  // Room handles thread context
```

**Benefits:**
-  Database never touches main thread
-  Automatic cancellation when scope ends (no memory leaks)
-  Single source of truth for threading (Repository)
-  ViewModel stays clean and testable

## Setup & Build Instructions

### Prerequisites
- **Android Studio** (Giraffe or later)
- **Gradle** 8.13+ (included in Android Studio)
- **JDK** 17+
- **Android SDK** API 34 (compileSdk)
- **Emulator or physical device** with Android 8.0+ (minSdk 26)

### Build from Source

1. **Clone the repository:**
   ```bash
   git clone https://github.com/syakinaSYuhada/CampusFoodApp.git
   cd CampusFoodApp
   ```

2. **Open in Android Studio:**
   - File  Open  Select CampusFoodApp folder
   - Android Studio will automatically sync Gradle

3. **Build APK:**
   ```bash
   ./gradlew :app:assembleDebug  # Debug APK
   # or
   ./gradlew :app:assembleDebug --info  # Verbose output
   ```

4. **Run on emulator/device:**
   ```bash
   ./gradlew :app:installDebug  # Install debug APK
   ```

5. **Or use Android Studio:**
   - Click green  (Run) button
   - Select emulator or device

### Important Gradle Properties

In `gradle.properties`:
```properties
android.useAndroidX=true
android.enableJetifier=true
org.gradle.jvmargs=-Xmx4096m
org.gradle.parallel=true
org.gradle.caching=true
```

## Dependencies

Key libraries used:

```kotlin
// AndroidX & Material
androidx.appcompat:appcompat:1.6.1
com.google.android.material:material:1.11.0
androidx.constraintlayout:constraintlayout:2.1.4

// Lifecycle & Coroutines
androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2
androidx.lifecycle:lifecycle-livedata-ktx:2.6.2
kotlinx.coroutines:coroutines-android:1.7.1

// Room Database
androidx.room:room-ktx:2.5.2
androidx.room:room-compiler:2.5.2  // kapt

// Jetpack Navigation
androidx.navigation:navigation-fragment-ktx:2.7.5
androidx.navigation:navigation-ui-ktx:2.7.5

// UI Components
androidx.recyclerview:recyclerview:1.3.1

// Testing
junit:junit:4.13.2
androidx.test.espresso:espresso-core:3.5.1
```

## Testing the App

### Manual Testing Checklist

1. **Login Screen**
   -  Launch app  see login screen
   -  Select "Student"  navigate to Student Dashboard
   -  Back button  return to login
   -  Try "Vendor"  navigate to Vendor Dashboard

2. **Student Dashboard**
   -  RecyclerView shows 3 menu items (Nasi Lemak, Roti Canai, Char Kuey Teow)
   -  Notification appears: "Menu Updated - 3 items available"
   -  Back button returns to login
   -  No database threading errors in logcat

3. **Notifications**
   -  Android 13+: Permission dialog appears on first launch
   -  Grant permission  notifications work
   -  Deny permission  app still works without notifications

4. **Navigation**
   -  Material Toolbar visible with title and back arrow
   -  Back arrow interactive
   -  Android 13+ system back gesture works

### Logcat Validation

Good signs (no errors):
```
D  Load libframework-connectivity-tiramisu-jni.so using APEX ns com_android_tethering
I  AssetManager2(...) locale list changing from [] to [en-US]
D  Menu updated notification shown
```

Bad signs (fix if seen):
```
E  Cannot access database on the main thread  // Database threading error
E  ActivityNotFoundException                  // Missing manifest activity
```

## Git Commit History

View all commits showing incremental development:

```bash
git log --oneline

27c8cde - feat(ui): add visible back button via MaterialToolbar
7f794a8 - fix(navigation): remove ActionBar requirement for NavController
9c12463 - refactor(architecture): Single Activity with Navigation Component
7ccb708 - feat(notifications): add NotificationHelper and BroadcastReceiver
eb23869 - refactor(threading): proper coroutine pattern with withContext
cce8ff9 - fix(threading): use Dispatchers.IO for database operations
331a4af - fix(manifest): declare StudentDashboard and VendorDashboard activities
8d6eba2 - feat(build): add app module, manifest, login layout, values
a400256 - fix(build): move Gradle settings to project root
e4b3d85 - chore(init): project setup with Gradle, manifest, base activities
```

Each commit is standalone and demonstrates a specific feature or fix.

## Development Best Practices Demonstrated

1. **MVVM Architecture**
   - Clean separation of concerns (UI  ViewModel  Repository  DAO)
   - ViewModel survives configuration changes
   - StateFlow for reactive updates

2. **Coroutines & Threading**
   - Structured concurrency with scopes
   - Proper dispatcher usage (Main, IO)
   - No GlobalScope (memory leak prevention)

3. **Modern Android**
   - Single Activity + Fragments (Navigation Component)
   - ViewBinding-ready layout structure
   - Material Design 3 compliance

4. **Database Best Practices**
   - Room ORM (no raw SQL)
   - Type-safe queries
   - Flow for reactive queries

5. **Lifecycle Awareness**
   - lifecycleScope for safe coroutine binding
   - viewLifecycleOwner in Fragments
   - Automatic cancellation on destroy

## Future Enhancements

Possible additions (not in scope for this lab):
- [ ] Vendor order management dashboard
- [ ] Shopping cart & checkout flow
- [ ] Order history & tracking
- [ ] Payment integration
- [ ] Rating & reviews
- [ ] Unit tests (JUnit 4, Mockito)
- [ ] Integration tests (Espresso)
- [ ] Offline-first with sync
- [ ] Firebase integration
- [ ] More sophisticated notifications

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Build fails with "R cannot be resolved" | Clean & Rebuild: Build  Clean Project  Rebuild |
| "Cannot access database on main thread" | Check if latest threading fix is applied (commit eb23869+) |
| "Activity not found" exception | Verify AndroidManifest.xml has all activities declared |
| Notifications not appearing | Check Android version (13+ for POST_NOTIFICATIONS perm), grant permission |
| Back button not visible | Ensure MaterialToolbar is in activity_main.xml layout |
| Emulator slow | Increase VM RAM: Settings  System Settings  Memory |

## License

Educational project for UTEM FTMK BiTP3453 lab assignment.

---

**Repository:** https://github.com/syakinaSYuhada/CampusFoodApp

**Author:** Syakina S. Yuhada  
**Date:** January 2026
