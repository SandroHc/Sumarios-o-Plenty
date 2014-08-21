<?php
	include('../config.php');

	if(!isSet($_SESSION["isAuth"])) {
		redirect("../login.php");
		die();
	}

	global $con,$db_database;

	$name = get("name");

	if(!isSet($name)) {
		$_SESSION["alert"] = "danger";
		$_SESSION["alert_desc"] = "Impossível remover lição.";
	} else {
		$query = "DELETE FROM $db_database.licoes WHERE licao='". $name ."'";

		$con = startConnection();
		if(!mysqli_query($con, $query))
			echo "Erro: ". $con->error;

		$_SESSION["alert"] = "success";
		$_SESSION["alert_desc"] = "Lição removida com sucesso.";
	}

	redirect("../index.php");