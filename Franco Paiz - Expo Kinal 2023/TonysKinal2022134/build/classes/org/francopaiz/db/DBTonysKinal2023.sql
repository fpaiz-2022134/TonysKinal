/*
    Franco Alejandro Paiz González
    IN5AV
    2022134
    Creación: 28/03/2023
    Fechas de modificación: 28/03/2023
                            04/04/2023
                            06/04/2023
                            25/04/2023
                            24/05/2023
                            29/05/2023
                            31/05/2023
                            01/06/2023
                            

        
    
    
*/

Drop database if exists DBTonysKinal2023;
Create database DBTonysKinal2023;

Use DBTonysKinal2023;

Create table Empresas(
	codigoEmpresa int not null auto_increment,
    nombreEmpresa varchar(150) not null,
    direccion varchar(150) not null,
    telefono varchar(8) not null,
    primary key PK_codigoEmpresa (codigoEmpresa)
);

Create table TipoEmpleado(
	codigoTipoEmpleado int not null auto_increment,
    descripcion varchar(50) not null,
    primary key PK_codigoTipoEmpleado(codigoTipoEmpleado)
);

Create table TipoPlato(
	codigoTipoPlato int not null auto_increment,
    descripcionTipo varchar(100) not null,
    primary key PK_codigoTipoPlato (codigoTipoPlato)
);

Create table Productos(
	codigoProducto int not null auto_increment,
    nombreProducto varchar(150) not null,
    cantidadProducto int not null,
    primary key PK_codigoProducto (codigoProducto)
);

Create table Empleados(
	codigoEmpleado int not null auto_increment,
    numeroEmpleado int not null,
    apellidosEmpleado varchar(150) not null,
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar(150) not null,
    telefonoContacto varchar(8) not null,
    gradoCocinero varchar(50),
    codigoTipoEmpleado int not null,
    primary key PK_codigoEmpleado (codigoEmpleado),
    constraint FK_Empleados_TipoEmpleado foreign key
		(codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado)
);

Create table Servicios(
	codigoServicio int not null auto_increment,
    fechaServicio date not null,
    tipoServicio varchar(150) not null,
    horaServicio time not null,
    lugarServicio varchar(150) not null,
    telefonoContacto varchar(8),
    codigoEmpresa int not null,
    primary key PK_codigoServicio (codigoServicio),
    constraint FK_Servicios_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa)
);

Create table Presupuesto(
	codigoPresupuesto int not null auto_increment,
    fechaSolicitud date not null,
    cantidadPresupuesto decimal(10,2) not null,
    codigoEmpresa int not null,
    primary key PK_codigoPresupuesto (codigoPresupuesto),
    constraint FK_Presupuesto_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa)
);

Create table Platos(
	codigoPlato int not null auto_increment,
    cantidad int not null,
    nombrePlato varchar(50) not null,
    descripcionPlato varchar(150) not null,
    precioPlato decimal(10,2) not null,
    codigoTipoPlato int not null,
    primary key PK_codigoPlato (codigoPlato),
    constraint FK_Platos_TipoPlato foreign key(codigoTipoPlato)
		references TipoPlato(codigoTipoPlato)
);

Create table Productos_has_Platos(
	Productos_codigoProducto int not null,
    codigoPlato int not null,
    codigoProducto int not null,
    primary key PK_Productos_codigoProducto (Productos_codigoProducto),
    constraint FK_Productos_has_Platos_Productos foreign key(codigoProducto)
		references Productos(codigoProducto)
);


Create table Servicios_has_Platos(
	Servicios_codigoServicio int not null,
    codigoPlato int not null,
    codigoServicio int not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Platos_Servicios foreign key(codigoServicio)
		references Servicios(codigoServicio),
	constraint FK_Servicios_has_Platos foreign key (codigoPlato)
		references Platos(codigoPlato)
);

Create table Servicios_has_Empleados(
	Servicios_codigoServicio int not null,
    codigoServicio int not null,
    codigoEmpleado int not null,
    fechaEvento date not null,
    horaEvento time not null,
    lugarEvento varchar(150) not null,
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio),
    constraint FK_Servicios_has_Empleados_Servicios foreign key (codigoServicio)
		references Servicios(codigoServicio),
    constraint FK_Servicios_has_Empleados_Empleados foreign key(codigoEmpleado) 
		references Empleados(codigoEmpleado)
    
);

-- ---------------------------------------PROCEDIMIENTOS ALMACENADOS -----------------------
-- -------------------------- EMPRESAS -----------------------------------------------------
-- Agregar Empresa

Delimiter $$
	Create procedure sp_AgregarEmpresa(in nombreEmpresa varchar(150), 
    in direccion varchar(150), in telefono varchar(10))
		Begin
			insert into Empresas(nombreEmpresa, direccion, telefono)
				values(nombreEmpresa, direccion, telefono);
		End $$
Delimiter ;

call sp_AgregarEmpresa('Tigo', '16 avenida WTC local 2-09 zona 5, Ciudad de Guatemala', '23450909');
call sp_AgregarEmpresa('Claro', 'Centra Sur, Local 6', '12223344');
call sp_AgregarEmpresa('DirectEnglsih','Villa Nueva, Santa Clara', '45678800');
call sp_AgregarEmpresa('Pinulito','Amatitlán, 12 calle 3-04', '32458801');
call sp_AgregarEmpresa('Eggsa','Zona 5, Cd. Guatemala', '21234566');
call sp_AgregarEmpresa('Toyota','Ciudad Guatemala, zona 7', '67883211');
call sp_AgregarEmpresa('Walmart','Villa Nueva, Santa Clara', '32447676');
call sp_AgregarEmpresa('Honda','Pacific Center, 2do. Nivel', '44332211');
call sp_AgregarEmpresa('Steren','Zona 7, Guatemala', '44557799');
call sp_AgregarEmpresa('Max','Villa Romana, Local 12', '45657722');



