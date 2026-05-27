-- 创建数据库
CREATE DATABASE IF NOT EXISTS fy_income_tracker CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE fy_income_tracker;

-- 用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    enabled BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 账号表
CREATE TABLE accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    account_name VARCHAR(100) NOT NULL,
    account_index INT DEFAULT 1,
    status VARCHAR(50) DEFAULT '正常',
    notes TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_user_id (user_id),
    INDEX idx_account_index (account_index)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 收益记录表
CREATE TABLE income_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    record_date DATE NOT NULL,
    total_income DECIMAL(15, 2) NOT NULL,
    dungeon_income DECIMAL(15, 2),
    stall_income DECIMAL(15, 2),
    task_income DECIMAL(15, 2),
    other_income DECIMAL(15, 2),
    item_cost DECIMAL(15, 2),
    gold_cost DECIMAL(15, 2),
    other_cost DECIMAL(15, 2),
    net_income DECIMAL(15, 2),
    time_spent INT COMMENT '花费时间(分钟)',
    notes TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    INDEX idx_user_id (user_id),
    INDEX idx_account_id (account_id),
    INDEX idx_record_date (record_date),
    UNIQUE INDEX idx_account_date (account_id, record_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建索引以优化查询性能
CREATE INDEX idx_income_records_user_date ON income_records(user_id, record_date);
CREATE INDEX idx_income_records_account_date ON income_records(account_id, record_date);
