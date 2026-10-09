function CodDesc(cod,descr){
	this.cod = cod;
	this.descr = descr;
}

var tipiProdotto = new Object();
tipiProdotto['CC'] = new CodDesc('CC','Conti correnti');
tipiProdotto['DT'] = new CodDesc('DT','Dossier titoli');
tipiProdotto['GP'] = new CodDesc('GP','Gestioni patrimoniali');
tipiProdotto['AL'] = new CodDesc('AL','Altro');
tipiProdotto['FO'] = new CodDesc('FO','Fondi');
tipiProdotto['PD'] = new CodDesc('PD','Polizze danni');
tipiProdotto['PV'] = new CodDesc('PV','Polizze vita');
tipiProdotto['CD'] = new CodDesc('CD','certificati di deposito');
tipiProdotto['PI'] = new CodDesc('PI','Polizze infortuni');
tipiProdotto['PS'] = new CodDesc('PS','Polizze sanitarie');
tipiProdotto['TP'] = new CodDesc('TP','Telepiù');
tipiProdotto['IM'] = new CodDesc('IM','Immobili multiproprietà');
tipiProdotto['IT'] = new CodDesc('IT','Immobili tradizionali');
tipiProdotto['PR'] = new CodDesc('PR','Prestiti');
tipiProdotto['MT'] = new CodDesc('MT','Mutui');

function onNewRowContratti(row){
	if(row.isChiuso == '1')
		row.style.backgroundColor = 'coral';
}

function onNewCellContratti(cell){
	if(cell.propertyName == 'numeroContratto' ||
	   cell.propertyName == 'numeroPolizza' ||
	   cell.propertyName == 'codProdotto')
		cell.align = 'center';
		
	if(cell.propertyName == 'desAgente')
		cell.innerHTML = cell.row.agente_codAgente+'<br>'+cell.row.agente_nominativo;
	if(cell.propertyName == 'desCliente')
		cell.innerHTML = cell.row.cliente_codMediolanum+'<br>'+cell.row.cliente_cognome+' '+cell.row.cliente_nome;
	if(cell.propertyName == 'tipoProdotto')
		cell.innerHTML = tipiProdotto[cell.row.tipoProdotto].descr;
		
}

function selContratto(contr){
	
	var ret = new Object();		
	ret.type = 'contratto';
	ret.tipoRicerca = document.dati.tipoRicerca.value;
	ret.tipoProdotto = contr.tipoProdotto;
	if(contr.numeroPolizza != '')
		ret.numeroContratto = contr.numeroPolizza;
	else
		ret.numeroContratto = contr.numeroContratto;
	ret.agente_codRete = contr.agente_codRete;
	ret.agente_codAgente = contr.agente_codAgente;
	ret.agente_nominativo = contr.agente_nominativo;
	ret.agente_codMediolanum = contr.agente_codMediolanum;
	ret.cliente_codMediolanum = contr.cliente_codMediolanum;
	ret.cliente_cognome = contr.cliente_cognome;
	ret.cliente_nome = contr.cliente_nome;
	ret.descrContratto = contr.descrContratto;

	if(parent.__isWlt){
		closeModalPopup(ret);	
	}else{
		returnValue = ret;
		window.close();
	}
}
