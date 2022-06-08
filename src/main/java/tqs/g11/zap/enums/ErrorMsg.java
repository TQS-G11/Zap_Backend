package tqs.g11.zap.enums;

public enum ErrorMsg {
    PRODUCT_NOT_FOUND("Product with provided ID not found."),
    PRODUCT_NOT_ENOUGH_STOCK("Product does not have enough stock.");

    private final String text;

    ErrorMsg(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
