<?php
    require 'coordenada.php';

    while (true) {
        $coord = new Coordenada(rand(0,100),rand(0,100));
        file_put_contents('resources/coordenadas.txt', $coord->toString() . PHP_EOL, FILE_APPEND);
        sleep(5);
    }
?>