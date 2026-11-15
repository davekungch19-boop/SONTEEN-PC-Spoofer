# SONTEEN PC Spoofer (Android, Kotlin + Jetpack Compose)

เปิดโฟลเดอร์นี้ใน Android Studio (Koala+ / JDK 17) -> Sync -> Build > Build APK(s)

โครงสร้าง
- domain/ : โมเดล, EmulatorProfile (รายการ prop ที่จะตั้งค่า), SpoofRepository (interface)
- data/   : RootShell (su), BackupStore (SharedPreferences), SpoofRepositoryImpl
- ui/     : MainActivity, theme (สี/คอมโพเนนต์), main (ViewModel + State + Screen)

หมายเหตุ: ต้องใช้เครื่อง root. prop ro.* ต้องใช้ resetprop (Magisk) และค่าจะหายเมื่อรีบูต
