<ul id="header">
	<li>Sumários</li>
	<li>
		Disciplinas
		<ul>
			<li>
				Programação de Sistemas Informáticos
			</li>
			<li><?php echo ($_SESSION["isAuth"] ? "Sessão iniciada" : "Sessão não iniciada") ?></li>
		</ul>
	</li>
	<li data-toggle="modal" data-target="#addLicao" onClick="formClear()"><span class="glyphicon glyphicon-plus"></span></li>
</ul>