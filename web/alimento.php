<?php
class Alimento {
    var $nombre;
    var $categoria;
    var $porcion;

    public function toString(){
        echo $this->nombre, PHP_EOL;
        echo $this->categoria , PHP_EOL;
        echo $this->porcion , PHP_EOL;
    }

    public function Alimento($nombre, $categoria, $porcion){
        $this->nombre = $nombre;
        $this->categoria = $categoria;
        $this->porcion = $porcion;
    }
}

$ali = new Alimento('caldo de pollo', 'animal', '2 tazas');
// $ali->nombre = 'caldo de pollo';
// $ali->categoria = 'animal';
// $ali->porcion = '2 tazas';
$ali->toString();
?>
