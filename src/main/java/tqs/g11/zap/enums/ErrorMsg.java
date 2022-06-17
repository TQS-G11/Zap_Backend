package tqs.g11.zap.enums;

public enum ErrorMsg {
    PRODUCT_NOT_FOUND("Product with provided ID not found."),
    PRODUCT_NOT_ENOUGH_STOCK("Product does not have enough stock."),
    CART_PRODUCT_NOT_FOUND("Cart Product with provided ID not found."),
    WRONG_CLIENT_FOR_CART_PRODUCT("Provided User is not the Cart Product Owner."),
    DESTINATION_NOT_GIVEN("Provided Destination was '' or null.");


    private final String text;

    ErrorMsg(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
