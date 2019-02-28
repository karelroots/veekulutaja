public class ToiduVeekulu extends Veekulu {
    private double otsene;

    public ToiduVeekulu(String tähis, int kogus, double kaudne, double otsene) {
        super(tähis, kaudne, kogus);
        this.otsene = otsene;

    }

    @Override
    public double arvutaKulu() {

        return super.getKogus()*super.getKaudne()+getKogus()*otsene;
    }









}
