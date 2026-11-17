# Колледж ВТГК — Android-проект

Мобильное приложение для Высшего техническо-гуманитарного колледжа (vtgk.kz).
Разделы: Главная, Абитуриент, Специальности, Колледж (О колледже + Администрация), Контакты.
Цветовые режимы: Тёмная / AMOLED / Светлая + 4 акцентных цвета (выбор сохраняется).

## Как собрать APK в Android Studio
1. Установите Android Studio (Ladybug 2024.2 или новее).
2. File → Open → выберите папку `android`.
3. Дождитесь синхронизации Gradle (Studio сама скачает Gradle 8.9 и SDK 35).
   Если попросит создать Gradle Wrapper — согласитесь.
4. Build → Build App Bundle(s) / APK(s) → Build APK(s).
5. Готовый файл: `app/build/outputs/apk/debug/app-debug.apk` — его можно установить на телефон.

Для публикации в Google Play: Build → Generate Signed App Bundle / APK и свой ключ подписи.

## Сборка без Android Studio (GitHub)
Загрузите содержимое папки `android` в репозиторий GitHub — workflow
`.github/workflows/build-apk.yml` соберёт APK автоматически
(вкладка Actions → последний запуск → Artifacts → vtgk-apk).

## Структура
- `app/src/main/assets/index.html` — весь интерфейс приложения (автономный файл).
- `app/src/main/java/kz/vtgk/app/MainActivity.kt` — WebView, кнопка «Назад», открытие звонков/почты/соцсетей, цвет системных панелей.
- `res/drawable/ic_launcher_foreground.xml` — временная иконка, замените логотипом колледжа (res → New → Image Asset).

## Обновление контента
Дизайн редактируется в файле `VTGK App.dc.html` проекта; после правок экспортируйте его
заново в `assets/index.html` и пересоберите APK.
Фотографии загружаются с vtgk.kz, поэтому для них нужен интернет.
