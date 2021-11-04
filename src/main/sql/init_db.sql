DROP TABLE IF EXISTS public.product CASCADE;
DROP TABLE IF EXISTS public.category CASCADE;
DROP TABLE IF EXISTS public.supplier CASCADE;
DROP TABLE IF EXISTS public.order CASCADE;
DROP TABLE IF EXISTS public.valid_order CASCADE;
DROP TABLE IF EXISTS public.invalid_order CASCADE;
DROP TABLE IF EXISTS public.cart CASCADE;
DROP TABLE IF EXISTS public.cart_product CASCADE;

CREATE TABLE public.category (
    id INTEGER UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR NOT NULL,
    department VARCHAR NOT NULL,
    description VARCHAR NOT NULL
);

CREATE TABLE public.supplier (
    id INTEGER UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR NOT NULL,
    description VARCHAR NOT NULL
);

CREATE TABLE public.product (
    id INTEGER UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
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
    id SERIAL PRIMARY KEY NOT NULL,
    user_id INTEGER
);

CREATE TABLE public.cart_product (
    id INTEGER UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    cart_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    CONSTRAINT fk_cart
        FOREIGN KEY (cart_id)
            REFERENCES cart (id),
    CONSTRAINT fk_product
        FOREIGN KEY (product_id)
            REFERENCES product (id)
);

CREATE TABLE public.valid_order (
    id INTEGER UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR,
    last_name VARCHAR,
    country VARCHAR,
    city VARCHAR,
    address VARCHAR,
    user_id INTEGER
);

CREATE TABLE public.invalid_order (
    id INT UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    message VARCHAR
);

CREATE TABLE public.order (
    id INT UNIQUE PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    valid BOOLEAN NOT NULL,
    cart_data VARCHAR NOT NULL,
    cart_id INTEGER NOT NULL,
    valid_id INTEGER,
    invalid_id INTEGER,
    CONSTRAINT fk_cart
        FOREIGN KEY (cart_id)
            REFERENCES cart (id),
    CONSTRAINT fk_valid_order
        FOREIGN KEY (valid_id)
            REFERENCES valid_order (id),
    CONSTRAINT fk_invalid_order
        FOREIGN KEY (invalid_id)
            REFERENCES invalid_order (id)
);

INSERT INTO supplier (name, description)
VALUES ('Mojang Studios', 'Swedish video game company.'),
       ('EA', 'At the intersection of arts and electronics.'),
       ('Square Enix', 'They have a cool name.'),
       ('Other', 'Everything else.'),
       ('Codecool', 'Exciting games from our best & brightest.');

INSERT INTO category (NAME, DEPARTMENT, DESCRIPTION)
VALUES ('Action, adventure', 'Games', 'Pack your bag, say goodbye to your friends in the Shire.'),
       ('Sandbox, simulation', 'Games', 'Unleash your creativity.'),
       ('Strategy', 'Games', 'Strategy games requier thinking. In advance.');

INSERT INTO product (name, default_price, currency, description, category_id, supplier_id)
VALUES ('Tomb Raider',5.99, 'USD', 'The scariest one.', 1, 3),
       ('Tomb Raider 2', 5.99, 'USD', 'Make Lara do cool jumps.', 1, 3),
       ('Tomb Raider 3', 5.99, 'USD', 'Lots of swimming.', 1, 3),
       ('Tomb Raider: The Last Revelation', 5.99, 'USD', 'Does she even raid tombs?', 1, 3),
       ('Minecraft', 19.99, 'USD', 'Build a house made of dirt.', 2, 1),
       ('The Battle for Wesnoth', 0.00, 'USD', 'Wizards. Orcs. Trolls. Mermaids. Open-source turn-based strategy game.', 3, 4),
       ('Journey to the Center of the Earth', 1.99, 'USD', '.', 1, 4),
       ('Private Static Final Fantasy 2', 4.20, 'USD', 'Fantasy_final_v2_finalversion_2.exe', 1, 5),
       ('Terraria', 9.20, 'USD', 'Farming, I guess?', 2, 4),
       ('Yu-Gi-Oh! Power of Chaos: Yugi the Destiny (2003)', 1.20, 'USD', 'Try to play your cards right.', 3, 4),
       ('The Sims 2', 19.99, 'USD', 'Throw a pool party.', 2, 2),
       ('Minecraft Dungeons', 9.99, 'USD', '', 1, 1),
       ('Fifa 2004', 9.99, 'USD', 'Great music', 2, 2);

INSERT INTO cart (id, user_id)
VALUES (0, NULL);

