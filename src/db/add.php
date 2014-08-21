<?php
	include('../config.php');

	if(!isSet($_SESSION["isAuth"])) {
		redirect("../login.php");
		die();
	}

	$licao = get("licao");
	$disciplina = get("disciplina");
	$modulo = get("modulo");
	$planificacao = get("planificacao");
	$data = get("data_mysql");

	$update = get("update");

	if(!isSet($licao) || !isSet($disciplina) || !isSet($planificacao)) {
		$_SESSION["alert"] = "danger";
		$_SESSION["alert_desc"] = "Impossível adicionar lição";
	} else {
		global $db_database;

		if($update)
			$query = "UPDATE $db_database.licoes SET licao=\"". $licao ."\", disciplina=". $disciplina .", modulo=\"". $modulo ."\", planificacao=\"". htmlspecialchars($planificacao) ."\", data=\"". $data ."\" WHERE licao=\"". $licao ."\"";
		else
			$query = "INSERT INTO $db_database.licoes (licao, disciplina, modulo, planificacao, data) VALUES (\"". $licao ."\", ". $disciplina .", \"". $modulo ."\", \"". htmlspecialchars($planificacao) ."\", \"". $data ."\")";

		$con = startConnection();
		if(!mysqli_query($con, $query))
			echo "Erro: ". $con->error;

		$_SESSION["alert"] = "success";
		$_SESSION["alert_desc"] = "Lição adicionada com sucesso.";
	}

	redirect("../index.php");