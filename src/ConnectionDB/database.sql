CREATE TABLE products (
  id INTEGER PRIMARY KEY,
  name TEXT NOT NULL,
  description TEXT,
  price REAL NOT NULL,
  category TEXT NOT NULL
);

CREATE TABLE customers (
	id INTEGER PRIMARY KEY,
	first_name TEXT NOT NULL,
	last_name TEXT NOT NULL,
	Email TEXT NOT NULL UNIQUE,
	Phone TEXT,
	address TEXT,
	city TEXT,
	state TEXT,
	zip TEXT
);

CREATE TABLE users (
  id INTEGER PRIMARY KEY,
  username TEXT NOT NULL,
  password TEXT NOT NULl,
  first_name TEXT NOT NULL,
  Email TEXT NOT NULL UNIQUE
);

CREATE TABLE orders (
  id INTEGER PRIMARY KEY,
  orders_data TEXT NOT NULL,
  customers_id INTEGER NOT NULL,
  Total DECIMAL(10, 2) NOT NULL
);
