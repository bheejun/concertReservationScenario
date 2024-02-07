INSERT INTO member (id, member_name, password, point, role)
VALUES
    (UUID_TO_BIN('f8eff4fb-2ed4-466e-b606-1968f300af03'), 'Member1', 'Password1', 100.00, 'USER'),
    (UUID_TO_BIN('f8eff4fb-2ed4-466e-b606-1968f300af13'), 'Member2', 'Password2', 100.00, 'USER'),
    (UUID_TO_BIN('f8eff4fb-2ed4-466e-b606-1968f300af23'), 'Member3', 'Password3', 100.00, 'USER'),
    (UUID_TO_BIN('f8eff4fb-2ed4-466e-b606-1968f300af33'), 'Member4', 'Password4', 100.00, 'USER'),
    (UUID_TO_BIN('f8eff4fb-2ed4-466e-b606-1968f300af43'), 'Member5', 'Password5', 100.00, 'USER'),
    (UUID_TO_BIN('f8eff4fb-2ed4-466e-b606-1968f300af53'), 'Member6', 'Password6', 100.00, 'USER'),
    (UUID_TO_BIN('f8eff4fb-2ed4-466e-b606-1968f300af63'), 'Member7', 'Password7', 100.00, 'USER'),
    (UUID_TO_BIN('f8eff4fb-2ed4-466e-b606-1968f300af73'), 'Member8', 'Password8', 100.00, 'USER'),
    (UUID_TO_BIN('f8eff4fb-2ed4-466e-b606-1968f300af83'), 'Member9', 'Password9', 100.00, 'USER'),
    (UUID_TO_BIN('f8eff4fb-2ed4-466e-b606-1968f300af93'), 'Member10', 'Password10', 100.00, 'USER');



INSERT INTO concert (id, artist, concert_name, ticket_price) VALUES(UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bd'), 'Sample Artist', 'Sample Concert', 100.00);

INSERT INTO schedule (id, concert_date, seat_count, extra_seat_count, concert_id)
VALUES (UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), '2024-09-08 11:00', 50, 50, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bd'));



INSERT INTO seat (id, seat_num, is_booked, schedule_id, version)
VALUES
    (UUID_TO_BIN('f8eff4fb-2ed4-466e-b606-1968f300af75'), 1, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 2, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 3, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 4, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 5, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 6, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 7, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 8, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 9, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 10, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 11, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 12, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 13, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 14, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 15, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 16, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 17, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 18, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 19, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 20, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 21, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 22, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 23, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 24, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 25, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 26, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 27, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 28, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 29, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 30, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 31, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 32, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 33, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 34, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 35, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 36, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 37, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 38, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 39, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 40, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 41, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 42, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 43, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 44, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 45, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 46, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 47, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 48, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 49, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0),
    (UUID_TO_BIN(UUID()), 50, FALSE, UUID_TO_BIN('5311f80a-9187-414e-b3eb-484d639908bc'), 0);
