# Is Yeri Denetim Android APK (Kotlin + Compose)

Bu repo, is yeri denetimi icin cok modullu bir Android uygulamasi icerir.
Uygulama yonetici ve saha ekiplerinin ayni panelden operasyonu takip etmesini hedefler.

## Uygulama Modulleri

- Dashboard ve yonetici ozet ekranlari
- Calisan kaydi ve yonetici rol takibi
- Mesai kayitlari
- Izin talepleri ve hizli onay akisi
- Maas hesaplama kayitlari
- Ariza kaydi ve kapatma akisi
- Periyodik bakim plani
- Zimmet / varlik takibi
- Kalibrasyon takibi
- Denetim kontrol maddeleri (uygun/uygunsuz)
- Olay/kaza bildirimi
- Duzeltici faaliyet takibi
- Ziyaretci takibi
- Egitim takibi
- Vardiya planlama
- Dokuman gecerlilik suresi takibi

## Teknik Ozellikler

- Kotlin
- Jetpack Compose + Material 3
- Tek aktivite, cok sekmeli UI
- In-memory kayit yontemi ile hizli MVP

## APK Uretme

1. Android SDK kurulu bir ortamda calistirin.
2. Asagidaki komutu verin:

   ```bash
   ./gradlew assembleDebug
   ```

3. Cikan APK:

   ```text
   app/build/outputs/apk/debug/app-debug.apk
   ```

## Not

Bu surum MVP odaklidir. Uretim kullaniminda su iyilestirmeler onerilir:

- Kalici veri katmani (Room + repository)
- Kimlik dogrulama (yonetici/IK/denetci rolleri)
- API entegrasyonu ve raporlama
- Push bildirimleri ve gorev hatirlatmalari
