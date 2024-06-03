-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 03, 2024 at 06:14 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `grocery_system`
--

-- --------------------------------------------------------

--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
  `account_id` int(11) NOT NULL,
  `account_name` varchar(255) NOT NULL,
  `account_type` enum('ADMIN','EMPLOYEE') NOT NULL DEFAULT 'EMPLOYEE',
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `accounts`
--

INSERT INTO `accounts` (`account_id`, `account_name`, `account_type`, `password`) VALUES
(1, 'ADMIN', 'ADMIN', '123'),
(2, 'EMPLOYEE', 'EMPLOYEE', '123');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `total_amount` double NOT NULL,
  `is_void` tinyint(1) NOT NULL DEFAULT 0,
  `order_date` date NOT NULL,
  `account_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `total_amount`, `is_void`, `order_date`, `account_id`) VALUES
(1, 252, 1, '2024-05-26', 1),
(2, 1060, 1, '2024-05-26', 1),
(3, 1260, 1, '2024-05-26', 1),
(4, 454, 0, '2024-05-26', 1),
(5, 656, 0, '2024-05-26', 1),
(6, 2320, 0, '2024-05-26', 1),
(7, 3828, 0, '2024-05-26', 1),
(8, 300, 0, '2024-05-26', 1),
(9, 50, 0, '2024-05-26', 1),
(10, 50, 1, '2024-05-26', 1),
(11, 50, 0, '2024-05-26', 1),
(12, 50, 0, '2024-05-26', 1),
(13, 50, 0, '2024-05-26', 1),
(14, 50, 0, '2024-05-26', 1),
(15, 50, 0, '2024-05-26', 1),
(22, 50, 0, '2024-05-26', 1),
(35, 50, 1, '2024-05-26', 1),
(36, 50, 0, '2024-05-26', 1),
(37, 252, 1, '2024-05-26', 1),
(41, 50, 0, '2024-05-26', 1),
(42, 252, 0, '2024-05-27', 1),
(43, 600, 0, '2024-05-27', 1),
(44, 21, 0, '2024-05-30', 1),
(45, 21, 0, '2024-05-30', 1);

-- --------------------------------------------------------

--
-- Table structure for table `order_details`
--

CREATE TABLE `order_details` (
  `order_detail_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_details`
--

INSERT INTO `order_details` (`order_detail_id`, `order_id`, `product_id`, `product_quantity`) VALUES
(1, 5, 1, 1),
(2, 5, 14, 3),
(3, 6, 21, 10),
(4, 6, 20, 10),
(7, 8, 1, 6),
(8, 9, 1, 1),
(9, 10, 1, 1),
(10, 11, 1, 1),
(11, 12, 1, 1),
(12, 13, 1, 1),
(13, 14, 1, 1),
(14, 15, 1, 1),
(21, 22, 1, 1),
(34, 35, 1, 1),
(35, 36, 1, 1),
(36, 37, 1, 1),
(37, 37, 14, 1),
(42, 41, 1, 1),
(45, 42, 1, 1),
(46, 42, 14, 1),
(47, 43, 1, 12),
(48, 44, 24, 1),
(49, 44, 23, 1),
(50, 45, 23, 1),
(51, 45, 24, 1);

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `quantity_available` int(11) NOT NULL,
  `product_type` varchar(255) NOT NULL,
  `is_available` tinyint(1) NOT NULL DEFAULT 1,
  `delivery_date` date NOT NULL,
  `expiry_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `product_name`, `price`, `quantity_available`, `product_type`, `is_available`, `delivery_date`, `expiry_date`) VALUES
(1, 'BACON', 50, 0, 'FROZEN FOODS', 1, '2024-05-01', '2024-05-31'),
(2, 'BACON      ', 23, 29, 'FROZEN FOOD', 0, '2024-05-02', '2024-05-30'),
(3, 'BACON', 20, 122, 'FROZEN FOOD', 0, '2024-05-02', '2024-05-21'),
(4, 'BACON', 12, 123, 'FROZEN FOOD', 0, '2024-05-01', '2024-05-24'),
(5, 'Bacon', 20, 12, 'Frozen Food', 0, '2024-05-01', '2024-05-30'),
(6, 'Bacon', 12, 33, 'Frozen Food', 0, '2024-05-01', '2024-05-31'),
(7, 'Bacon', 12, 33, 'Frozen Food', 0, '2024-05-01', '2024-05-31'),
(8, 'bacon', 12, 12, 'Frozen Foods', 0, '2024-05-01', '2024-05-25'),
(9, 'BACON    ', 23, 34, 'FROZEN FOOD', 0, '2024-05-01', '2024-05-29'),
(10, 'GRAHAM', 29, 100, 'SNACKS', 1, '2024-05-08', '2024-05-31'),
(11, 'BREAD', 50, 112, 'DRY FOOD', 0, '2024-05-01', '2024-05-31'),
(12, 'BEEF', 122, 12, 'FROZEN FOODS', 0, '2024-05-01', '2024-05-28'),
(13, 'PORK HALAL', 223, 213, 'FROZEN FOOD', 0, '2024-05-01', '2024-05-24'),
(14, 'PANCIT CANTON', 202, 183, 'DRY FOODS', 1, '2024-05-01', '2024-05-31'),
(15, 'ICE CREAM', 12, 123, 'BEVERAGES', 0, '2024-05-01', '2024-05-31'),
(16, 'BANANA', 10, 101, 'FRUITS', 1, '2024-05-01', '2024-05-31'),
(17, 'PANCAKE', 1, 10, 'FOOD', 1, '2024-05-01', '2024-05-31'),
(18, 'FRUTOS', 1.63, 20, 'CANDY', 1, '2024-05-01', '2024-05-31'),
(19, 'FRUTOS', 2.4, 29, 'CANDY', 0, '2024-05-01', '2024-05-31'),
(20, 'SNOW BEAR', 231, 199, 'CANDY', 1, '2024-05-01', '2024-05-25'),
(21, 'PANCAKE', 1, 10, 'FOOD', 1, '2024-05-01', '2024-05-31'),
(22, 'PANCAKES', 1, 10, 'FOOD', 0, '2024-05-01', '2024-05-31'),
(23, 'PIATOS', 12, 18, 'SNACKS', 1, '2024-05-01', '2024-05-31'),
(24, 'COFFEE', 9, 8, 'DRINKS', 1, '2024-05-28', '2024-05-31'),
(25, 'BURGER', 21, 12, 'FOOD', 1, '2024-05-29', '2024-06-11'),
(26, 'FRIES', 12, 12, 'FOOD', 0, '2024-06-03', '2024-06-21');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accounts`
--
ALTER TABLE `accounts`
  ADD PRIMARY KEY (`account_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `account_id` (`account_id`);

--
-- Indexes for table `order_details`
--
ALTER TABLE `order_details`
  ADD PRIMARY KEY (`order_detail_id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `accounts`
--
ALTER TABLE `accounts`
  MODIFY `account_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- AUTO_INCREMENT for table `order_details`
--
ALTER TABLE `order_details`
  MODIFY `order_detail_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=27;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`);

--
-- Constraints for table `order_details`
--
ALTER TABLE `order_details`
  ADD CONSTRAINT `order_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `order_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
