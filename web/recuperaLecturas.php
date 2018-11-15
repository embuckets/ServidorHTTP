<?php

    function arrayToJson($nodos){
        $jsonArr = json_encode($nodos);
        $output = "{ \"nodos\" : " .  $jsonArr . " }";
        return $output;
    }
    
    $servername = "localhost";
    $username = "root";
    $password = "evangelion01";
    $dbname = "espacios";

    
    //Create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // Check connection
    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    
    //array de nodos con la hora mas alta 
    $nodos = array();
    
    // $sql = "select nodo, posicionx, posiciony, max(hora) from lecturas group by nodo";
    $sql = "select c1.nodo, c1.posicionx, c1.posiciony, c1.hora from lecturas as c1 join ( select nodo, max(hora) as hora from lecturas group by nodo ) as c2 using (nodo, hora) order by c1.nodo";
    
    while (true){
        $result = $conn->query($sql);
        //abre archivo donde se va a guardar el json
        $myfile = fopen("nodos.json", "w") or die("Unable to open nodos.json!");

        if ($result->num_rows > 0) {
            // resultado puede tener varios rows
            while($row = $result->fetch_assoc()) {
                // echo "nodo: {$row['nodo']}, max:{$row['max(hora)']}" . PHP_EOL;
                // $myjson = json_encode($row);
                // echo $myjson . PHP_EOL;
                $nodos[] = $row;
                // echo $nodosjson . PHP_EOL . PHP_EOL;
                
                // file_put_contents('resources/datos-base.csv', "{$row["nodo"]},{$row["posicionx"]},{$row["posiciony"]},{$row["temperatura"]},{$row["luminosidad"]},{$row["humedad"]},{$row["hora"]}" . PHP_EOL, FILE_APPEND);
            }
        } else {
            echo "0 results";
        }
        // $nodosjson = json_encode($nodos);
        echo arrayToJson($nodos) . PHP_EOL . PHP_EOL;
        fwrite($myfile, arrayToJson($nodos));
        fclose($myfile); // flush
        $nodos = array();
        sleep(5);
    }
    $conn->close();

?>