-- Editar Empresa

Delimiter $$
	Create procedure sp_EditarEmpresa(in codEmpresa int, in nomEmpresa varchar(150), 
    in direc varchar(150), in tel varchar(10))
		Begin
			Update Empresas E
				set E.nombreEmpresa = nomEmpresa, E.direccion = direc, E.telefono = tel
					Where E.codigoEmpresa = codEmpresa; 
		End $$
Delimiter ;

call sp_EditarEmpresa(1,'Tigo', '27 avenida WTC local 2-09 zona 5, Ciudad de Guatemala', '23450909');

-- Eliminar Empresa

Delimiter $$
	Create procedure sp_EliminarEmpresa(in codEmpresa int)
		Begin
			Delete from Empresas 
				Where codigoEmpresa = codEmpresa;
		End $$
Delimiter ;

-- call sp_EliminarEmpresa(1);

-- Listar Empresa

Delimiter $$
	Create procedure sp_ListarEmpresas()
		Begin
			Select
				E.codigoEmpresa,
				E.nombreEmpresa,
				E.direccion,
				E.telefono
					from Empresas E;
		End$$
Delimiter ;

call sp_ListarEmpresas();

-- Buscar Empresa

Delimiter $$
	Create procedure sp_BuscarEmpresa(in codEmpresa int)
		Begin
			Select
				E.codigoEmpresa,
				E.nombreEmpresa,
				E.direccion,
				E.telefono
					from Empresas E
						where E.codigoEmpresa = codEmpresa ;
		End $$
Delimiter ;

call sp_BuscarEmpresa(1);


-- ---------------------------------------PROCEDIMIENTOS ALMACENADOS -----------------------
-- -------------------------- TipoEmpleado -----------------------------------------------------
-- Agregar TipoEmpleado

Delimiter $$
	Create procedure sp_AgregarTipoEmpleado(in descripcion varchar(100))
		Begin
			Insert into TipoEmpleado (descripcion)
				values(descripcion);
				
		End $$
Delimiter ;

call sp_AgregarTipoEmpleado('Encargado de alimentos.');
call sp_AgregarTipoEmpleado('Chef');
call sp_AgregarTipoEmpleado('Conserje');
call sp_AgregarTipoEmpleado('Gastrónomo/Gourmet');
call sp_AgregarTipoEmpleado('Recepcionista');
call sp_AgregarTipoEmpleado('Catador o Sommelier');
call sp_AgregarTipoEmpleado('Ayudante camarero');
call sp_AgregarTipoEmpleado('Bodeguero');
call sp_AgregarTipoEmpleado('Asistente de Cocina');
call sp_AgregarTipoEmpleado('Maitre/Administrador');
call sp_AgregarTipoEmpleado('Cocinero');



-- Editar TipoEmpleado

Delimiter $$
	Create procedure sp_EditarTipoEmpleado(in codTipoEmpleado int, in descrip varchar(100))
		Begin
			Update TipoEmpleado TE
				set TE.descripcion = descrip
					Where TE.codigoTipoEmpleado = codTipoEmpleado;
		End $$
Delimiter ;

call sp_EditarTipoEmpleado(1, 'Encargado de cocina/Sub chef');

-- Eliminar TipoEmpleado

Delimiter $$
	Create procedure sp_EliminarTipoEmpleado(in codTipoEmpleado int)
		Begin
			Delete from TipoEmpleado 
				Where codigoTipoEmpleado = codTipoEmpleado;
		End $$
Delimiter ;

-- call sp_EliminarTipoEmpleado(1);

-- Listar TipoEmpleado

Delimiter $$
	Create procedure sp_ListarTiposEmpleados()
		Begin
			Select
				TE.codigoTipoEmpleado,
				TE.descripcion
					from TipoEmpleado TE;
		End$$
Delimiter ;

call sp_ListarTiposEmpleados();

-- Buscar TipoEmpleado

Delimiter $$
	Create procedure sp_BuscarTipoEmpleado(in codTipoEmpleado int)
		Begin
			Select
				TE.codigoTipoEmpleado,
				TE.descripcion
					from TipoEmpleado TE
						Where TE.codigoTipoEmpleado = codTipoEmpleado;
		End$$
Delimiter ;

call sp_BuscarTipoEmpleado(1);

-- ---------------------------------------PROCEDIMIENTOS ALMACENADOS -----------------------
-- -------------------------- TipoPlato -----------------------------------------------------
-- Agregar TipoPlato

Delimiter $$
	Create procedure sp_AgregarTipoPlato(in descripcionTipo varchar(100))
		Begin
			insert into TipoPlato(descripcionTipo)
				values(descripcionTipo);
		End $$
Delimiter ;

call sp_AgregarTipoPlato('Lasagna');
call sp_AgregarTipoPlato('Tradicional');
call sp_AgregarTipoPlato('Especialidad');
call sp_AgregarTipoPlato('Comida China');
call sp_AgregarTipoPlato('Desayunos');
call sp_AgregarTipoPlato('Comida Tailandesa');
call sp_AgregarTipoPlato('Bebida Tropical');
call sp_AgregarTipoPlato('Entrada');
call sp_AgregarTipoPlato('Pastas');
call sp_AgregarTipoPlato('Sopas');
call sp_AgregarTipoPlato('Ensaladas');


