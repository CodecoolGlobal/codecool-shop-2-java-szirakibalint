DROP TABLE IF EXISTS public.product CASCADE;
DROP TABLE IF EXISTS public.category CASCADE;
DROP TABLE IF EXISTS public.supplier CASCADE;
DROP TABLE IF EXISTS public.order CASCADE;
DROP TABLE IF EXISTS public.cart CASCADE;
DROP TABLE IF EXISTS public.cart_product CASCADE;

CREATE TABLE public.category (
    id INTEGER PRIMARY KEY,
    name VARCHAR NOT NULL,
    department VARCHAR NOT NULL,
    description VARCHAR NOT NULL
);

CREATE TABLE public.supplier (
    id INTEGER PRIMARY KEY,
    name VARCHAR NOT NULL,
    description VARCHAR NOT NULL
);

CREATE TABLE public.product (
    id INTEGER PRIMARY KEY,
    name VARCHAR NOT NULL,
    default_price DECIMAL(20, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    description VARCHAR NOT NULL,
    category_id INTEGER NOT NULL,
    supplier_id INTEGER NOT NULL,
    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
            REFERENCES category (id),
    CONSTRAINT fk_supplier
        FOREIGN KEY (supplier_id)
            REFERENCES supplier (id)
);

CREATE TABLE public.cart (
    id INTEGER PRIMARY KEY,
    user_id INTEGER NOT NULL
);

CREATE TABLE public.cart_product (
    cart_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    CONSTRAINT fk_cart
        FOREIGN KEY (cart_id)
            REFERENCES cart (id),
    CONSTRAINT fk_product
        FOREIGN KEY (product_id)
            REFERENCES product (id)
);

CREATE TABLE public.order (
    id INTEGER PRIMARY KEY,
    user_id INTEGER,
    name VARCHAR,
    valid BOOLEAN NOT NULL,
    address VARCHAR,
    message VARCHAR,
    cart_id INTEGER NOT NULL,
    CONSTRAINT fk_cart
        FOREIGN KEY (cart_id)
            REFERENCES cart (id)
);