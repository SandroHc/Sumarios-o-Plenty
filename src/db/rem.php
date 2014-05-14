<?php
	include('../config.php');

	if(!isSet($_SESSION["login"])) {
		header("Location: ../login.php");
		die();
	}

	global $con,$db_database;

	$name = get("name");

	if(!isSet($name)) {
		$_SESSION["err"] = "Impossível remover lição";
	} else {
		$query = "DELETE FROM $db_database.licoes WHERE licao='". $name ."'";

		startConnection();
		if(!mysqli_query($con, $query))
			echo "Erro: ". $con->error;
		endConnection();

		$_SESSION["succ"] = "Lição removida com sucesso";
	}

	header("Location: ../index.php");