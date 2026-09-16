CREATE TABLE franchises(
                           franchise_id INT AUTO_INCREMENT,
                           franchise_name VARCHAR(50) UNIQUE NOT NULL,

                           PRIMARY KEY( franchise_id )
);

CREATE TABLE branches(
                         branch_id INT AUTO_INCREMENT,
                         branch_name VARCHAR(50) UNIQUE NOT NULL,
                         franchise_id INT,

                         PRIMARY KEY( branch_id ),
                         FOREIGN KEY( franchise_id ) REFERENCES franchises(franchise_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE products(
                         product_id INT AUTO_INCREMENT,
                         product_name VARCHAR(50) UNIQUE NOT NULL,

                         PRIMARY KEY( product_id )
);

CREATE TABLE branch_products(
                                branch_id INT,
                                product_id INT,
                                product_stock INT,

                                FOREIGN KEY( branch_id ) REFERENCES branches(branch_id) ON DELETE CASCADE ON UPDATE CASCADE,
                                FOREIGN KEY( product_id ) REFERENCES products(product_id) ON DELETE CASCADE ON UPDATE CASCADE
);