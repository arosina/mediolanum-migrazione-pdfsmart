use CEPE
go

-- Inizialmente attiviamo la versione tattica
UPDATE PDF_CONFIG SET ATTIVO = 'S' WHERE SEZIONE = 'AML' AND PARAMETRO = 'MODULI_CON_QUESTIONARIO_AML_AN43'
UPDATE PDF_CONFIG SET ATTIVO = 'S' WHERE SEZIONE = 'AML' AND PARAMETRO = 'MODULI_CON_QUESTIONARIO_AML_AN44'
go

-- Configurazione BAN9 non attiva
DELETE FROM PDF_CONFIG WHERE SEZIONE = 'AML' AND PARAMETRO = 'MODULI_CON_QUESTIONARIO_AML_BAN9'
INSERT INTO PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
VALUES ('AML', 'MODULI_CON_QUESTIONARIO_AML_BAN9', 'fondiitaliainiziale,fondiitaliaaggiuntivo,fondiirlandesiiniziale,fondiirlandesiiniziale,fondiirlandesiaggiuntivo,fonditerziiniziale,fonditerziaggiuntivo,mylifeiniziale,mylifeaggiuntivo,intelligentlifeplan,intelligentlifeplanaggiuntivo,polizzapersonalpir,personalpiraggiuntivo,lifefunds,polizzeunitvitaaggiuntivo,mediolanumcapitalnew,newgeneration,premiumplan,polizzeunitmilaggiuntivo,polizzemiltranche,taxbenefitnew,taxbenefitnewaggiuntivo,taxbenefitnewvariazione,taxbenefitoldaggiuntivo,gpminiziale,gpmaggiuntivo,piralternativoiniziale,piralternativoaggiuntivo', 'N', 'Elenco moduli che agganciano il questionario AML BAN9')
GO

-- Configurazione processo tattico
DELETE FROM PDF_CONFIG WHERE SEZIONE = 'AML' AND PARAMETRO = 'PROCESSO_TATTICO'
INSERT INTO PDF_CONFIG (SEZIONE, PARAMETRO, VALORE, ATTIVO, DESCRIZIONE)
VALUES ('AML', 'PROCESSO_TATTICO', 'S', 'S', 'Impostazione processo tattico (S) target (N)')
GO

PRINT "AML configurato"
go


