SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `pccontrol` ;
CREATE SCHEMA IF NOT EXISTS `pccontrol` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `pccontrol` ;

-- -----------------------------------------------------
-- Table `pccontrol`.`times_on_day`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pccontrol`.`times_on_day` ;

CREATE TABLE IF NOT EXISTS `pccontrol`.`times_on_day` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` ENUM('START','END') NOT NULL,
  `date_action` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `pccontrol`.`minutes_per_day`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `pccontrol`.`minutes_per_day` ;

CREATE TABLE IF NOT EXISTS `pccontrol`.`minutes_per_day` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `week_day` VARCHAR(45) NOT NULL,
  `minutes` SMALLINT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
