# Pet Shop - Shopping Cart

## Opis projektu
Pet Shop to prosty system koszyka zakupowego dla sklepu zoologicznego.  
Projekt został stworzony w ramach zadania rekrutacyjnego.  
Aplikacja obsługuje m.in.:

- Dodawanie produktów do koszyka
- Obsługę różnych typów promocji:
  - **Multipack** – rabat przy zakupie wielu sztuk tego samego produktu
  - **Bundle** – rabat przy zakupie określonych kombinacji produktów
- Obliczanie wartości koszyka z uwzględnieniem promocji
- Generowanie DTO dla koszyka i produktów

Projekt nie posiada interfejsu użytkownika – wszystkie operacje realizowane są poprzez **REST API**.

## Logika działania koszyka i promocji

### Obliczanie wartości koszyka

Aplikacja przechowuje wybrane przez użytkownika produkty w koszyku (`CartEntity`). Informacje o każdym produkcie, tj. definicja produktu, czyli nazwa, cena, kod kreskowy są trzymane w bazie (`ProductEntity`) i to stąd pobieramy informację o aktualnej cenie. Dodatkowo ProductEntity zawiera enum DiscountType, w którym zawarta jest informacja czy i jakiego typu promocji podlega dany produkt (NONE, MULTIPACK, BUNDLE).
Podczas obliczania wartości koszyka stosowane są następujące kroki:

1. **Produkty bez promocji**  
   - Wszystkie produkty, których pole `discountType` w `ProductEntity` jest ustawione na `NONE`, są zliczane wprost: `normalPrice * quantity`.

2. **Promocje typu Multipack**  
   - Produkty z `discountType = MULTIPACK` są przetwarzane przez serwis `MultipackPromotionService`.
   - Repozytorium `MultipackPromotionRepository` przechowuje definicje promocji typu multipack, np. "Kup 3 sztuki produktu X, a ich cena będzie wtedy wynosić Z za sztukę".
   - Dla każdego takiego produktu sprawdzana jest aktywna promocja i obliczana cena na podstawie liczby pełnych pakietów oraz pozostałych sztuk.

3. **Promocje typu Bundle**  
   - Produkty z `discountType = BUNDLE` są przetwarzane przez serwis `BundlePromotionService`.
   - Repozytorium `BundlePromotionRepository` przechowuje definicje promocji typu bundle, np. "Produkt A + Produkt B kosztują razem Z".
   - Serwis iteruje po koszyku, identyfikuje wszystkie produkty kwalifikujące się do zestawów i oblicza rabaty dla każdego zestawu.  
   - Produkty, które zostały już użyte w bundle, są odpowiednio "odliczane" z dostępnej ilości, aby nie powtarzały się w innych promocjach.
  
   !Promocje typu bundle mają pierwszeństwo przed promocjami typu Multipack! - niestety zabrakło mi czasu, aby stworzyć logikę faworyzującą promocję korzystniejszą dla użytkownika

### Rola `DiscountType`

- Pole `discountType` w `ProductEntity` decyduje, czy dany produkt jest objęty promocją typu `MULTIPACK` lub `BUNDLE`, czy nie ma promocji (`NONE`).  
- Dzięki temu system wie, które produkty mają być przetwarzane w ramach odpowiednich metod promocji i przede wszystkim znacząco ograniczamy w ten sposób ilość zapytań do bazy danych

### Aktualizacja produktów

- Definicja produktu (`ProductEntity`) przechowuje aktualne informacje o produkcie, takie jak nazwa, cena normalna.
- Dane w `ProductEntity` mogą być aktualizowane, co pozwala na dynamiczne zmiany w promocjach i cenach, bez konieczności modyfikowania logiki koszyka.

### Podsumowanie procesu dodawania produktu do koszyka

1. **CartService -> calculateCartTotalWithoutPromotions()** Obliczenie wartości koszyka bez promocji, aby wyświetlić użytkownikowi cenę jaką zapłaciłby bez promocji.
2. **CartService -> calculateCartTotal()** Rozpoczęcie procesu liczenia wartości koszyka z promocjami
3. **CartCalculationService -> calculateNonDiscountedProducts()** Zliczenie wartości produktów, które nie mają przypisanej żadnej promocji (DiscountType.NONE).
4. **BundlePromotionService -> getDiscountedItemsTotal()** Obliczenie wartości produktów objętych promocją typu Bundle.
5. **MultipackPromotionService -> getDiscountedItemsTotal()** Obliczenie wartości produktów objętych promocją typu Multipack.
6. Suma wszystkich wartości jest zapisywana w DTO.

---
### Scheduler do usuwania starych koszyków

Scheduler CartCleanupScheduler automatycznie usuwa koszyki starsze niż 7 dni z bazy danych, aby zapobiec jej przepełnieniu i utrzymać porządek w danych tymczasowych. Uruchamia się codziennie o godzinie 3:00 w strefie czasowej Europe/Warsaw i usuwa koszyki, które nie były aktualizowane od 7 dni.


## Technologie

- Java 17
- Spring Boot
- Maven
- Hibernate / JPA
- Lombok
- MapStruct
- JUnit / Mockito (do testów jednostkowych)
