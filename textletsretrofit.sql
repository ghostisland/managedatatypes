-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 14, 2021 at 11:34 PM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.4.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dtypes`
--

-- --------------------------------------------------------

--
-- Table structure for table `textletsretrofit`
--

CREATE TABLE `textletsretrofit` (
  `id` mediumint(9) NOT NULL,
  `date` varchar(500) NOT NULL,
  `name` varchar(500) NOT NULL,
  `email` varchar(500) NOT NULL,
  `password` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `textletsretrofit`
--

INSERT INTO `textletsretrofit` (`id`, `date`, `name`, `email`, `password`) VALUES
(1, '6046157265', 'haley', 'haley@email.com', '123abc'),
(2, '6021419973', 'jason', 'jason@email.com', '123abc'),
(3, '6047997999', 'kate', 'kate@email.com', '123abc'),
(4, '6071463735', 'dimitri', 'dimitri@email.com', '123abc'),
(5, '6057079800', 'morris', 'morris@email.com', '123abc'),
(6, '6076060242', 'juliet', 'juliet@email.com', '123abc'),
(7, '6099447489', 'rasia', 'rasia@email.com', '123abc'),
(8, '6051367167', 'elizabeth', 'elizabeth@email.com', '123abc'),
(9, '6039155742', 'sondra', 'sondra@email.com', '123abc'),
(10, '6057239667', 'kamilah', 'kamilah@email.com', '123abc');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `textletsretrofit`
--
ALTER TABLE `textletsretrofit`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `textletsretrofit`
--
ALTER TABLE `textletsretrofit`
  MODIFY `id` mediumint(9) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
