-- MySQL Script generated by MySQL Workbench
-- 11/08/18 11:40:18
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema espacios
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema espacios
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `espacios` DEFAULT CHARACTER SET utf8 ;
USE `espacios` ;

-- -----------------------------------------------------
-- Table `espacios`.`edificios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `espacios`.`edificios` (
  `edificio` VARCHAR(5) NOT NULL,
  PRIMARY KEY (`edificio`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `espacios`.`numeros_edificios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `espacios`.`numeros_edificios` (
  `numero` INT NOT NULL,
  PRIMARY KEY (`numero`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `espacios`.`departamentos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `espacios`.`departamentos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `departamento` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `departamento_UNIQUE` (`departamento` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `espacios`.`profesores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `espacios`.`profesores` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `departamento` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_departamento`
    FOREIGN KEY (`id`)
    REFERENCES `espacios`.`departamentos` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `espacios`.`espacios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `espacios`.`espacios` (
  `edificio` VARCHAR(5) NOT NULL,
  `numero` INT NOT NULL,
  `descripcion` VARCHAR(45) NULL,
  `encargado` INT NULL,
  INDEX `fk_numero_idx` (`numero` ASC),
  PRIMARY KEY (`edificio`, `numero`),
  INDEX `fk_encargado_idx` (`encargado` ASC),
  CONSTRAINT `fk_edificio`
    FOREIGN KEY (`edificio`)
    REFERENCES `espacios`.`edificios` (`edificio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_numero`
    FOREIGN KEY (`numero`)
    REFERENCES `espacios`.`numeros_edificios` (`numero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_encargado`
    FOREIGN KEY (`encargado`)
    REFERENCES `espacios`.`profesores` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `espacios`.`nodos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `espacios`.`nodos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `rango` INT NULL,
  `espacio_edificio` VARCHAR(5) NULL,
  `espacio_numero` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_nodo_espacio_edificio_idx` (`espacio_edificio` ASC),
  INDEX `fk_nodo_espacio_numero_idx` (`espacio_numero` ASC),
  CONSTRAINT `fk_nodo_espacio_edificio`
    FOREIGN KEY (`espacio_edificio`)
    REFERENCES `espacios`.`espacios` (`edificio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_nodo_espacio_numero`
    FOREIGN KEY (`espacio_numero`)
    REFERENCES `espacios`.`espacios` (`numero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `espacios`.`espacio_profesor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `espacios`.`espacio_profesor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `profesor` INT NOT NULL,
  `espacio_edificio` VARCHAR(5) NOT NULL,
  `espacio_numero` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_profesor_idx` (`profesor` ASC),
  INDEX `fk_espacio_edificio_idx` (`espacio_edificio` ASC),
  INDEX `fk_espacio_numero_idx` (`espacio_numero` ASC),
  CONSTRAINT `fk_profesor`
    FOREIGN KEY (`profesor`)
    REFERENCES `espacios`.`profesores` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_espacio_edificio`
    FOREIGN KEY (`espacio_edificio`)
    REFERENCES `espacios`.`espacios` (`edificio`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_espacio_numero`
    FOREIGN KEY (`espacio_numero`)
    REFERENCES `espacios`.`espacios` (`numero`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `espacios`.`lecturas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `espacios`.`lecturas` (
  `nodo` INT NOT NULL,
  `posicionx` INT NOT NULL,
  `posiciony` INT NOT NULL,
  `temperatura` INT NOT NULL,
  `luminosidad` VARCHAR(45) NOT NULL,
  `humedad` VARCHAR(45) NOT NULL,
  `hora` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX `fk_nodo_idx` (`nodo` ASC),
  PRIMARY KEY (`nodo`),
  CONSTRAINT `fk_nodo`
    FOREIGN KEY (`nodo`)
    REFERENCES `espacios`.`nodos` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;