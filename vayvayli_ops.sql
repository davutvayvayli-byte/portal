-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Anamakine: localhost:3306
-- Üretim Zamanı: 23 Ara 2025, 10:52:03
-- Sunucu sürümü: 5.7.44
-- PHP Sürümü: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Veritabanı: `vayvayli_ops`
--

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `admin_tokens`
--

CREATE TABLE `admin_tokens` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `token_hash` char(64) NOT NULL,
  `expires_at` datetime NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `admin_tokens`
--

INSERT INTO `admin_tokens` (`id`, `user_id`, `token_hash`, `expires_at`, `created_at`) VALUES
(1, 1, '254dc7a5392c6be937158865164cfbb8ce018e0f3c6e478b986df4f8f11da1dd', '2025-12-30 10:10:51', '2025-12-23 10:10:51');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `admin_users`
--

CREATE TABLE `admin_users` (
  `id` int(11) NOT NULL,
  `username` varchar(191) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Tablo döküm verisi `admin_users`
--

INSERT INTO `admin_users` (`id`, `username`, `password_hash`, `created_at`) VALUES
(1, 'admin', '$2y$10$o3y0mL7hD4Mhwl7vwYy43.T4rY11qtjPUC77ctR0muC9IMuM6X0a.', '2025-12-23 08:37:37');

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `portal_banners`
--

CREATE TABLE `portal_banners` (
  `id` int(11) NOT NULL,
  `image_path` text NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `link_url` text,
  `sort_order` int(11) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `portal_blocks`
--

CREATE TABLE `portal_blocks` (
  `id` int(11) NOT NULL,
  `column_type` enum('FULL_TOP','LEFT','CENTER','RIGHT','FOOTER') NOT NULL DEFAULT 'CENTER',
  `block_title` varchar(255) NOT NULL,
  `block_type` enum('html','module') NOT NULL DEFAULT 'html',
  `module_id` int(11) DEFAULT NULL,
  `html_content` mediumtext,
  `sort_order` int(11) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `portal_categories`
--

CREATE TABLE `portal_categories` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `color` varchar(100) DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `sort_order` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `portal_galleries`
--

CREATE TABLE `portal_galleries` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `slug` varchar(255) NOT NULL,
  `cover` text,
  `count` int(11) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `sort_order` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `portal_gallery_items`
--

CREATE TABLE `portal_gallery_items` (
  `id` int(11) NOT NULL,
  `gallery_id` int(11) NOT NULL,
  `type` enum('image','video') NOT NULL DEFAULT 'image',
  `src` text NOT NULL,
  `caption` varchar(255) DEFAULT NULL,
  `sort_order` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `portal_media`
--

CREATE TABLE `portal_media` (
  `id` int(11) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `original_name` varchar(255) NOT NULL,
  `mime` varchar(100) NOT NULL,
  `size` bigint(20) NOT NULL,
  `url` text NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `portal_menus`
--

CREATE TABLE `portal_menus` (
  `id` int(11) NOT NULL,
  `menu_title` varchar(255) NOT NULL,
  `url` varchar(512) NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `sort` int(11) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `portal_messages`
--

CREATE TABLE `portal_messages` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `message` mediumtext NOT NULL,
  `is_read` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `portal_news`
--

CREATE TABLE `portal_news` (
  `id` int(11) NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `slug` varchar(255) NOT NULL,
  `summary` text,
  `content` mediumtext,
  `date` varchar(50) DEFAULT NULL,
  `image` text,
  `views` int(11) NOT NULL DEFAULT '0',
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `author` varchar(100) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `portal_pages`
--

CREATE TABLE `portal_pages` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `slug` varchar(255) NOT NULL,
  `content` mediumtext,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `sort_order` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tablo için tablo yapısı `portal_settings`
--

CREATE TABLE `portal_settings` (
  `key` varchar(191) NOT NULL,
  `value` text
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dökümü yapılmış tablolar için indeksler
--

--
-- Tablo için indeksler `admin_tokens`
--
ALTER TABLE `admin_tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `token_hash` (`token_hash`),
  ADD KEY `fk_tokens_user` (`user_id`);

--
-- Tablo için indeksler `admin_users`
--
ALTER TABLE `admin_users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Tablo için indeksler `portal_banners`
--
ALTER TABLE `portal_banners`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `portal_blocks`
--
ALTER TABLE `portal_blocks`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `portal_categories`
--
ALTER TABLE `portal_categories`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `portal_galleries`
--
ALTER TABLE `portal_galleries`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_gallery_slug` (`slug`);

--
-- Tablo için indeksler `portal_gallery_items`
--
ALTER TABLE `portal_gallery_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_gallery_item` (`gallery_id`);

--
-- Tablo için indeksler `portal_media`
--
ALTER TABLE `portal_media`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `portal_menus`
--
ALTER TABLE `portal_menus`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_menu_parent` (`parent_id`);

--
-- Tablo için indeksler `portal_messages`
--
ALTER TABLE `portal_messages`
  ADD PRIMARY KEY (`id`);

--
-- Tablo için indeksler `portal_news`
--
ALTER TABLE `portal_news`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_news_slug` (`slug`),
  ADD KEY `idx_news_cat` (`category_id`);

--
-- Tablo için indeksler `portal_pages`
--
ALTER TABLE `portal_pages`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `uq_pages_slug` (`slug`);

--
-- Tablo için indeksler `portal_settings`
--
ALTER TABLE `portal_settings`
  ADD PRIMARY KEY (`key`);

--
-- Dökümü yapılmış tablolar için AUTO_INCREMENT değeri
--

--
-- Tablo için AUTO_INCREMENT değeri `admin_tokens`
--
ALTER TABLE `admin_tokens`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Tablo için AUTO_INCREMENT değeri `admin_users`
--
ALTER TABLE `admin_users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Tablo için AUTO_INCREMENT değeri `portal_banners`
--
ALTER TABLE `portal_banners`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `portal_blocks`
--
ALTER TABLE `portal_blocks`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `portal_categories`
--
ALTER TABLE `portal_categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `portal_galleries`
--
ALTER TABLE `portal_galleries`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `portal_gallery_items`
--
ALTER TABLE `portal_gallery_items`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `portal_media`
--
ALTER TABLE `portal_media`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `portal_menus`
--
ALTER TABLE `portal_menus`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `portal_messages`
--
ALTER TABLE `portal_messages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `portal_news`
--
ALTER TABLE `portal_news`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Tablo için AUTO_INCREMENT değeri `portal_pages`
--
ALTER TABLE `portal_pages`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Dökümü yapılmış tablolar için kısıtlamalar
--

--
-- Tablo kısıtlamaları `admin_tokens`
--
ALTER TABLE `admin_tokens`
  ADD CONSTRAINT `fk_tokens_user` FOREIGN KEY (`user_id`) REFERENCES `admin_users` (`id`) ON DELETE CASCADE;

--
-- Tablo kısıtlamaları `portal_gallery_items`
--
ALTER TABLE `portal_gallery_items`
  ADD CONSTRAINT `fk_gallery_item` FOREIGN KEY (`gallery_id`) REFERENCES `portal_galleries` (`id`) ON DELETE CASCADE;

--
-- Tablo kısıtlamaları `portal_menus`
--
ALTER TABLE `portal_menus`
  ADD CONSTRAINT `fk_menu_parent` FOREIGN KEY (`parent_id`) REFERENCES `portal_menus` (`id`) ON DELETE SET NULL;

--
-- Tablo kısıtlamaları `portal_news`
--
ALTER TABLE `portal_news`
  ADD CONSTRAINT `fk_news_cat` FOREIGN KEY (`category_id`) REFERENCES `portal_categories` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
