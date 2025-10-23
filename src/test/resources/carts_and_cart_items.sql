-- Koszyki
INSERT INTO cart (id, session_id, created_at, updated_at) VALUES
('44585f42-f0e6-44db-8439-cfcf28d6d382', 'existing-cart-session-123' , now(), now()),
('55585f42-f0e6-44db-8439-cfcf28d6d382', 'multipack-cart-session-123' , now(), now()),
('66585f42-f0e6-44db-8439-cfcf28d6d382', 'bundle-cart-session-123' , now(), now())
;

-- Artykuły w koszykach
INSERT INTO cart_item (cart_id, product_barcode, quantity, price_stamp) VALUES
('44585f42-f0e6-44db-8439-cfcf28d6d382','1111111111111', 2, 10.00),
('44585f42-f0e6-44db-8439-cfcf28d6d382','2222222222222', 1, 10.00),

('55585f42-f0e6-44db-8439-cfcf28d6d382','3333333333333', 3, 30.00),
('55585f42-f0e6-44db-8439-cfcf28d6d382','2222222222222', 2, 10.00),

('66585f42-f0e6-44db-8439-cfcf28d6d382','4444444444444', 2, 15.00),
('66585f42-f0e6-44db-8439-cfcf28d6d382','5555555555555', 1, 40.00)
;