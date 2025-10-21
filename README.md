# Pet Shop - Shopping Cart

## Opis projektu
Pet Shop to prosty system koszyka zakupowego dla sklepu zoologicznego.  
Projekt został stworzony w ramach zadania rekrutacyjnego dla firmy Pragmatic Coders.  
Aplikacja obsługuje m.in.:

- Dodawanie produktów do koszyka
- Obsługę różnych typów promocji:
  - **Multipack** – rabat przy zakupie wielu sztuk tego samego produktu
  - **Bundle** – rabat przy zakupie określonych kombinacji produktów
- Obliczanie wartości koszyka z uwzględnieniem promocji
- Generowanie DTO dla koszyka i produktów

Projekt nie posiada interfejsu użytkownika – wszystkie operacje realizowane są poprzez **REST API**.

---

## Technologie

- Java 17
- Spring Boot
- Maven
- Hibernate / JPA
- Lombok
- JUnit / Mockito (do testów jednostkowych)
