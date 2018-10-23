function Persona(nombre, paterno, materno, nacimiento, peso, estatura, cintura, cadera, correo, telefono, genero, estadoCivil) {
    this.nombre = nombre;
    this.paterno = paterno;
    this.materno = materno;
    this.nacimiento = new Date(nacimiento);
    this.peso = peso;
    this.estatura = estatura;
    this.cintura = cintura;
    this.cadera = cadera;
    this.correo = correo;
    this.telefono = telefono;
    this.genero = genero;
    this.estadoCivil = estadoCivil;
    this.getNombreCompleto = function () {
        return this.nombre + " " + this.paterno + " " + this.materno;
    };
    this.getEdad = function () {
        var today = new Date();
        return today.getFullYear() - this.nacimiento.getFullYear();
    };
    this.getEstadoCivil = function () {
        return this.estadoCivil;
    };
    this.getNacimiento = function () {
        return this.nacimiento;
    };
}