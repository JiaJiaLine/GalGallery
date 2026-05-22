SET NAMES utf8mb4;

DROP DATABASE IF EXISTS `galgallery`;
CREATE DATABASE `galgallery` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `galgallery`;

CREATE TABLE `game` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `name` VARCHAR(100) NOT NULL COMMENT 'Game display name',
    `original_name` VARCHAR(150) NULL COMMENT 'Original game name',
    `cover_url` VARCHAR(255) NULL COMMENT 'Game cover image URL',
    `description` TEXT NULL COMMENT 'Game description',
    `developer` VARCHAR(100) NULL COMMENT 'Game developer or studio',
    `release_date` DATE NULL COMMENT 'Release date',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT 'Display sort order',
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Created time',
    `updated_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT 'Updated time',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Logical delete flag: 0 normal, 1 deleted',
    PRIMARY KEY (`id`),
    KEY `idx_game_name` (`name`),
    KEY `idx_game_deleted_sort` (`deleted`, `sort_order`, `id`),
    KEY `idx_game_created_at` (`created_at`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Game table';

CREATE TABLE `tag` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `name` VARCHAR(50) NOT NULL COMMENT 'Tag name',
    `color` VARCHAR(20) NULL COMMENT 'Tag color, for example #409EFF',
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Created time',
    `updated_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT 'Updated time',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Logical delete flag: 0 normal, 1 deleted',
    PRIMARY KEY (`id`),
    KEY `idx_tag_name` (`name`),
    KEY `idx_tag_deleted_name` (`deleted`, `name`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Tag table';

CREATE TABLE `gallery_image` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `game_id` BIGINT UNSIGNED NOT NULL COMMENT 'Related game ID',
    `type` VARCHAR(30) NOT NULL COMMENT 'Image type: character, photo, screenshot',
    `title` VARCHAR(100) NULL COMMENT 'Image title',
    `description` TEXT NULL COMMENT 'Image description',
    `image_url` VARCHAR(255) NOT NULL COMMENT 'Original image access URL',
    `thumbnail_url` VARCHAR(255) NULL COMMENT 'Thumbnail image access URL',
    `original_filename` VARCHAR(255) NOT NULL COMMENT 'Original uploaded filename',
    `file_size` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'File size in bytes',
    `width` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Image width in pixels',
    `height` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Image height in pixels',
    `mime_type` VARCHAR(80) NOT NULL COMMENT 'MIME type',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT 'Display sort order',
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Created time',
    `updated_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT 'Updated time',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Logical delete flag: 0 normal, 1 deleted',
    PRIMARY KEY (`id`),
    KEY `idx_gallery_image_game_id` (`game_id`),
    KEY `idx_gallery_image_type` (`type`),
    KEY `idx_gallery_image_game_type` (`game_id`, `type`, `deleted`),
    KEY `idx_gallery_image_deleted_sort` (`deleted`, `sort_order`, `id`),
    KEY `idx_gallery_image_created_at` (`created_at`),
    CONSTRAINT `fk_gallery_image_game`
        FOREIGN KEY (`game_id`) REFERENCES `game` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `ck_gallery_image_type`
        CHECK (`type` IN ('character', 'photo', 'screenshot'))
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Gallery image table';

CREATE TABLE `image_tag` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `gallery_image_id` BIGINT UNSIGNED NOT NULL COMMENT 'Related gallery image ID',
    `tag_id` BIGINT UNSIGNED NOT NULL COMMENT 'Related tag ID',
    `created_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'Created time',
    `updated_at` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT 'Updated time',
    `deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Logical delete flag: 0 normal, 1 deleted',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_image_tag_gallery_image_tag` (`gallery_image_id`, `tag_id`),
    KEY `idx_image_tag_gallery_image_id` (`gallery_image_id`),
    KEY `idx_image_tag_tag_id` (`tag_id`),
    KEY `idx_image_tag_deleted` (`deleted`),
    CONSTRAINT `fk_image_tag_gallery_image`
        FOREIGN KEY (`gallery_image_id`) REFERENCES `gallery_image` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `fk_image_tag_tag`
        FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci
  COMMENT='Gallery image and tag relation table';

INSERT INTO `game`
    (`id`, `name`, `original_name`, `cover_url`, `description`, `developer`, `release_date`, `sort_order`)
VALUES
    (1, 'Summer Pockets', 'Summer Pockets', '/uploads/covers/summer-pockets.webp', 'A gallery sample for character images and screenshots.', 'Key', '2018-06-29', 10),
    (2, 'Aokana', 'Ao no Kanata no Four Rhythm', '/uploads/covers/aokana.webp', 'A gallery sample for sky, school, and heroine images.', 'sprite', '2014-11-28', 20),
    (3, 'Cafe Stella', 'Cafe Stella to Shinigami no Chou', '/uploads/covers/cafe-stella.webp', 'A gallery sample for photo style assets and UI screenshots.', 'Yuzusoft', '2019-12-20', 30);

INSERT INTO `tag`
    (`id`, `name`, `color`)
VALUES
    (1, 'heroine', '#F56C6C'),
    (2, 'cg', '#E6A23C'),
    (3, 'screenshot', '#409EFF'),
    (4, 'favorite', '#67C23A'),
    (5, 'standing-art', '#909399');

INSERT INTO `gallery_image`
    (`id`, `game_id`, `type`, `title`, `description`, `image_url`, `thumbnail_url`, `original_filename`, `file_size`, `width`, `height`, `mime_type`, `sort_order`)
VALUES
    (1, 1, 'character', 'Shiroha standing art', 'Sample character standing art.', '/uploads/images/summer-pockets-shiroha.webp', '/uploads/thumbs/summer-pockets-shiroha.webp', 'shiroha-standing.webp', 842120, 1200, 1800, 'image/webp', 10),
    (2, 1, 'screenshot', 'Island opening scene', 'Sample opening scene screenshot.', '/uploads/images/summer-pockets-opening.webp', '/uploads/thumbs/summer-pockets-opening.webp', 'opening-scene.webp', 1248120, 1920, 1080, 'image/webp', 20),
    (3, 2, 'character', 'Asuka standing art', 'Sample heroine standing art.', '/uploads/images/aokana-asuka.webp', '/uploads/thumbs/aokana-asuka.webp', 'asuka-standing.webp', 932880, 1100, 1700, 'image/webp', 10),
    (4, 2, 'screenshot', 'Flying circus match', 'Sample match screenshot.', '/uploads/images/aokana-match.webp', '/uploads/thumbs/aokana-match.webp', 'flying-circus-match.webp', 1456000, 1920, 1080, 'image/webp', 20),
    (5, 3, 'photo', 'Cafe background reference', 'Sample background reference image.', '/uploads/images/cafe-stella-cafe.webp', '/uploads/thumbs/cafe-stella-cafe.webp', 'cafe-background.webp', 1012460, 1920, 1080, 'image/webp', 10);

INSERT INTO `image_tag`
    (`id`, `gallery_image_id`, `tag_id`)
VALUES
    (1, 1, 1),
    (2, 1, 5),
    (3, 2, 3),
    (4, 2, 4),
    (5, 3, 1),
    (6, 3, 5),
    (7, 4, 2),
    (8, 4, 3),
    (9, 5, 2),
    (10, 5, 4);
