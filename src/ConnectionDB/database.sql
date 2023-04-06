BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "product" (
	"id"	INTEGER,
	"name"	TEXT NOT NULL,
	"description"	TEXT,
	"price"	REAL NOT NULL,
	"category"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "customer" (
	"id"	INTEGER,
	"first_name"	TEXT NOT NULL,
	"last_name"	TEXT NOT NULL,
	"email"	TEXT NOT NULL UNIQUE,
	"phone"	TEXT,
	"address"	TEXT,
	"city"	TEXT,
	"state"	TEXT,
	"zip"	TEXT,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "user" (
	"id"	INTEGER,
	"username"	TEXT NOT NULL UNIQUE,
	"password"	TEXT NOT NULL,
	"first_name"	TEXT NOT NULL,
	"last_name"	TEXT NOT NULL,
	"email"	TEXT NOT NULL UNIQUE,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "order_by" (
	"id"	INTEGER,
	"order_date"	TEXT NOT NULL,
	"customer_id"	INTEGER NOT NULL,
	"Total"	DECIMAL(10, 2) NOT NULL,
	FOREIGN KEY("customer_id") REFERENCES "customer"("id"),
	PRIMARY KEY("id")
);
COMMIT;
