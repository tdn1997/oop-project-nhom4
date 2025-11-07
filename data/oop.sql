
-- Cấu trúc bảng cho bảng `consumer`
CREATE TABLE `consumer` (
  `id` VARCHAR(3) NOT NULL PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL,
  `phone` VARCHAR(15) NOT NULL,
  `date_of_birth` VARCHAR(10) NOT NULL -- format: dd/mm/yyyy
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Đang đổ dữ liệu cho bảng `consumer`
INSERT INTO `consumer` (`id`, `name`, `phone`, `date_of_birth`) VALUES
('A02', 'Nguyen Van A', 123456789, '11/03/1992'),
('A01', 'Nguyen Van Duong', 123671832, '10/10/2000'),
('B01', 'Pham Hong Ngoc', 378291827, '03/12/2000'),
('B02', 'Nguyen Quang Hung', 678473782, '06/03/1996'),
('A11', 'Nguyen Thu Ha', 675893820, '22/10/1993'),
('A45', 'Nguyen Quynh Anh', 767837190, '06/02/1998'),
('B81', 'Phan Quynh Anh', 567821009, '17/09/1997'),
('B12', 'Tran Ngoc Anh', 567398271, '28/12/1999'),
('A10', 'Le Ha Thu', 127831980, '12/04/2000'),
('B46', 'Pham Ha Linh', 91673829, '09/02/2000'),
('B10', 'Dong Vu Ha', 672918022, '11/08/2001'),
('B03', 'Trieu Quoc Khanh', 567123901, '04/02/2002'),
('A04', 'Pham Ha Anh', 109237846, '13/12/1999'),
('A05', 'Nguyen Hong Ha', 678391211, '22/03/1998'),
('B04', 'Tran Quoc Dat', 677191223, '05/05/1998'),
('B09', 'Pham Thu Huong', 192011204, '29/12/2000'),
('B07', 'Nguyen Thi Hong', 676389731, '11/06/2003'),
('A06', 'Dao Nhu Anh', 789345123, '04/01/1996'),
('A08', 'Le Anh Duc', 789120909, '20/01/2000'),
('B21', 'Dao Huy Manh', 678290182, '05/11/1998'),
('B31', 'Le Hoang Anh', 678391281, '01/12/2001'),
('B19', 'Pham thi Ha', 671112281, '30/06/2000'),
('A19', 'Nguyen Yen Nhi', 381967247, '30/06/1999'),
('A18', 'Do Kieu Hanh', 56700102, '28/07/2000'),
('A22', 'Nguyen Van G', 678391098, '01/01/2000'),
('C11', 'Nguyen Van Nam', 678347198, '10/10/1996');

-- Cấu trúc bảng cho bảng `products`
CREATE TABLE `products` (
  `id` SERIAL PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `description` TEXT,
  `stock_quantity` INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Đang đổ dữ liệu cho bảng `products`
INSERT INTO `products` (name, description, price, stock_quantity)
VALUES
('iPhone 15', 'Apple smartphone, 128GB storage', 999.99, 50),
('Samsung Galaxy S24', 'Android flagship phone', 899.50, 40),
('MacBook Air M3', 'Apple laptop, 13-inch', 1199.00, 20),
('Dell XPS 13', 'Windows ultrabook, 16GB RAM', 1099.00, 25),
('Sony WH-1000XM5', 'Noise cancelling headphones', 399.00, 30),
('Logitech MX Master 3S', 'Wireless mouse', 129.00, 60),
('Apple Watch Series 9', 'Smartwatch, GPS version', 449.00, 35),
('iPad Air', 'Apple tablet, 10.9-inch', 699.00, 40);

-- Cấu trúc bảng cho bảng `purchases`
CREATE TABLE `purchases` (
  `id` SERIAL PRIMARY KEY,
  `customer_id` VARCHAR(10) NOT NULL,
  `purchase_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `total_amount` DECIMAL(10,2),
  FOREIGN KEY (customer_id) REFERENCES consumer(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Đang đổ dữ liệu cho bảng `purchases`
INSERT INTO `purchases` (customer_id, purchase_date, total_amount)
VALUES
('A02', '2025-10-01 10:30:00', 1498.99),
('A01', '2025-10-05 14:15:00', 399.00),
('B01', '2025-10-07 09:00:00', 1598.00),
('A11', '2025-10-10 11:45:00', 699.00),
('B46', '2025-10-15 16:30:00', 1548.00),
('B03', '2025-10-18 13:20:00', 1998.00),
('A05', '2025-10-20 10:00:00', 799.00),
('A06', '2025-10-22 08:45:00', 1249.00),
('B21', '2025-10-25 17:00:00', 849.00),
('C11', '2025-10-28 09:30:00', 1699.00);

-- Cấu trúc bảng cho bảng `purchase_items`
CREATE TABLE `purchase_items` (
  `id` SERIAL PRIMARY KEY,
  `purchase_id` INT NOT NULL,
  `product_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  `unit_price` DECIMAL(10,2) NOT NULL,
  FOREIGN KEY (purchase_id) REFERENCES purchases(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Đang đổ dữ liệu cho bảng `purchase_items`
INSERT INTO `purchase_items` (purchase_id, product_id, quantity, unit_price)
VALUES
-- Purchase 1 (A02)
(1, 1, 1, 999.99),
(1, 6, 1, 129.00),
(1, 5, 1, 370.00),

-- Purchase 2 (A01)
(2, 5, 1, 399.00),

-- Purchase 3 (B01)
(3, 2, 1, 899.00),
(3, 8, 1, 699.00),

-- Purchase 4 (A11)
(4, 8, 1, 699.00),

-- Purchase 5 (B46)
(5, 4, 1, 1099.00),
(5, 6, 1, 129.00),
(5, 5, 1, 320.00),

-- Purchase 6 (B03)
(6, 3, 1, 1199.00),
(6, 7, 1, 449.00),
(6, 6, 1, 150.00),

-- Purchase 7 (A05)
(7, 8, 1, 699.00),
(7, 5, 1, 100.00),

-- Purchase 8 (A06)
(8, 1, 1, 999.99),
(8, 6, 2, 124.50),

-- Purchase 9 (B21)
(9, 2, 1, 849.00),

-- Purchase 10 (C11)
(10, 3, 1, 1199.00),
(10, 7, 1, 499.00);

COMMIT;
