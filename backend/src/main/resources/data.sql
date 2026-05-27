INSERT INTO accounts (user_id, account_name, account_index, status, notes, created_at, updated_at)
VALUES (1, '五开账号1', 1, '正常', '默认账号1', NOW(), NOW()),
       (1, '五开账号2', 2, '正常', '默认账号2', NOW(), NOW());

INSERT INTO items (name, price, description, created_at, updated_at)
VALUES ('藏宝图', 120.00, '常用藏宝图', NOW(), NOW()),
       ('高级石', 58.50, '强化材料', NOW(), NOW());
