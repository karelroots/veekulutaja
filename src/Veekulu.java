public abstract class Veekulu {
	private String tähis;
	private String nimi;
	private double kaudne;
	private int kogus;

	public Veekulu(String tähis, double kaudne, int kogus) {
		this.tähis = tähis;
		this.kaudne = kaudne;
		this.kogus = kogus;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}

	public double getKaudne() {
		return kaudne;
	}
	public int getKogus() { return kogus; }

	public String getTähis() {
		return tähis;
	}

	public abstract double arvutaKulu();

	@Override
	public String toString() {
		return "Veekulu tähis=" + tähis + ", kulu=" + arvutaKulu();
	}







}
