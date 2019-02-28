
public class RiieteVeekulu extends Veekulu {

    public RiieteVeekulu(String tähis, double kaudne) {
        super(tähis, kaudne, 1);
    }

    @Override
    public double arvutaKulu() {
        return super.getKaudne();
    }




}