-- Editar TipoPlato

Delimiter $$
	Create procedure sp_EditarTipoPlato(in codTipoPlato int, in descripTipo varchar(100))
		Begin
			Update TipoPlato TP
				set descripcionTipo = descripTipo
					Where TP.codigoTipoPlato = codTipoPlato;
		End $$
Delimiter ;

call sp_EditarTipoPlato(1, 'Mariscos');

-- Eliminar TipoPlato

Delimiter $$
	Create procedure sp_EliminarTipoPlato(in codTipoPlato int)
		Begin
			Delete from TipoPlato 
				Where codigoTipoPlato = codTipoPlato;
		End $$
Delimiter ;

-- call sp_EliminarTipoPlato(1);

-- Listar TipoPlato

Delimiter $$
	Create procedure sp_ListarTiposPlatos()
		Begin
			Select
				TP.codigoTipoPlato,
				TP.descripcionTipo
					from TipoPlato TP;
		End$$
Delimiter ;

call sp_ListarTiposPlatos();

-- Buscar TipoPlato

Delimiter $$
	Create procedure sp_BuscarTipoPlato(in codTipoPlato int)
		Begin
			Select
				TP.codigoTipoPlato,
				TP.descripcionTipo
					from TipoPlato TP
						Where TP.codigoTipoPlato = codTipoPlato;
		End $$
Delimiter ;

call sp_BuscarTipoPlato(1);

-- ---------------------------------------PROCEDIMIENTOS ALMACENADOS -----------------------
-- -------------------------- Productos -----------------------------------------------------
-- Agregar Productos

Delimiter $$
	Create procedure sp_AgregarProducto(in nombreProducto varchar(150) , in cantidadProducto int)
		Begin
			Insert into Productos(nombreProducto, cantidadProducto)
				values(nombreProducto, cantidadProducto);
		End $$
Delimiter ;

call sp_AgregarProducto('Carne enlatada.', 20);
call sp_AgregarProducto('Camarones', 20);
call sp_AgregarProducto('Carne enlatada', 20);
call sp_AgregarProducto('Bistec', 50);
call sp_AgregarProducto('Pescado', 35);
call sp_AgregarProducto('Sazonador',100);
call sp_AgregarProducto('Saborizante', 100);
call sp_AgregarProducto('Cebolla grande', 300);
call sp_AgregarProducto('Carton huevos',99);
call sp_AgregarProducto('Frijoles Ducal',80);
call sp_AgregarProducto('Pan Bimbo', 200);


-- Editar Productos

Delimiter $$
	Create procedure sp_EditarProducto(in codProducto int, in nomProducto varchar(150), in cant int)
		Begin
			Update Productos P
				Set nombreProducto = nomProducto, cantidadProducto = cant
					Where codigoProducto = codProducto;
		End $$
Delimiter ;

call sp_EditarProducto(1, 'Carne Enlatada.', 25);

-- Eliminar Productos

Delimiter $$
	Create procedure sp_EliminarProducto(in codProducto int)
		Begin
			Delete from Productos 
				Where codigoProducto = codProducto;
		End $$
Delimiter ;

-- call sp_EliminarProducto(1);

-- Listar Productos

Delimiter $$
	Create procedure sp_ListarProductos()
		Begin
			Select
				P.codigoProducto,
				P.nombreProducto,
				P.cantidadProducto
					From Productos P;
		End $$
Delimiter ;

call sp_ListarProductos();

-- Buscar Productos

Delimiter $$
	Create procedure sp_BuscarProducto(in codProducto int)
		Begin
			Select
				P.codigoProducto,
				P.nombreProducto,
				P.cantidadProducto
					From Productos P
						Where P.codigoProducto = codProducto;
		End $$
Delimiter ;

call sp_BuscarProducto(1);

-- ---------------------------------------PROCEDIMIENTOS ALMACENADOS -----------------------
-- -------------------------- Empleados -----------------------------------------------------
-- Agregar Empleados

Delimiter $$
	Create procedure sp_AgregarEmpleado(in numeroEmpleado int, in apellidosEmpleado varchar (150), 
		in nombresEmpleado varchar(150), in direccionEmpleado varchar(150), in telefonoContacto varchar(8),
        in gradoCocinero varchar(50), in codigoTipoEmpleado int)
		Begin
			Insert into Empleados (numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, 
				telefonoContacto, gradoCocinero, codigoTipoEmpleado)
					values(numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, 
					telefonoContacto, gradoCocinero, codigoTipoEmpleado);
		End $$
Delimiter ;

call sp_AgregarEmpleado(1, 'Perez Santos', 'Mario Alejandro', '16 calle 8-13 zona 3 Villa Nueva', 
	'55670909', 'Chef', 1);
