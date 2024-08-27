package kr.oshino.eataku.common.enums;

public enum Scope {

    STAR1(1, "★☆☆☆☆"),
    STAR2(2, "★★☆☆☆"),
    STAR3(3, "★★★☆☆"),
    STAR4(4, "★★★★☆"),
    STAR5(5, "★★★★★");


    private final int value;
    private final String stars;

    Scope(int value, String stars) {
        this.value = value;
        this.stars = stars;
    }

    public int getValue() {
        return value;
    }

    public String getStars() {
        return stars;
    }

    @Override
    public String toString() {
        return stars;
    }

}
