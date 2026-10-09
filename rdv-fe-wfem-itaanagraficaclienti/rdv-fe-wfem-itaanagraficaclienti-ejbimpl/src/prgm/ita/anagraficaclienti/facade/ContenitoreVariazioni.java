package prgm.ita.anagraficaclienti.facade;

class ContenitoreVariazioni{
	
	boolean natoInizialeUs;
	boolean natoFinaleUs;
	boolean cittInizialeUs;
	boolean cittFinaleUs;
	boolean residenzaInizialeUs;
	boolean residenzaFinaleUs;
	public ContenitoreVariazioni(boolean natoInizialeUs,
			boolean natoFinaleUs, boolean cittInizialeUs,
			boolean cittFinaleUs, boolean residenzaInizialeUs,
			boolean residenzaFinaleUs) {
		this.natoInizialeUs = natoInizialeUs;
		this.natoFinaleUs = natoFinaleUs;
		this.cittInizialeUs = cittInizialeUs;
		this.cittFinaleUs = cittFinaleUs;
		this.residenzaInizialeUs = residenzaInizialeUs;
		this.residenzaFinaleUs = residenzaFinaleUs;
	}
	
	public ContenitoreVariazioni(boolean natoInizialeUs, boolean cittInizialeUs,
			boolean cittFinaleUs, boolean residenzaInizialeUs,
			boolean residenzaFinaleUs){
		this.natoInizialeUs = natoInizialeUs;
		this.natoFinaleUs = natoInizialeUs;
		this.cittInizialeUs = cittInizialeUs;
		this.cittFinaleUs = cittFinaleUs;
		this.residenzaInizialeUs = residenzaInizialeUs;
		this.residenzaFinaleUs = residenzaFinaleUs;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (cittFinaleUs ? 1231 : 1237);
		result = prime * result + (cittInizialeUs ? 1231 : 1237);
		result = prime * result + (natoFinaleUs ? 1231 : 1237);
		result = prime * result + (natoInizialeUs ? 1231 : 1237);
		result = prime * result + (residenzaFinaleUs ? 1231 : 1237);
		result = prime * result + (residenzaInizialeUs ? 1231 : 1237);
			
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContenitoreVariazioni other = (ContenitoreVariazioni) obj;
		if (cittFinaleUs != other.cittFinaleUs)
			return false;
		if (cittInizialeUs != other.cittInizialeUs)
			return false;
		if (natoFinaleUs != other.natoFinaleUs)
			return false;
		if (natoInizialeUs != other.natoInizialeUs)
			return false;
		if (residenzaFinaleUs != other.residenzaFinaleUs)
			return false;
		if (residenzaInizialeUs != other.residenzaInizialeUs)
			return false;
		return true;
	}
	
	
	
	
	
}