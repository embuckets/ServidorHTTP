https://stackoverflow.com/questions/5299471/php-parsing-a-txt-file

<?php 
    // $txt = file_get_contents('alimentos.txt');
    // $rows = explode("\n", $txt);

    // foreach ($rows as $row => $data) {
    //     $row_data = explode("&", $data);
    // si de verdad es titulo length = 2

    // explode(",", $data);
    // si es titulo length es 1

    // }
    //strpos para checar si contiene caracter

    $st = "algo asfd,afd 2";
    print_r (explode("&", $st));
?>