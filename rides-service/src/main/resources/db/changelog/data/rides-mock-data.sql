INSERT INTO ride (driver_id, passenger_id, departure_address, destination_address, ride_status, order_date_time, cost)
VALUES (1, 1, '123 Main St', '456 Elm St', 'CREATED', '2023-10-01 10:00:00', 12.50),
       (1, 2, '789 Oak St', '321 Pine St', 'ACCEPTED', '2023-10-01 11:00:00', 15.75),
       (2, 1, '456 Maple St', '654 Cedar St', 'ON_THE_WAY_TO_PASSENGER', '2023-10-01 12:00:00', 10.00),
       (2, 3, '321 Birch St', '987 Spruce St', 'ON_THE_WAY_TO_DESTINATION', '2023-10-01 13:00:00', 20.00),
       (3, 2, '654 Oak St', '123 Main St', 'COMPLETED', '2023-10-01 14:00:00', 18.25),
       (3, 4, '789 Elm St', '456 Pine St', 'CANCELED', '2023-10-01 15:00:00', 0.00),
       (4, 1, '123 Maple St', '321 Cedar St', 'CREATED', '2023-10-01 16:00:00', 14.50),
       (4, 5, '456 Birch St', '654 Spruce St', 'ACCEPTED', '2023-10-01 17:00:00', 22.00),
       (5, 2, '789 Pine St', '987 Oak St', 'ON_THE_WAY_TO_PASSENGER', '2023-10-01 18:00:00', 16.75),
       (5, 3, '321 Elm St', '123 Cedar St', 'ON_THE_WAY_TO_DESTINATION', '2023-10-01 19:00:00', 13.00),
       (1, 4, '654 Spruce St', '456 Birch St', 'COMPLETED', '2023-10-01 20:00:00', 19.99),
       (2, 5, '987 Maple St', '321 Oak St', 'CANCELED', '2023-10-01 21:00:00', 0.00),
       (1, 1, '123 Cedar St', '654 Main St', 'CREATED', '2023-10-02 10:30:00', 11.00),
       (2, 2, '456 Elm St', '789 Oak St', 'ACCEPTED', '2023-10-02 11:30:00', 17.25),
       (3, 1, '789 Birch St', '321 Maple St', 'ON_THE_WAY_TO_PASSENGER', '2023-10-02 12:30:00', 12.00),
       (3, 3, '654 Spruce St', '456 Pine St', 'ON_THE_WAY_TO_DESTINATION', '2023-10-02 13:30:00', 15.50),
       (4, 2, '321 Maple St', '987 Cedar St', 'COMPLETED', '2023-10-02 14:30:00', 20.99),
       (4, 4, '456 Elm St', '654 Spruce St', 'CANCELED', '2023-10-02 15:30:00', 0.00),
       (5, 1, '789 Pine St', '321 Oak St', 'CREATED', '2023-10-02 16:30:00', 14.75),
       (5, 5, '123 Maple St', '987 Birch St', 'ACCEPTED', '2023-10-02 17:30:00', 23.50),
       (1, 3, '456 Cedar St', '654 Elm St', 'ON_THE_WAY_TO_PASSENGER', '2023-10-02 18:30:00', 16.00),
       (1, 2, '789 Spruce St', '321 Birch St', 'ON_THE_WAY_TO_DESTINATION', '2023-10-02 19:30:00', 14.25),
       (2, 4, '123 Oak St', '456 Maple St', 'COMPLETED', '2023-10-02 20:30:00', 18.50),
       (2, 5, '654 Pine St', '789 Birch St', 'CANCELED', '2023-10-02 21:30:00', 0.00),
       (3, 1, '789 Main St', '654 Elm St', 'CREATED', '2023-10-03 10:00:00', 15.00),
       (3, 2, '321 Cedar St', '456 Oak St', 'ACCEPTED', '2023-10-03 11:00:00', 19.00),
       (4, 1, '654 Birch St', '789 Maple St', 'ON_THE_WAY_TO_PASSENGER', '2023-10-03 12:00:00', 13.50),
       (4, 3, '987 Pine St', '123 Oak St', 'ON_THE_WAY_TO_DESTINATION', '2023-10-03 13:00:00', 17.75),
       (5, 2, '456 Elm St', '654 Cedar St', 'COMPLETED', '2023-10-03 14:00:00', 21.00);