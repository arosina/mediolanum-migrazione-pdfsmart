delete from PDF_MESSAGE_CODES where CONF_ITEM_NAME='pdfwebformsWarnings'
delete from PDF_MESSAGE_CODES where CONF_ITEM_NAME='pdfwebformsErrors'
delete from PDF_MESSAGE_CODES where CONF_ITEM_NAME='pdfwebformsTypeErrors'

-- WARNINGS
insert into PDF_MESSAGE_CODES (CONF_ITEM_NAME, CONF_ITEM_PROPERTIES) values ('pdfwebformsWarnings', 
"")

-- ERRORS
insert into PDF_MESSAGE_CODES (CONF_ITEM_NAME, CONF_ITEM_PROPERTIES) values ('pdfwebformsErrors', 
"
1=Nel modulo sono presenti errori di compilazione. Clicca sulle frecce rosse a sinistra per verificarli
")

-- TYPE ERRORS
insert into PDF_MESSAGE_CODES (CONF_ITEM_NAME, CONF_ITEM_PROPERTIES) values ('pdfwebformsTypeErrors', 
"
# #########################################
# Doppia spunta
# #########################################
VP=Valore precedente: (.*)

# #########################################
# Acroform
# #########################################
1=Campo obbligatorio
2=Valore non valido: deve essere (.*)
3=Valore non valido: deve essere superiore o uguale a (.*)
4=Valore non valido: deve essere inferiore o uguale a (.*)
5=Valore non valido: deve essere superiore o uguale a (.*) e inferiore o uguale a (.*)

# #########################################
# DataLoader
# #########################################
#	ndgCliente(n)
100=Non esiste un cliente con questo codice
101=Non esiste un cliente con questo codice prospect
102=E' necessario completare il censimento anagrafico del cliente

# #########################################
# BaseDriver
# #########################################
#	luogo*
200=Il luogo specificato non esiste.

#	descrToponimo*
201=Il toponimo specificato non esiste.

# 	codiceAgevolazione
202=L'agevolazione selezionata non è più valida. Selezionarne o richiederne una nuova.
203=La forma contrattuale indicata non è coerente con quanto specificato nella deroga selezionata.
204=Il numero mandato indicato non è coerente con quanto specificato nella deroga selezionata.
205=La deroga selezionata non è coerente con l'importo del versamento iniziale richiesto.
206=La deroga selezionata non è coerente con il totale dell'investimento richiesto.

#	campi conto*, iban*
207=Il codice Iban è errato.
208=Il conto specificato non esiste o non appartiene al cliente.
209=Attenzione! Il conto selezionato è bloccato: per proseguire è necessario selezionare un altro conto corrente.

#	provinciaComune*, capComune*, comune*
210=Il comune specificato non esiste.

#	ndgCliente(n)
211=Il profilo del cliente risulta scaduto o non compilato, non è possibile procedere con la richiesta.
212=Non è possibile procedere, il cliente risulta deceduto.
213=Non è possibile procedere, il cliente risulta revocato.

#	codiceAgente
214=Codice Family Banker non ammesso.
215=Family Banker non attivo.
216=Family Banker non abilitato al collocamento del prodotto.

#	dataSottoscrizione
217=La data di sottoscrizione del contratto non può essere antecedente la data di validità del mandato Family Banker.

#	tecnici
230=Family Banker non presente in anagrafica.
231=Data mandato non presente in anagrafica.

# #########################################
# MOM
# #########################################
#	idReportAdeguatezza
300=Nessuna pratica trovata per il codice report adeguatezza (.*)
301=Per il codice report adeguatezza (.*) è stata trovata la pratica (.*) che risulta ancora in lavorazione

")