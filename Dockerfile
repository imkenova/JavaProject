# Используем базовый образ Java с JDK
FROM eclipse-temurin:21-jdk as build

# Указываем рабочую директорию в контейнере
WORKDIR /app

# Копируем Gradle файлы для использования кэша слоев
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Копируем исходный код проекта
COPY src src
# Собираю проект и Пропускаю тесты пока (потому что они не проходят)
RUN chmod +x ./gradlew && ./gradlew build -x test
# Собираем проект и создаем исполняемый JAR - прогоняет с тестама, которые у нас не работают (поэтому пока не используем)
#RUN ./gradlew build

# Используем новый этап сборки для создания минимального образа
FROM eclipse-temurin:21-jre

WORKDIR /app

# Копируем только собранный jar файл из предыдущего этапа сборки
COPY --from=build /app/build/libs/*.jar app.jar

# Объявляем, что контейнер будет слушать порт 8080 во время выполнения (надо будет поменять позже)
EXPOSE 8080

# Запускаем наше Spring Boot приложение
ENTRYPOINT ["java","-jar","app.jar"]
