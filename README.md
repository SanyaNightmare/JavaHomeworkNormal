Домашнее задание №7 — Java Collections

Структура:
Все задания находятся в `src/main/java/hw7`:

1. `CustomArrayList` — реализация динамического массива.
2. `CollectionPerformanceExperiment` — сравнение ArrayList и LinkedList.
3. `StudentRegistry` — реестр студентов на HashMap и TreeMap.

Тесты находятся в `src/test/java/hw7`.

Запуск
Java 17+, Maven.

- Тесты: `mvn test` или Maven → Lifecycle → test в IDEA.
- Эксперимент: запуск `CollectionPerformanceExperiment.main()`.

Выводы эксперимента
ArrayList значительно быстрее при доступе по индексу и вставке в середину. LinkedList быстрее при удалении из начала, поскольку не требует сдвига элементов. Добавление в начало у обеих коллекций показало близкое время. При добавлении в конец LinkedList оказался быстрее, а при удалении с конца — ArrayList. Результаты зависят от условий запуска, поэтому выбор коллекции определяется типом операций.