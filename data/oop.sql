
-- Cấu trúc bảng cho bảng `consumer`

CREATE TABLE `consumer` (
  `id` VARCHAR(3) NOT NULL PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL,
  `phone` VARCHAR(15) NOT NULL,
  `date_of_birth` VARCHAR(10) NOT NULL -- dd/mm/yyyy
) ENGINE=INNODB DEFAULT CHARSET=UTF8;

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

COMMIT;


