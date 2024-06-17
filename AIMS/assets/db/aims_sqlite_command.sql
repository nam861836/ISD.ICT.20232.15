--
-- File generated with SQLiteStudio v3.4.4 on Tue Dec 12 02:07:58 2023
--
-- Text encoding used: System
--
PRAGMA foreign_keys = off;

ATTACH
"aims.db" AS "aims";

BEGIN TRANSACTION;

-- Table: Book
DROP TABLE IF EXISTS Book;

CREATE TABLE IF NOT EXISTS Book (
    id           INTEGER      PRIMARY KEY AUTOINCREMENT
                              NOT NULL,
    author       VARCHAR (45) NOT NULL,
    coverType    VARCHAR (45) NOT NULL,
    publisher    VARCHAR (45) NOT NULL,
    publishDate  DATETIME     NOT NULL,
    numOfPages   INTEGER      NOT NULL,
    language     VARCHAR (45) NOT NULL,
    bookCategory VARCHAR (45) NOT NULL,
    CONSTRAINT fk_book_product FOREIGN KEY (
        id
    )
    REFERENCES Product (id)
);


-- Table: Card
--DROP TABLE IF EXISTS Card;
--
--CREATE TABLE IF NOT EXISTS Card (
--    id             INTEGER      NOT NULL
--                                PRIMARY KEY,
--    cardNumber     VARCHAR (45) NOT NULL,
--    holderName     VARCHAR (45) NOT NULL,
--    expirationDate DATE         NOT NULL,
--    securityCode   VARCHAR (45) NOT NULL
--);


-- Table: CD
DROP TABLE IF EXISTS CD;

CREATE TABLE IF NOT EXISTS CD (
    id           INTEGER      PRIMARY KEY
                              NOT NULL,
    artist       VARCHAR (45) NOT NULL,
    recordLabel  VARCHAR (45) NOT NULL,
    musicType    VARCHAR (45) NOT NULL,
    releasedDate DATE,
    CONSTRAINT fk_cd_product FOREIGN KEY (
        id
    )
    REFERENCES Product (id)
);


-- Table: DVD
DROP TABLE IF EXISTS DVD;

CREATE TABLE IF NOT EXISTS DVD (
    id           INTEGER      PRIMARY KEY
                              NOT NULL,
    discType     VARCHAR (45) NOT NULL,
    director     VARCHAR (45) NOT NULL,
    runtime      INTEGER      NOT NULL,
    studio       VARCHAR (45) NOT NULL,
    subtitle     VARCHAR (45) NOT NULL,
    releasedDate DATETIME,
    filmType     VARCHAR (45) NOT NULL,
    CONSTRAINT fk_dvd_product FOREIGN KEY (
        id
    )
    REFERENCES Product (id)
);


-- Table: Product
DROP TABLE IF EXISTS Product;

CREATE TABLE IF NOT EXISTS Product (
    id       INTEGER      PRIMARY KEY AUTOINCREMENT
                          NOT NULL,
    type     VARCHAR (45) NOT NULL,
    category VARCHAR (45) NOT NULL,
    price    INTEGER      NOT NULL,
    quantity INTEGER      NOT NULL,
    title    VARCHAR (45) NOT NULL,
    value    INTEGER      NOT NULL,
    imageUrl VARCHAR (45) NOT NULL
);


-- Table: Order
DROP TABLE IF EXISTS [Order];

CREATE TABLE IF NOT EXISTS [Order] (
    id           INTEGER       NOT NULL
                               PRIMARY KEY AUTOINCREMENT,
    name         VARCHAR (45)  NOT NULL,
    address      VARCHAR (45)  NOT NULL,
    phone        VARCHAR (45)  NOT NULL,
    shippingFees INTEGER       NOT NULL,
    instruction  VARCHAR (255),
    province     VARCHAR (255),
    status       INTEGER       DEFAULT (0),
    amount       INTEGER       DEFAULT (100000) 
);


-- Table: OrderItem
DROP TABLE IF EXISTS OrderItem;

CREATE TABLE IF NOT EXISTS OrderItem (
    productID  INTEGER NOT NULL,
    orderID  INTEGER NOT NULL,
    price    INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    PRIMARY KEY (
        productID,
        orderID
    ),
    CONSTRAINT fk_orderitem_item FOREIGN KEY (
        productID
    )
    REFERENCES Product (id),
    CONSTRAINT fk_orderitem_order FOREIGN KEY (
        orderID
    )
    REFERENCES [Order] (id) 
);


-- Table: PaymentTransaction
DROP TABLE IF EXISTS PaymentTransaction;

CREATE TABLE IF NOT EXISTS PaymentTransaction (
    id            INTEGER      PRIMARY KEY AUTOINCREMENT
                               NOT NULL,
    orderID       INTEGER      NOT NULL,
    createdAt     DATETIME     NOT NULL,
    content       VARCHAR (45) NOT NULL,
    txnRef        VARCHAR (45),
    cardType      VARCHAR (45),
    amount        INTEGER      DEFAULT (0),
    transactionNo VARCHAR,
    CONSTRAINT fk_transaction_order FOREIGN KEY (
        orderID
    )
    REFERENCES [Order] (id) 
);


-- Table: Shipment
DROP TABLE IF EXISTS Shipment;

CREATE TABLE IF NOT EXISTS Shipment (
    id                  INTEGER       NOT NULL
                                      PRIMARY KEY AUTOINCREMENT,
    shipType            INTEGER       NOT NULL,
    deliveryInstruction VARCHAR (255),
    deliveryTime        VARCHAR (255),
    shipmentDetail      VARCHAR (255),
    orderId             INTEGER       CONSTRAINT Shipment_Order_id_fk REFERENCES [Order]
);


-- Index: OrderItem.fk_orderitem_order_idx
DROP INDEX IF EXISTS [OrderItem.fk_orderitem_order_idx];

CREATE INDEX IF NOT EXISTS [OrderItem.fk_orderitem_order_idx] ON OrderItem (
    "orderID"
);


-- Index: Transaction.fk_transaction_order_idx
DROP INDEX IF EXISTS [Transaction.fk_transaction_order_idx];

CREATE INDEX IF NOT EXISTS [Transaction.fk_transaction_order_idx] ON PaymentTransaction (
    "orderID"
);


COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