call sp_AgregarEmpleado(2, 'Tepaz Bonilla', 'Fernando Salías', '3 calle 9-20 zona 4 Guatemala', '68987755', 'Ninguno', 2);
call sp_AgregarEmpleado(3, 'Molina Alarcón','Juliana Regina', '13ra. Calle 30-21', '23546724', 'Camarera', 3);
call sp_AgregarEmpleado(4, 'Méndez Juarez', 'Catalina Sofia', '"Frutales" 21 calle 1-09', '55880099', 'Bartender', 4);
call sp_AgregarEmpleado(5, 'Ballesteros Llopis', 'María Clarisa', '"Margaritas" 20 calle 13-08', '99775454', 'Chef', 5);
call sp_AgregarEmpleado(6, 'Alvarado Bueno', 'Javier Ivan', '"Girasoles" 25 calle 3-08', '32342674', 'Senior Chef', 6);
call sp_AgregarEmpleado(7, 'González Sierra', 'Daniel Julian', '"Villas linda" 16 calle 8-13', '43225523', 'Sous Chef', 7);
call sp_AgregarEmpleado(8, 'Benavente Ferrández', 'Ruben Alejandro', '12va. Avenida 09-22', '23447766', 'Segundo Cocinero', 8);
call sp_AgregarEmpleado(9, 'Díaz Donoso', 'Julio Alonso', '"Linda Vista" 21 calle 1-09', '21006541', 'Tercer Cocinero', 9);
call sp_AgregarEmpleado(10, 'Pacheco Pozo', 'Jose Brian', '10ma. Avenida 20-31, Cd. Guatemala', '43446724', 'Ninguno', 10);
-- Editar Empleados

Delimiter $$
	Create procedure sp_EditarEmpleado(in codEmpleado int, in numEmpleado int, in apelliEmpleado varchar(150),
		in nomEmpleado varchar(150), in direcEmpleado varchar(150), in telContacto varchar(8),
        in gradCocinero varchar(50))
		Begin
			Update Empleados E 
				Set E.numeroEmpleado=numEmpleado, E.apellidosEmpleado=apelliEmpleado, 
					E.nombresEmpleado=nomEmpleado, E.direccionEmpleado= direcEmpleado, E.telefonoContacto=telContacto,
					E.gradoCocinero= gradCocinero
						Where E.codigoEmpleado=codEmpleado;
		End $$
Delimiter ;

call sp_EditarEmpleado(1, 1, 'Perez Santos', 'Mario Alejandro', '16 calle 8-13 zona 3 Villa Nueva', 
	'55670909', 'Sub Chef');
    
-- Eliminar Empleados

Delimiter $$
	Create procedure sp_EliminarEmpleado(in codEmpleado int)
		Begin
			Delete from Empleados 
				Where codigoEmpleado = codEmpleado;
		End $$	
Delimiter ;

-- call sp_EliminarEmpleado(1);

-- Listar Empleados

Delimiter $$
	Create procedure sp_ListarEmpleados()
		Begin
			Select
				E.codigoEmpleado,
				E.numeroEmpleado,
				E.apellidosEmpleado,
				E.nombresEmpleado,
				E.direccionEmpleado,
				E.telefonoContacto,
				E.gradoCocinero,
				E.codigoTipoEmpleado
					From Empleados E;
		End $$
Delimiter ;

call sp_ListarEmpleados();

-- Buscar Empleados

Delimiter $$
	Create procedure sp_BuscarEmpleado(in codEmpleado int)
		Begin
			Select
				E.codigoEmpleado,
				E.numeroEmpleado,
				E.apellidosEmpleado,
				E.nombresEmpleado,
				E.direccionEmpleado,
				E.telefonoContacto,
				E.gradoCocinero,
				E.codigoTipoEmpleado
					From Empleados E
						Where E.codigoEmpleado = codEmpleado;
		End $$
Delimiter ;

call sp_BuscarEmpleado(1);

-- ---------------------------------------PROCEDIMIENTOS ALMACENADOS -----------------------
-- -------------------------- Servicios -----------------------------------------------------
-- Agregar Servicios

