-- Produkty
INSERT INTO product (barcode, name, product_category, pet_type, normal_price, discount_type)
VALUES
    -- Produkty bez przeczeny
    ('1111111111111', 'Kocia karma', 'FOOD', 'CAT', 10.00, 'NONE'),
    ('2222222222222', 'Psia karma', 'FOOD', 'DOG', 10.00, 'NONE'),
    -- Produkty w przecenie multipack
    ('3333333333333', 'Karma dla koni', 'FOOD', 'HORSE', 30.00, 'MULTIPACK'),
    -- Produkty w przecenie bundle
    ('4444444444444', 'Boomerang', 'TOYS', 'DOG', 15.00, 'BUNDLE'),
    ('5555555555555', 'Maska Batmana', 'CLOTHING', 'DOG', 40.00, 'BUNDLE')
    ;



-- Promocje Multipack
INSERT INTO multipack_promotion (product_barcode, required_quantity, special_price, start_date, end_date, active)
VALUES
    ('3333333333333', 3, 80.00, DATEADD('DAY', -7, CURRENT_TIMESTAMP), DATEADD('DAY', 7, CURRENT_TIMESTAMP),  true),
    ('3333333333333', 2, 50.00, DATEADD('DAY', -14, CURRENT_TIMESTAMP), DATEADD('DAY', -7, CURRENT_TIMESTAMP),  false);



-- Promocje Bundle
INSERT INTO bundle_promotion (first_product_barcode, second_product_barcode, bundle_price, active)
VALUES
    ('4444444444444', '5555555555555', 50, true),
    ('1111111111111', '2222222222222', 15, false);
