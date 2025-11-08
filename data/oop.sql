
-- Cấu trúc bảng cho bảng `customer`
CREATE TABLE `customer` (
  `id` VARCHAR(3) NOT NULL PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL,
  `phone` VARCHAR(15) NOT NULL,
  `date_of_birth` VARCHAR(10) NOT NULL, -- dd/mm/yyyy
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Đang đổ dữ liệu cho bảng `customer`
INSERT INTO `customer` (`id`, `name`, `phone`, `date_of_birth`) VALUES
('A02', 'Nguyễn Văn A', 123456789, '11/03/1992'),
('A01', 'Nguyễn Văn Dương', 123671832, '10/10/2000'),
('B01', 'Phạm Hồng Ngọc', 378291827, '03/12/2000'),
('B02', 'Nguyễn Quang Hùng', 678473782, '06/03/1996'),
('A11', 'Nguyễn Thu Hà', 675893820, '22/10/1993'),
('A45', 'Nguyễn Quỳnh Anh', 767837190, '06/02/1998'),
('B81', 'Phan Quỳnh Anh', 567821009, '17/09/1997'),
('B12', 'Trần Ngọc Anh', 567398271, '28/12/1999'),
('A10', 'Lê Hà Thu', 127831980, '12/04/2000'),
('B46', 'Phạm Hà Linh', 91673829, '09/02/2000'),
('B10', 'Đổng Vu Hà', 672918022, '11/08/2001'),
('B03', 'Triệu Quốc Khánh', 567123901, '04/02/2002'),
('A04', 'Phạm Hà Anh', 109237846, '13/12/1999'),
('A05', 'Nguyễn Hồng Hà', 678391211, '22/03/1998'),
('B04', 'Trần Quốc Đạt', 677191223, '05/05/1998'),
('B09', 'Phạm Thu Hương', 192011204, '29/12/2000'),
('B07', 'Nguyễn Thi Hồng', 676389731, '11/06/2003'),
('A06', 'Đào Như Anh', 789345123, '04/01/1996'),
('A08', 'Lê Anh Đức', 789120909, '20/01/2000'),
('B21', 'Đào Huy Mạnh', 678290182, '05/11/1998'),
('B31', 'Lê Hoàng Anh', 678391281, '01/12/2001'),
('B19', 'Phạm Thị Hà', 671112281, '30/06/2000'),
('A19', 'Nguyễn Yến Nhi', 381967247, '30/06/1999'),
('A18', 'Đỗ Kiều Hạnh', 56700102, '28/07/2000'),
('A22', 'Nguyễn Văn G', 678391098, '01/01/2000'),
('C11', 'Nguyễn Văn Nam', 678347198, '10/10/1996');

-- Cấu trúc bảng cho bảng `product`
CREATE TABLE `product` (
  `id` SERIAL PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `description` TEXT,
  `stock_quantity` INT DEFAULT 0,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Đang đổ dữ liệu cho bảng `product`
INSERT INTO `product` (name, description, price, stock_quantity)
VALUES
('iPhone 15', 'Apple smartphone, 128GB storage', 999.99, 50),
('Samsung Galaxy S24', 'Android flagship phone', 899.50, 40),
('MacBook Air M3', 'Apple laptop, 13-inch', 1199.00, 20),
('Dell XPS 13', 'Windows ultrabook, 16GB RAM', 1099.00, 25),
('Sony WH-1000XM5', 'Noise cancelling headphones', 399.00, 30),
('Logitech MX Master 3S', 'Wireless mouse', 129.00, 60),
('Apple Watch Series 9', 'Smartwatch, GPS version', 449.00, 35),
('iPad Air', 'Apple tablet, 10.9-inch', 699.00, 40);

-- Cấu trúc bảng cho bảng `purchase`
CREATE TABLE `purchase` (
  `id` SERIAL PRIMARY KEY,
  `customer_id` VARCHAR(10) NOT NULL,
  `purchase_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `total_amount` DECIMAL(10,2),
  `purchase_status` VARCHAR(20) DEFAULT 'pending',  -- pending, completed, canceled
  `payment_method` VARCHAR(20) DEFAULT 'cash',      -- cash, transfer, card
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES customer(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Đang đổ dữ liệu cho bảng `purchase`
INSERT INTO `purchase` (customer_id, total_amount, purchase_status, payment_method, purchase_date)
VALUES
('A02', 899.99, 'completed', 'card', '2024-01-12 10:15:23'),
('B01', 1299.50, 'completed', 'transfer', '2024-01-15 14:42:10'),
('A05', 299.00, 'pending', 'cash', '2024-01-18 09:03:55'),
('B07', 749.99, 'completed', 'card', '2024-01-20 16:27:41'),
('A18', 1599.00, 'canceled', 'transfer', '2024-01-22 11:59:00'),
('C11', 499.00, 'completed', 'cash', '2024-01-25 08:13:22'),
('A22', 129.00, 'pending', 'card', '2024-01-26 19:45:17'),
('B21', 1899.99, 'completed', 'card', '2024-01-28 13:07:32'),
('A10', 349.50, 'completed', 'cash', '2024-01-29 10:01:49'),
('B12', 219.00, 'pending', 'transfer', '2024-02-01 17:55:05');

-- Cấu trúc bảng cho bảng `purchase_product`
CREATE TABLE `purchase_product` (
  `id` SERIAL PRIMARY KEY,
  `purchase_id` BIGINT UNSIGNED NOT NULL,
  `product_id` BIGINT UNSIGNED NOT NULL,
  `quantity` INT NOT NULL,
  `unit_price` DECIMAL(10,2) NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (purchase_id) REFERENCES purchase(id),
  FOREIGN KEY (product_id) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Đang đổ dữ liệu cho bảng `purchase_product`
INSERT INTO purchase_product (purchase_id, product_id, quantity, unit_price)
VALUES
(1, 2, 1, 899.99),

(2, 3, 1, 1199.00),
(2, 6, 1, 100.50),

(3, 6, 1, 129.00),
(3, 7, 1, 170.00),

(4, 5, 1, 399.00),
(4, 7, 1, 350.99),

(5, 3, 1, 1199.00),
(5, 5, 1, 400.00),

(6, 7, 1, 449.00),
(6, 6, 1, 50.00),

(7, 6, 1, 129.00),

(8, 3, 1, 1199.00),
(8, 1, 1, 700.99),

(9, 6, 1, 129.00),
(9, 5, 1, 220.50),

(10, 6, 1, 129.00),
(10, 7, 1, 90.00);

COMMIT;