Delimiter $$
	Create procedure sp_AgregarServicio(in fechaServicio date, in tipoServicio varchar(150), in horaServicio time, 
    in lugarServicio varchar(150), in telefonoContacto varchar(8), in codigoEmpresa int)
		Begin
			Insert into Servicios(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
				values(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa);
		End $$
Delimiter ;

call sp_AgregarServicio('2023-04-06', 'Boda','08:27' ,'Capilla "Las Palmeras, Cd. Guatemala"' , '23234545', 1);
call sp_AgregarServicio('2023-05-06', 'Fiesta 15 Años.','08:27' ,'Hotel "Los Girasoles, Cd. Guatemala"' , '22334455', 2);
call sp_AgregarServicio('2023-02-12', 'Almuerzo', '12:30', 'Residencia Perez Sector 2', '23236655', 3);
call sp_AgregarServicio('2023-03-12', 'Desayuno', '7:00', 'Restaurante "Lo mejor", cd.Guatemala', '23324455',  4);
call sp_AgregarServicio('2023-04-15', 'Almuerzo', '14:00', 'Salón Comercial de la Cede', '45546766', 5);
call sp_AgregarServicio('2023-05-30', 'Fiesta empresarial', '20:00', 'Salón del Area de Reunión', '33224455', 6);
call sp_AgregarServicio('2023-06-20', 'Boda', '17:00', 'Centro Salón Santa Clara', '32224322', 7);
call sp_AgregarServicio('2023-06-12', 'Almuerzo particular', '14:00', 'Restaurante "Los Cebollines"', '33778778', 8);
call sp_AgregarServicio('2023-07-01', 'Cena formal', '21:00', 'Capilla "Los Santos", cd.Guatemala', '98890000', 9);
call sp_AgregarServicio('2023-07-10', 'Convivio', '16:00', 'Hotel "Señorial", cd.Guatemala', '43331566', 10 );

-- call sp_AgregarServicio();

-- Editar Servicios 

Delimiter $$
	Create procedure sp_EditarServicio(in codServicio int, in feServicio date, in tipServicio varchar(150), in horServicio time,
		in lugServicio varchar(150), in telContacto varchar(8))
		Begin
			Update Servicios S
				Set S.fechaServicio = feServicio, S.tipoServicio = tipServicio, S.horaServicio = horServicio,
					S.lugarServicio = lugServicio, S.telefonoContacto = telContacto
						Where S.codigoServicio = codServicio;
		End $$
Delimiter ;

call sp_EditarServicio('1', '2023-04-06', 'Boda/Comida','08:27' ,'Capilla "Las Palmeras, Cd. Guatemala"' , '23234545');

-- Eliminar Servicios

Delimiter $$
	Create procedure sp_EliminarServicio(in codServicio int)
		Begin
			Delete from Servicios 
				Where codigoServicio = codServicio;
		End $$
Delimiter ;

-- call sp_EliminarServicio(1);

-- Listar Servicios

Delimiter $$
	Create procedure sp_ListarServicios()
		Begin 
			Select 
				S.codigoServicio,
				S.fechaServicio,
                S.tipoServicio,
				S.horaServicio,
				S.lugarServicio,
				S.telefonoContacto,
				S.codigoEmpresa
					From Servicios S;
		End $$
Delimiter ;

call sp_ListarServicios();

-- Buscar Servicios

Delimiter $$
	Create procedure sp_BuscarServicio(in codServicio int)
		Begin
			Select
				S.codigoServicio,
				S.fechaServicio,
                S.tipoServicio,
				S.horaServicio,
				S.lugarServicio,
				S.telefonoContacto,
				S.codigoEmpresa
					From Servicios S
						Where S.codigoServicio = codServicio;
		End $$
Delimiter ;

call sp_BuscarServicio(1);

-- ---------------------------------------PROCEDIMIENTOS ALMACENADOS -----------------------
-- -------------------------- Presupuesto -----------------------------------------------------
-- Agregar Presupuesto

Delimiter $$
	Create procedure sp_AgregarPresupuesto(in fechaSolicitud date, in cantidadPresupuesto decimal(10,2), 
		in codigoEmpresa int)
		Begin
			Insert into Presupuesto(fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
				values(fechaSolicitud, cantidadPresupuesto, codigoEmpresa);
		End $$	
Delimiter ;

call sp_AgregarPresupuesto('2023-03-07', 4200, 1);
 call sp_AgregarPresupuesto('2023-04-12',5000,  2);
 call sp_AgregarPresupuesto('2023-05-29', 6000, 3);
 call sp_AgregarPresupuesto('2023-07-11', 3000, 4);
 call sp_AgregarPresupuesto('2023-06-12', 4500, 5);
 call sp_AgregarPresupuesto('2023-07-04', 6300, 6);
 call sp_AgregarPresupuesto('2023-06-29', 5400, 7);
 call sp_AgregarPresupuesto('2023-06-20', 6150, 8);
 call sp_AgregarPresupuesto('2023-07-25', 7000, 9);
 call sp_AgregarPresupuesto('2023-06-03', 6200, 10);


-- Editar Presupuesto

Delimiter $$
	Create procedure sp_EditarPresupuesto(in codPresupuesto int, in fecSolicitud date, in cantPresupuesto decimal(10,2))
		Begin
			Update Presupuesto P
				Set 
					P.fechaSolicitud = fecSolicitud, P.cantidadPresupuesto = cantPresupuesto
						Where P.codigoPresupuesto = codPresupuesto;
		End $$
Delimiter ;

call sp_EditarPresupuesto(1, '2023-03-07', 4500);

-- Eliminar Presupuesto

Delimiter $$
	Create procedure sp_EliminarPresupuesto(in codPresupuesto int)
		Begin
			Delete from Presupuesto 
				Where codigoPresupuesto = codPresupuesto;
		End $$
Delimiter ;

-- call sp_EliminarPresupuesto(1);

-- Listar Presupuesto

Delimiter $$
	Create procedure sp_ListarPresupuestos()
		Begin
			Select
				P.codigoPresupuesto,
				P.fechaSolicitud,
				P.cantidadPresupuesto,
				P.codigoEmpresa
					from Presupuesto P;
		End $$
Delimiter ;

call sp_ListarPresupuestos();

-- Buscar Presupuesto

Delimiter $$
	Create procedure sp_BuscarPresupuesto(in codPresupuesto int)
		Begin
			Select
				P.codigoPresupuesto,
				P.fechaSolicitud,
				P.cantidadPresupuesto,
				P.codigoEmpresa
					from Presupuesto P
						Where P.codigoPresupuesto = codPresupuesto;
		End $$
Delimiter ;

call sp_BuscarPresupuesto(1);

-- ---------------------------------------PROCEDIMIENTOS ALMACENADOS -----------------------
-- -------------------------- Platos -----------------------------------------------------
-- Agregar Platos

Delimiter $$
	Create procedure sp_AgregarPlato(in cantidad int, in nombrePlato varchar(50), 
		in descripcionPlato varchar(150), in precioPlato decimal(10,2), in codigoTipoPlato int)
		Begin
			Insert into Platos(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato)
				Values(cantidad, nombrePlato, descripcionPlato, precioPlato, codigoTipoPlato);
		End $$
Delimiter ;

call sp_AgregarPlato(20, 'Ceviche de Camarón', 'Ingr: Camarón, cebolla, tomate, pescado.', 75,1);
call sp_AgregarPlato(30, 'Pepián', 'Platillo tradicional guatemalteco.', 55, 2);
call sp_AgregarPlato(40, 'HotDog Italiano', 'Ingr: Queso, salsa, salchicha italiana.', 50, 3);
call sp_AgregarPlato(50, 'Tacos Chinos', 'Taco de buena porción con verduras y carne.', 35, 4);
call sp_AgregarPlato(60, 'Omelette Premiere', 'Huevo, queso mozarella, salsa.', 25, 5);
call sp_AgregarPlato(30, 'Pad Thai', 'Comida tradicional tailandesa.', 60.50, 6);
call sp_AgregarPlato(25, 'Piña Colada', 'Jugo de piña, ron', 24.50, 7);
call sp_AgregarPlato(35, 'Nachos con Queso', 'Entrada completa.', 20, 8  );
call sp_AgregarPlato(70, 'Pasta Italiana', 'Carne preparada, salsas', 64, 9);
call sp_AgregarPlato(50, 'Sopa a preferencia', 'Mexicana/Guatemalteca/Española', 30, 10);
-- Editar Platos

Delimiter $$
	Create procedure sp_EditarPlato(in codPlato int,in cant int, in nomPlato varchar(50), 
		in descripPlato varchar(150), in precPlato decimal(10,2))
		Begin
			Update Platos Pl
				Set
					Pl.cantidad = cant,
					Pl.nombrePlato = nomPlato,
					Pl.descripcionPlato = descripPlato,
					Pl.precioPlato = precPlato
						Where Pl.codigoPlato = codPlato; 
		End $$
Delimiter ;

call sp_EditarPlato(1,25, 'Ceviche de Camarón', 'Ingr: Camarón, cebolla, tomate, pescado.', 75);

-- Eliminar Platos 

Delimiter $$
	Create procedure sp_EliminarPlato(in codPlato int)
		Begin
			Delete from Platos 
				Where codigoPlato = codPlato;
		End $$
Delimiter ;

-- call sp_EliminarPlato(1);

-- Listar Platos

Delimiter $$
	Create procedure sp_ListarPlatos()
		Begin
			Select 
				Pl.codigoPlato,
				Pl.cantidad,
				Pl.nombrePlato,
				Pl.descripcionPlato,
				Pl.precioPlato,
				Pl.codigoTipoPlato
					From Platos Pl;
		End $$
Delimiter ;

call sp_ListarPlatos();

-- Buscar Platos

Delimiter $$
	Create procedure sp_BuscarPlato(in codPlato int)
		Begin
			Select 
				Pl.codigoPlato,
				Pl.cantidad,
				Pl.nombrePlato,
				Pl.descripcionPlato,
				Pl.precioPlato,
				Pl.codigoTipoPlato
					From Platos Pl
						Where Pl.codigoPlato = codPlato;
		End $$
Delimiter ;

call sp_BuscarPlato(1);

-- ---------------------------------------PROCEDIMIENTOS ALMACENADOS -----------------------
-- -------------------------- Productos_has_Platos -----------------------------------------------------
-- Agregar Productos_has_Platos

Delimiter $$
	Create procedure sp_AgregarProducto_has_Plato(in Productos_codigoProducto int, in codigoPlato int, in codigoProducto int)
		Begin
			Insert into Productos_has_Platos(Productos_codigoProducto, codigoPlato, codigoProducto)
				Values(Productos_codigoProducto, codigoPlato, codigoProducto);
		End $$
Delimiter ;

call sp_AgregarProducto_has_Plato(1, 1,1);
call sp_AgregarProducto_has_Plato(2, 2,2);
call sp_AgregarProducto_has_Plato(3, 3,3);
call sp_AgregarProducto_has_Plato(4, 4,4);
call sp_AgregarProducto_has_Plato(5, 5,5);
call sp_AgregarProducto_has_Plato(6, 6,6);
call sp_AgregarProducto_has_Plato(7, 7,7);
call sp_AgregarProducto_has_Plato(8, 8,8);
call sp_AgregarProducto_has_Plato(9, 9,9);
call sp_AgregarProducto_has_Plato(10, 10,10);


-- Editar Productos_has_Platos
--  NO PUEDEN EDITARSE YA QUE CONTIENE SOLO LLAVES FORANEAS
Delimiter $$
	Create procedure sp_EditarProducto_has_Plato(in Product_codProduct int, in codPlato int)
		Begin
			Update Productos_has_Platos PP
				set
					PP.codigoPlato = codPlato
						Where PP.Productos_codigoProducto = Product_codProduct;
        End $$
Delimiter ;

-- call sp_EditarProducto_has_Plato(1,1);

-- Eliminar Productos_has_Platos

Delimiter $$
	Create procedure sp_EliminarProducto_has_Plato(in Product_codProduct int)
		Begin
			Delete from Productos_has_Platos 
				Where Productos_codigoProducto = Product_codProduct;
        End $$
Delimiter ;

-- call sp_EliminarProducto_has_Plato(1);

-- Listar Productos_has_Platos

Delimiter $$
	Create procedure sp_ListarProductos_has_Platos()
		Begin
			Select
				PP.Productos_codigoProducto, 
				PP.codigoPlato, 
                PP.codigoProducto
					From Productos_has_Platos PP;
        End $$
Delimiter ;

call sp_ListarProductos_has_Platos();

-- Buscar Productos_has_Platos

Delimiter $$
	Create procedure sp_BuscarProducto_has_Plato(in Product_codProduct int)
		Begin
			Select
				PP.Productos_codigoProducto, 
				PP.codigoPlato, 
                PP.codigoProducto
					From Productos_has_Platos PP
						Where PP.Productos_codigoProducto = Product_codProduct;
        End $$
Delimiter ;

call sp_BuscarProducto_has_Plato(1);
 

-- ---------------------------------------PROCEDIMIENTOS ALMACENADOS -----------------------
-- -------------------------- Servicios_has_Platos -----------------------------------------------------
-- Agregar Servicios_has_Platos

Delimiter $$
	Create procedure sp_AgregarServicio_has_Plato(in Servicios_codigoServicio int, in codigoPlato int, in codigoServicio int)
		Begin
			Insert into Servicios_has_Platos(Servicios_codigoServicio, codigoPlato, codigoServicio)
				Values(Servicios_codigoServicio, codigoPlato, codigoServicio);
        End $$
Delimiter ;

call sp_AgregarServicio_has_Plato(1,1,1);
call sp_AgregarServicio_has_Plato(2,2,2);
call sp_AgregarServicio_has_Plato(3,3,3);
call sp_AgregarServicio_has_Plato(4,4,4);
call sp_AgregarServicio_has_Plato(5,5,5);
call sp_AgregarServicio_has_Plato(6,6,6);
call sp_AgregarServicio_has_Plato(7,7,7);
call sp_AgregarServicio_has_Plato(8,8,8);
call sp_AgregarServicio_has_Plato(9,9,9);
call sp_AgregarServicio_has_Plato(10,10,10);


select*from Servicios_has_Platos;

-- Editar Servicios_has_Platos

Delimiter $$
	Create procedure sp_EditarServicio_has_Plato(in Servicios_codServicio int, in codPlato int, in codServicio int )
		Begin
			Update Servicios_has_Platos SP
				Set
                    SP.codigoPlato = codPlato,
                    SP.codigoServicio = codServicio
						Where SP.Servicios_codigoServicio = Servicios_codServicio;
        End $$
Delimiter ;

call sp_EditarServicio_has_Plato(1,1,1);

-- Eliminar Servicios_has_Platos

Delimiter $$
	Create procedure sp_EliminarServicio_has_Plato(in Servicios_codServicio int )
		Begin
			Delete from Servicios_has_Platos 
				Where Servicios_codigoServicio = Servicios_codServicio;
        End $$
Delimiter ;

-- call sp_EliminarServicio_has_Plato(1);

-- Listar Servicios_has_Platos

Delimiter $$
	Create procedure sp_ListarServicios_has_Platos()
		Begin
			Select 
				SP.Servicios_codigoServicio,
				SP.codigoPlato ,
				SP.codigoServicio 
					From Servicios_has_Platos SP;
        End $$
Delimiter ;

call sp_ListarServicios_has_Platos();

-- Buscar Servicios_has_Platos

Delimiter $$
	Create procedure sp_BuscarServicio_has_Plato(in Servicios_codServicio int)
		Begin
			Select 
				SP.Servicios_codigoServicio,
				SP.codigoPlato ,
				SP.codigoServicio 
					From Servicios_has_Platos SP
						Where SP.Servicios_codigoServicio = Servicios_codServicio;
        End $$
Delimiter ;


-- ---------------------------------------PROCEDIMIENTOS ALMACENADOS -----------------------
-- -------------------------- Servicios_has_Empleados -----------------------------------------------------
-- Agregar Servicios_has_Empleados


Delimiter $$
	Create procedure sp_AgregarServicio_has_Empleado(in Servicios_codigoServicio int, in codigoServicio int, 
		in codigoEmpleado int, in fechaEvento date, in horaEvento time, in lugarEvento varchar(150))
		Begin
			Insert into Servicios_has_Empleados(Servicios_codigoServicio, codigoServicio, 
				codigoEmpleado, fechaEvento, horaEvento, lugarEvento)
				values(Servicios_codigoServicio, codigoServicio, codigoEmpleado, 
					fechaEvento, horaEvento, lugarEvento);
        End $$
Delimiter ;

call sp_AgregarServicio_has_Empleado(1, 1, 1, '2023-04-06', '09:00', 'Capilla "Las Palmeras, Cd. Guatemala"');
call sp_AgregarServicio_has_Empleado(2, 2, 2, '2023-05-06', '09:00', 'Hotel Galaus, Zacapa.');
call sp_AgregarServicio_has_Empleado(3,3,3, '2023-02-12', '14:00', 'Residencia Perez Sector 2');
call sp_AgregarServicio_has_Empleado(4,4,4, '2023-03-12', '08:30', 'Restaurante "Lo mejor", cd.Guatemala');
call sp_AgregarServicio_has_Empleado(5,5,5, '2023-04-15', '14:30', 'Salón Comercial de la Cede');
call sp_AgregarServicio_has_Empleado(6,6,6, '2023-05-30', '21:30', 'Salón del Area de Reunión');
call sp_AgregarServicio_has_Empleado(7,7,7, '2023-06-20', '18:00', 'Centro Salón Santa Clara');
call sp_AgregarServicio_has_Empleado(8,8,8,'2023-06-12', '15:00', 'Restaurante "Los Cebollines"');
call sp_AgregarServicio_has_Empleado(9,9,9, '2023-07-01', '21:00', 'Capilla "Los Santos", cd.Guatemala');
call sp_AgregarServicio_has_Empleado(10,10,10, '2023-07-10', '16:00', 'Hotel "Señorial", cd.Guatemala');
-- Editar Servicios_has_Empleados

Delimiter $$
	Create procedure sp_EditarServicio_has_Empleado(in Servicios_codServicio int, in fecEvento date, in hoEvento time, in lugEvento varchar(150))
        Begin
			Update Servicios_has_Empleados SE 
				Set
                    SE.fechaEvento = fecEvento,
                    SE.horaEvento = hoEvento,
                    SE.lugarEvento = lugEvento
						Where SE.Servicios_codigoServicio = Servicios_codServicio;
        End $$
Delimiter ;

call sp_EditarServicio_has_Empleado(1, '2023-04-06', '09:30', 'Capilla "Las Palmeras, Cd. Guatemala"');

-- Eliminar Servicios_has_Empleados

Delimiter $$
	Create procedure sp_EliminarServicio_has_Empleado(in Servicios_codServicio int)
		Begin
			Delete from Servicios_has_Empleados 
				Where Servicios_codigoServicio = Servicios_codServicio;
        End $$
Delimiter ;

-- call sp_EliminarServicio_has_Empleado(1);

-- Listar Servicios_has_Empleados

Delimiter $$
	Create procedure sp_ListarServicios_has_Empleados()
		Begin
			Select
				SE.Servicios_codigoServicio, 
                SE.codigoServicio, 
                SE.codigoEmpleado, 
                SE.fechaEvento, 
                SE.horaEvento, 
                SE.lugarEvento
					From Servicios_has_Empleados SE;
        End $$
Delimiter ;

call sp_ListarServicios_has_Empleados();

-- Buscar Servicios_has_Empleados

Delimiter $$
	Create procedure sp_BuscarServicio_has_Empleado(in Servicios_codServicio int)
		Begin
			Select
				SE.Servicios_codigoServicio,
                SE.codigoServicio,
                SE.codigoEmpleado,
                SE.fechaEvento,
                SE.horaEvento,
                SE.lugarEvento
					From Servicios_has_Empleados SE 
						Where SE.Servicios_codigoServicio = Servicios_codServicio;
        End $$
Delimiter ;

call sp_BuscarServicio_has_Empleado(1);
 -- ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'francosql134';
 


Create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar(100) not null,
    apellidoUsuario varchar(100) not null,
    usuarioLogin varchar(50) not null unique,
    contrasena varchar(50) not null unique,
    primary key PK_codigoUsuario(codigoUsuario)
);

Create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    primary key PK_usuarioMaster(usuarioMaster)
);
-- -----------PROCEDIMIENTOS LOGIN/USUARIO -----------
Delimiter $$
	Create procedure sp_AgregarUsuario (in nombreUsuario varchar(100), in apellidoUsuario varchar(100),
		in usuarioLogin varchar(50), in contrasena varchar(50))
	Begin
		Insert into Usuario(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
			values(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
    End $$
Delimiter ;

Delimiter $$
	Create procedure sp_ListarUsuarios()
    Begin
		Select U.codigoUsuario,
				U.nombreUsuario,
                U.apellidoUsuario,
                U.usuarioLogin,
                U.contrasena
			from Usuario U;
    End $$
Delimiter ;

call sp_AgregarUsuario('Franco', 'Alejandro', 'fpaiz', 'fp134');
call sp_AgregarUsuario('Pedro', 'Armas', 'parmas', 'parmas');
call sp_ListarUsuarios();


-- Procedimiento Almacenado para el reporte General

Delimiter $$
	Create procedure sp_ReporteGeneral(in codEmpresa int)
		Begin
			Select E.nombreEmpresa, E.direccion, E.telefono, P.cantidadPresupuesto, P.fechaSolicitud, S.tipoServicio,S.horaServicio,S.lugarServicio, S.fechaServicio, S.telefonoContacto,
            SHE.fechaEvento, SHE.horaEvento, SHE.lugarEvento, EM.apellidosEmpleado, EM.nombresEmpleado, TE.descripcion, PL.nombrePlato, PL.cantidad, PL.precioPlato, TP.descripcionTipo, PRO.nombreProducto, PRO.cantidadProducto
			from Empresas E
            Inner Join Servicios S
            On S.codigoEmpresa = E.codigoEmpresa
            Inner Join Presupuesto P
            On E.codigoEmpresa = P.codigoEmpresa  
            Inner Join Servicios_has_Empleados SHE
            On SHE.codigoServicio = S.codigoServicio
            Inner Join Empleados EM
            On EM.codigoEmpleado = SHE.codigoEmpleado
            Inner Join TipoEmpleado TE
            On TE.codigoTipoEmpleado = EM.codigoTipoEmpleado
            Inner Join Servicios_has_Platos SHP
            On SHP.codigoServicio = S.codigoServicio
            Inner Join Platos PL
            On PL.codigoPlato = SHP.codigoPlato
			Inner join TipoPlato TP
            On TP.codigoTipoPlato = PL.codigoTipoPlato
            Inner Join Productos_has_Platos PHP
            On PHP.codigoPlato = PL.codigoPlato
            Inner Join Productos PRO
            On PRO.codigoProducto = PHP.codigoProducto
            Where E.codigoEmpresa = codEmpresa;
        End $$
Delimiter ;

call sp_ReporteGeneral(1);