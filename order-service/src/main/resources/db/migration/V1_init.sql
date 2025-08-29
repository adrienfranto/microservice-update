

CREATE TABLE IF NOT EXISTS t_orders (
    id SERIAL PRIMARY KEY,
    order_number VARCHAR(255) NOT NULL,
    skucode VARCHAR(255),
    price NUMERIC(10, 2) NOT NULL,
    quantity INTEGER NOT NULL
);
