-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: sql.freedb.tech
-- Generation Time: Feb 02, 2024 at 10:49 AM
-- Server version: 8.0.36-0ubuntu0.22.04.1
-- PHP Version: 8.2.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `freedb_nca_hores`
--

-- --------------------------------------------------------

--
-- Table structure for table `direccio`
--

CREATE TABLE `direccio` (
  `carreg` varchar(255) NOT NULL,
  `departament` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `empleats`
--

CREATE TABLE `empleats` (
  `id` int NOT NULL,
  `carreg` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `cognom` varchar(255) DEFAULT NULL,
  `correu_electronic` varchar(255) DEFAULT NULL,
  `contrasenya` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `personal_de_neteges`
--

CREATE TABLE `personal_de_neteges` (
  `carreg` varchar(255) NOT NULL,
  `area_de_feina` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `professors`
--

CREATE TABLE `professors` (
  `carreg` varchar(255) NOT NULL,
  `assignatura` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `secretaria`
--

CREATE TABLE `secretaria` (
  `carreg` varchar(255) NOT NULL,
  `tasques` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `direccio`
--
ALTER TABLE `direccio`
  ADD PRIMARY KEY (`carreg`);

--
-- Indexes for table `empleats`
--
ALTER TABLE `empleats`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `carreg` (`carreg`);

--
-- Indexes for table `personal_de_neteges`
--
ALTER TABLE `personal_de_neteges`
  ADD PRIMARY KEY (`carreg`);

--
-- Indexes for table `professors`
--
ALTER TABLE `professors`
  ADD PRIMARY KEY (`carreg`);

--
-- Indexes for table `secretaria`
--
ALTER TABLE `secretaria`
  ADD PRIMARY KEY (`carreg`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `empleats`
--
ALTER TABLE `empleats`
  MODIFY `id` int NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `direccio`
--
ALTER TABLE `direccio`
  ADD CONSTRAINT `direccio_ibfk_1` FOREIGN KEY (`carreg`) REFERENCES `empleats` (`carreg`);

--
-- Constraints for table `personal_de_neteges`
--
ALTER TABLE `personal_de_neteges`
  ADD CONSTRAINT `personal_de_neteges_ibfk_1` FOREIGN KEY (`carreg`) REFERENCES `empleats` (`carreg`);

--
-- Constraints for table `professors`
--
ALTER TABLE `professors`
  ADD CONSTRAINT `professors_ibfk_1` FOREIGN KEY (`carreg`) REFERENCES `empleats` (`carreg`);

--
-- Constraints for table `secretaria`
--
ALTER TABLE `secretaria`
  ADD CONSTRAINT `secretaria_ibfk_1` FOREIGN KEY (`carreg`) REFERENCES `empleats` (`carreg`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
