<?php
class Alimento {
    private $nombre;
    private $categoria;
    private $porcion;

    public function __construct (
        string $nombre,
        string $categoria,
        string $porcion
    ) {
        $this->nombre = $nombre;
        $this->categoria = $categoria;
        $this->porcion = $porcion;
    }
    
    public function getNombre(): string {
        return $this->nombre;
    }
    public function getCategoria(): string {
        return $this->categoria;
    }
    public function getPorcion(): string {
        return $this->porcion;
    }

    public function toh1(): string {
        $result = '<h1>' . $this->nombre . '<h1>';
        return $result;
    }
}
?>
