UPDATE ykd_ebusiness_product
SET images = CONCAT('["/uploads/real/p', id, '-main.jpg"]'),
    detail = CONCAT('["/uploads/real/p', id, '-detail.jpg"]');
